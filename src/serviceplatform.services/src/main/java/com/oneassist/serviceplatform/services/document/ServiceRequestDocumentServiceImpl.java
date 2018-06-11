package com.oneassist.serviceplatform.services.document;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.DocTypeMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceDocumentEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntityDocumentEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.enums.FileType;
import com.oneassist.serviceplatform.commons.enums.ServiceReqeustEntityDocumentName;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestReport;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.filegenerator.BaseFileGenerator;
import com.oneassist.serviceplatform.commons.filegenerator.ServiceRequestReportFactory;
import com.oneassist.serviceplatform.commons.mongo.repositories.IMongoStorageDao;
import com.oneassist.serviceplatform.commons.proxies.OasysAdminProxy;
import com.oneassist.serviceplatform.commons.repositories.DocumentTypeMstRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceDocumentRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestEntityDocumentRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestTypeMstRepository;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.contracts.dtos.CmsDocumentDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDocumentDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestReportRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestReportResponseDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.DocumentDownloadRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceDocumentDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceDocumentRequestDto;
import com.oneassist.serviceplatform.contracts.response.DownloadDocumentDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import com.oneassist.serviceplatform.services.document.archival.IServiceRequestDocumentArchivalService;
import com.oneassist.serviceplatform.services.servicerequest.ServiceRequestServiceImpl;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.hibernate.exception.ConstraintViolationException;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class ServiceRequestDocumentServiceImpl implements IServiceRequestDocumentService {

    private final Logger logger = Logger.getLogger(ServiceRequestServiceImpl.class);

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ServiceDocumentRepository serviceDocumentRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    ServiceRequestHelper serviceRequestHelper;

    @Autowired
    private ServiceRequestReportFactory serviceRequestReportFactory;

    @Autowired
    private DocumentTypeMstRepository documentTypeMstRepository;

    @Autowired
    @Qualifier("mongoStorageDao")
    private IMongoStorageDao mongoStorageDao;

    @Autowired
    private ServiceRequestEntityDocumentRepository serviceRequestEntityDocumentRepository;

    @Autowired
    private IServiceRequestDocumentArchivalService serviceRequestDocumentArchivalService;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ServiceRequestTypeMstRepository serviceRequestTypeMstRepository;

    @Autowired
    private OasysAdminProxy oasysAdminProxy;

    @Override
    // @Transactional(rollbackFor = Exception.class, propagation =
    // Propagation.REQUIRED, readOnly = false)
    public ResponseDto<ServiceDocumentDto> uploadMultipleServiceDocuments(Long serviceRequestId, FormDataMultiPart multiPart, String documentId, String docTypeId,
            ServiceReqeustEntityDocumentName entityName, String entityId, String storageReferenceId, String documentKey, String isCMSCallRequired, String requestFileName) throws Exception {

        ResponseDto<ServiceDocumentDto> response = new ResponseDto<>();

        /*
         * Check if storageReferenceId and multiPart both are null then return error
         */
        if (StringUtils.isEmpty(storageReferenceId) && StringUtils.isEmpty(multiPart)) {
            response.setStatus(ResponseConstant.FAILED);
            response.setMessage("Either storageReferenceId or request document is required");
            return response;
        }

        /*
         * Check if storageReferenceId is not null and fileName is null then return error
         */
        if (!StringUtils.isEmpty(storageReferenceId) && StringUtils.isEmpty(requestFileName)) {
            response.setStatus(ResponseConstant.FAILED);
            response.setMessage("File name is required");
            return response;
        }

        /*
         * Check if docTypeId and documentKey both are null then return error
         */
        if (StringUtils.isEmpty(documentKey) && StringUtils.isEmpty(docTypeId)) {
            response.setStatus(ResponseConstant.FAILED);
            response.setMessage("Either documentTypeId or documentKey is mandatory to upload the document");
            return response;
        }

        isCMSCallRequired = StringUtils.isEmpty(isCMSCallRequired) ? Constants.YES_FLAG : isCMSCallRequired;

        /* Get service request detail by Service request ID */
        ServiceRequestEntity serviceRequestEntity = null;
        if (!StringUtils.isEmpty(documentKey) && !StringUtils.isEmpty(storageReferenceId)) {
            serviceRequestEntity = serviceRequestRepository.findByExternalSRReferenceId(serviceRequestId.toString());
        }
        if (serviceRequestEntity == null) {
            serviceRequestEntity = serviceRequestRepository.findServiceRequestEntityByServiceRequestId(serviceRequestId);
        }

        /* Get service request type detail by service request type Id */
        ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = serviceRequestTypeMstRepository.findServiceRequestTypeMstByServiceRequestTypeId(serviceRequestEntity.getServiceRequestTypeId());

        DocTypeMstEntity docTypeMstEntity = null;
        if (!StringUtils.isEmpty(documentKey)) {
            docTypeMstEntity = documentTypeMstRepository.findBydocName(documentKey);
            docTypeId = docTypeMstEntity.getDocTypeId().toString();
        } else {
            docTypeMstEntity = documentTypeMstRepository.findBydocTypeId(Long.parseLong(docTypeId));
        }

        ServiceDocumentEntity serviceDocumentEntity = null;
        String storageRefId = null;

        /* Check if storageReferenceId is not null or empty */
        if (!StringUtils.isEmpty(storageReferenceId) && StringUtils.isEmpty(multiPart)) {

            serviceDocumentEntity = this.populateServiceDocumentDtl(documentId, docTypeId, null, serviceRequestId, storageReferenceId);
            /* Save mongodb reference Id in table */
            serviceDocumentEntity = serviceDocumentRepository.save(serviceDocumentEntity);

            /* Check if service request type is for personal document */
            if (isCMSCallRequired.equals(Constants.YES_FLAG)) {
                // System.out.println("calling CMS..in create");
                if (!StringUtils.isEmpty(serviceRequestTypeMstEntity.getServiceRequestType())) {
                    if (serviceRequestTypeMstEntity.getServiceRequestType().equals(ServiceRequestType.PE_ADLD.getRequestType())
                            || serviceRequestTypeMstEntity.getServiceRequestType().equals(ServiceRequestType.PE_EW.getRequestType())
                            || serviceRequestTypeMstEntity.getServiceRequestType().equals(ServiceRequestType.PE_THEFT.getRequestType())) {

                        /* call CMS API to send mongoReferenceID */
                        CmsDocumentDto cmsDocumentDto = new CmsDocumentDto();
                        cmsDocumentDto.setDocKey(docTypeMstEntity.getDocName());
                        cmsDocumentDto.setDocValue(requestFileName);
                        oasysAdminProxy.sendDocumentDetailsToCMS(serviceRequestEntity.getExternalSRReferenceId(), storageReferenceId, cmsDocumentDto);
                    }
                }
            }
        }

        else if (!StringUtils.isEmpty(multiPart)) {

            List<BodyPart> bodyParts = multiPart.getBodyParts();
            for (int i = 0; i < bodyParts.size(); i++) {
                BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
                logger.info(bodyPartEntity);

                String fileName = bodyParts.get(i).getContentDisposition().getFileName();
                logger.info(fileName);

                InputStream fileInputStream = bodyPartEntity.getInputStream();
                String mimeType = bodyParts.get(i).getMediaType().toString();
                byte[] byteData = IOUtils.toByteArray(fileInputStream);

                ServiceDocumentRequestDto shipmentDocumentRequest = new ServiceDocumentRequestDto();
                shipmentDocumentRequest.setServiceRequestId(String.valueOf(serviceRequestId));
                shipmentDocumentRequest.setFileContentType(mimeType);
                shipmentDocumentRequest.setFileName(fileName);
                shipmentDocumentRequest.setDocumentByteArray(byteData);

                // See if document already exists
                if (!StringUtils.isEmpty(documentId)) {
                    serviceDocumentEntity = this.serviceDocumentRepository.findOne(documentId);
                }

                boolean isNewDoc = false;
                if (serviceDocumentEntity == null) {
                    isNewDoc = true;
                    // create a new document

                    // Saves the document in MongoDB
                    storageRefId = this.uploadDocument(shipmentDocumentRequest);
                    // Populate the entity now.
                    serviceDocumentEntity = this.populateServiceDocumentDtl(documentId, docTypeId, fileName, serviceRequestId, storageRefId);

                } else {
                    // replace existing document
                    storageRefId = updateDocument(shipmentDocumentRequest, serviceDocumentEntity);
                }

                try {
                    serviceDocumentEntity = serviceDocumentRepository.save(serviceDocumentEntity);

                    if (isCMSCallRequired.equals(Constants.YES_FLAG)) {
                        // System.out.println("calling CMS..");
                        if (!StringUtils.isEmpty(serviceRequestTypeMstEntity.getServiceRequestType())) {
                            if (serviceRequestTypeMstEntity.getServiceRequestType().equals(ServiceRequestType.PE_ADLD.getRequestType())
                                    || serviceRequestTypeMstEntity.getServiceRequestType().equals(ServiceRequestType.PE_EW.getRequestType())
                                    || serviceRequestTypeMstEntity.getServiceRequestType().equals(ServiceRequestType.PE_THEFT.getRequestType())) {

                                /* call CMS API to send mongoReferenceID */
                                CmsDocumentDto cmsDocumentDto = new CmsDocumentDto();
                                cmsDocumentDto.setDocKey(docTypeMstEntity.getDocName());
                                cmsDocumentDto.setDocValue(fileName);
                                oasysAdminProxy.sendDocumentDetailsToCMS(serviceRequestEntity.getExternalSRReferenceId(), storageRefId, cmsDocumentDto);

                            }
                        }
                    }
                    // if document with same docId exists then replace it, else
                    // create a new entry
                    // Insert entry in Entity_Doc_Dtl
                    if (isNewDoc && entityName != null && entityId != null) {
                        saveEntityDocumentDetails(entityName.getServiceRequestEntityDocumentName(), entityId, serviceDocumentEntity.getDocumentId());
                    }

                } catch (Exception exception) {

                    if (!StringUtils.isEmpty(storageRefId)) {
                        mongoStorageDao.deleteDocument(serviceDocumentEntity.getStorageRefId());
                    }

                    logger.error("Exception while uploading/updating document");
                    throw exception;
                }

            }
        }

        ServiceDocumentDto serviceRequestDocumentDto = modelMapper.map(serviceDocumentEntity, new TypeToken<ServiceDocumentDto>() {
        }.getType());

        response.setData(serviceRequestDocumentDto);
        response.setStatus(ResponseConstant.SUCCESS);

        response.setStatusCode(String.valueOf(ServiceRequestResponseCodes.CREATE_SERVICE_DOCUMENT_SUCCESS.getErrorCode()));
        response.setMessage(messageSource.getMessage(String.valueOf(ServiceRequestResponseCodes.CREATE_SERVICE_DOCUMENT_SUCCESS.getErrorCode()),
                new Object[] { serviceRequestDocumentDto.getServiceRequestId() }, null));

        return response;
    }

    private ServiceRequestEntityDocumentEntity saveEntityDocumentDetails(String entityName, String entityId, String documentId) throws Exception {
        ServiceRequestEntityDocumentEntity serviceRequestEntityDocumentEntity = new ServiceRequestEntityDocumentEntity();
        try {
            serviceRequestEntityDocumentEntity.setEntityId(entityId);
            serviceRequestEntityDocumentEntity.setEntityName(entityName);
            serviceRequestEntityDocumentEntity.setDocumentId(documentId);
            serviceRequestEntityDocumentEntity.setStatus(Constants.ACTIVE);
            serviceRequestEntityDocumentEntity.setCreatedOn(new Date());
            serviceRequestEntityDocumentEntity.setCreatedBy(Constants.USER_ADMIN);

            serviceRequestEntityDocumentRepository.save(serviceRequestEntityDocumentEntity);
        } catch (DataIntegrityViolationException | ConstraintViolationException ex) {
            logger.error("Exception while saving asset entity mapping:", ex);
        }
        return serviceRequestEntityDocumentEntity;
    }

    private String updateDocument(ServiceDocumentRequestDto serviceDocumentDto, ServiceDocumentEntity serviceDocumentEntity) throws Exception {

        DBObject metadata = new BasicDBObject();
        String mongoRefId = null;
        serviceDocumentDto.setFileInputStream(new ByteArrayInputStream(serviceDocumentDto.getDocumentByteArray()));

        if (serviceDocumentDto.getFileName() != null) {

            mongoRefId = mongoStorageDao.insertDocument(serviceDocumentDto.getFileInputStream(), serviceDocumentDto.getFileName(), serviceDocumentDto.getFileContentType(), metadata);

            if (!StringUtils.isEmpty(mongoRefId)) {

                if (!StringUtils.isEmpty(serviceDocumentEntity.getStorageRefId())) {
                    mongoStorageDao.deleteDocument(serviceDocumentEntity.getStorageRefId());
                }

                serviceDocumentEntity.setModifiedBy(Constants.USER_ADMIN);
                serviceDocumentEntity.setStatus(Constants.ACTIVE);
                serviceDocumentEntity.setModifiedOn(new Date());
                serviceDocumentEntity.setStorageRefId(mongoRefId);
                serviceDocumentEntity.setDocumentName(serviceDocumentDto.getFileName());

            } else {
                throw new Exception("Failed to insert new document to Mongo " + serviceDocumentDto.getFileName());
            }
        }

        return mongoRefId;
    }

    @Override
    public ResponseDto<DownloadDocumentDto> downloadServiceRequestDocument(String documentIds, String fileTypeRequired) throws Exception {
        ResponseDto<DownloadDocumentDto> response = new ResponseDto<DownloadDocumentDto>();
        DownloadDocumentDto documentResponse = null;

        if (!StringUtils.isEmpty(documentIds)) {
            String[] docArray = documentIds.split(",");
            List<String> serviceDocIds = new ArrayList<String>();

            for (String str : docArray) {
                serviceDocIds.add(str);
            }

            List<ServiceDocumentEntity> serviceDocEntities = (List<ServiceDocumentEntity>) serviceDocumentRepository.findAll(serviceDocIds);

            if (!CollectionUtils.isEmpty(serviceDocEntities)) {
                String[] mongoIds = new String[serviceDocEntities.size()];
                int docCount = 0;
                for (ServiceDocumentEntity serviceDocEntity : serviceDocEntities) {
                    mongoIds[docCount++] = serviceDocEntity.getStorageRefId();
                }

                DocumentDownloadRequestDto documentDownloadRequestDto = new DocumentDownloadRequestDto();
                documentDownloadRequestDto.setDocumentIds(mongoIds);

                documentResponse = getDownLoadDocumentDtoByFileType(documentDownloadRequestDto, fileTypeRequired);

                response.setData(documentResponse);
                response.setStatus(ResponseConstant.SUCCESS);
                response.setMessage(ResponseConstant.SUCCESS);
            } else {
                throw new Exception("Invalid document ids:" + documentIds);
            }
        }

        return response;
    }

    @Override
    public DownloadDocumentDto getDownLoadDocumentDtoByFileType(DocumentDownloadRequestDto documentDownloadRequestDto, String fileTypeRequired) throws Exception {

        DownloadDocumentDto documentResponse = null;

        if (fileTypeRequired != null && fileTypeRequired.equalsIgnoreCase(FileType.ZIPFILE.getFileTypeName())) {
            documentResponse = this.downloadZipFileDocuments(documentDownloadRequestDto);

        } else if (fileTypeRequired != null && fileTypeRequired.equalsIgnoreCase(FileType.IMAGEFILE.getFileTypeName())) {
            documentResponse = this.downloadImageFileServiceDocuments(documentDownloadRequestDto);

        } else if (fileTypeRequired != null && fileTypeRequired.equalsIgnoreCase(FileType.BYTEARRAYFILE.getFileTypeName())) {
            documentResponse = this.downloadByteArrayServiceDocuments(documentDownloadRequestDto);

        } else if (fileTypeRequired != null && fileTypeRequired.equalsIgnoreCase(FileType.THUMBNAILIMAGEFILE.getFileTypeName())) {
            documentResponse = this.downloadThumbnailFileServiceDocuments(documentDownloadRequestDto);

        }

        return documentResponse;
    }

    private DownloadDocumentDto downloadByteArrayServiceDocuments(DocumentDownloadRequestDto documentDownloadRequestDto) throws IOException {
        DownloadDocumentDto documentDto = new DownloadDocumentDto();
        List<GridFSDBFile> documents = mongoStorageDao.getDocuments(documentDownloadRequestDto.getDocumentIds());

        if (!CollectionUtils.isEmpty(documents)) {
            for (GridFSDBFile document : documents) {
                byte[] buffer = IOUtils.toByteArray(document.getInputStream());
                byte[] cipherByteArray = org.apache.commons.codec.binary.Base64.encodeBase64(buffer);

                documentDto.setFileName(document.getFilename());
                documentDto.setInputStream(new String(cipherByteArray, "UTF-8"));
            }
        } else {
            placeDownloadRequestFromArchive(documentDownloadRequestDto.getDocumentIds());
        }

        return documentDto;
    }

    private void placeDownloadRequestFromArchive(String[] documentIds) {
        for (String documentId : documentIds) {
            ServiceDocumentEntity serviceDocumentEntity = serviceDocumentRepository.findOne(documentId);
            serviceRequestDocumentArchivalService.downloadArchiveDocuments(serviceDocumentEntity.getServiceRequestId());
        }

    }

    private DownloadDocumentDto downloadImageFileServiceDocuments(DocumentDownloadRequestDto documentDownloadRequestDto) throws IOException {
        DownloadDocumentDto documentDto = new DownloadDocumentDto();
        List<GridFSDBFile> documents = mongoStorageDao.getDocuments(documentDownloadRequestDto.getDocumentIds());
        if (!CollectionUtils.isEmpty(documents)) {
            for (GridFSDBFile document : documents) {
                byte[] buffer = IOUtils.toByteArray(document.getInputStream());

                documentDto.setFileName(document.getFilename());
                documentDto.setInputStream(new String(buffer, "UTF-8"));
            }

        } else {
            placeDownloadRequestFromArchive(documentDownloadRequestDto.getDocumentIds());
        }

        return documentDto;
    }

    private DownloadDocumentDto downloadThumbnailFileServiceDocuments(DocumentDownloadRequestDto documentDownloadRequestDto) throws IOException {
        DownloadDocumentDto documentDto = new DownloadDocumentDto();
        List<GridFSDBFile> documents = mongoStorageDao.getDocuments(documentDownloadRequestDto.getDocumentIds());

        if (!CollectionUtils.isEmpty(documents)) {
            for (GridFSDBFile document : documents) {
                ByteArrayOutputStream bOutput = new ByteArrayOutputStream();

                Thumbnails.of(document.getInputStream()).crop(Positions.CENTER).size(160, 160).toOutputStream(bOutput);

                byte[] cipherByteArray = org.apache.commons.codec.binary.Base64.encodeBase64(bOutput.toByteArray());

                documentDto.setFileName(document.getFilename());
                documentDto.setInputStream(new String(cipherByteArray, "UTF-8"));
            }
        } else {
            placeDownloadRequestFromArchive(documentDownloadRequestDto.getDocumentIds());
        }

        return documentDto;
    }

    private DownloadDocumentDto downloadZipFileDocuments(DocumentDownloadRequestDto documentDownloadRequestDto) throws IOException {
        DownloadDocumentDto documentDto = new DownloadDocumentDto();
        List<GridFSDBFile> documents = mongoStorageDao.getDocuments(documentDownloadRequestDto.getDocumentIds());
        if (!CollectionUtils.isEmpty(documents)) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            int count = 0;
            String docName = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);

            for (GridFSDBFile gridFSDBFile : documents) {
                if (map.containsKey(gridFSDBFile.getFilename())) {
                    int value = map.get(gridFSDBFile.getFilename());
                    map.put(gridFSDBFile.getFilename(), ++value);
                    String filename = FilenameUtils.removeExtension(gridFSDBFile.getFilename());
                    String extension = gridFSDBFile.getFilename().substring(gridFSDBFile.getFilename().lastIndexOf(".") + 1);
                    docName = filename + "(" + (map.get(gridFSDBFile.getFilename())) + ")." + extension;
                } else {
                    map.put(gridFSDBFile.getFilename(), count);
                    docName = gridFSDBFile.getFilename();
                }

                ZipEntry zipEntry = new ZipEntry(docName);
                zos.putNextEntry(zipEntry);
                gridFSDBFile.writeTo(zos);
                zos.flush();
            }

            zos.close();
            byte[] cipherByteArray = org.apache.commons.codec.binary.Base64.encodeBase64(baos.toByteArray());
            documentDto.setInputStream(new String(cipherByteArray, "UTF-8"));
        } else {
            placeDownloadRequestFromArchive(documentDownloadRequestDto.getDocumentIds());
        }

        return documentDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseDto<ServiceDocumentDto> updateMultipleServiceDocuments(List<BodyPart> bodyParts, String docId) throws IOException, ParseException {
        byte[] byteData;
        ResponseDto<ServiceDocumentDto> response = new ResponseDto<ServiceDocumentDto>();
        ServiceDocumentEntity serviceRequestDocumentEntity = serviceDocumentRepository.findOne(docId);
        ServiceDocumentEntity serviceDocumentEntity = null;

        if (serviceRequestDocumentEntity != null) {
            serviceRequestDocumentEntity.setStatus(Constants.EXPIRED);
            serviceRequestDocumentEntity.setModifiedBy(Constants.USER_ADMIN);
            serviceRequestDocumentEntity.setModifiedOn(new Date());
            serviceDocumentRepository.save(serviceRequestDocumentEntity);

            for (int i = 0; i < bodyParts.size(); i++) {
                BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
                logger.info(bodyPartEntity);
                String fileName = bodyParts.get(i).getContentDisposition().getFileName();
                logger.info(fileName);
                InputStream fileInputStream = bodyPartEntity.getInputStream();
                String mimeType = bodyParts.get(i).getMediaType().toString();
                byteData = IOUtils.toByteArray(fileInputStream);
                ServiceRequestDocumentDto serviceDocumentDto = new ServiceRequestDocumentDto();
                serviceDocumentDto.setFileName(fileName);
                serviceDocumentDto.setDocumentByteArray(byteData);
                serviceDocumentDto.setFileContentType(mimeType);
                serviceDocumentEntity = serviceDocumentRepository.save(serviceDocumentEntity);
            }

            ServiceDocumentDto serviceRequestDocumentDto = modelMapper.map(serviceDocumentEntity, new TypeToken<ServiceDocumentDto>() {
            }.getType());

            response.setData(serviceRequestDocumentDto);
            response.setStatus(ResponseConstant.SUCCESS);
            response.setStatusCode(String.valueOf(ServiceRequestResponseCodes.CREATE_SERVICE_DOCUMENT_SUCCESS.getErrorCode()));
            response.setMessage(messageSource.getMessage(String.valueOf(ServiceRequestResponseCodes.CREATE_SERVICE_DOCUMENT_SUCCESS.getErrorCode()), new Object[] {}, null));
        } else {
            response.setStatus(ResponseConstant.FAILED);
            response.setStatusCode(String.valueOf(ServiceRequestResponseCodes.UPDATE_SERVICE_DOCUMENT_FAILED.getErrorCode()));
            response.setMessage(messageSource.getMessage(String.valueOf(ServiceRequestResponseCodes.UPDATE_SERVICE_DOCUMENT_FAILED.getErrorCode()), new Object[] {}, null));
        }

        return response;
    }

    @Override
    public ResponseDto<ServiceDocumentDto> deleteServiceRequestDocument(String documentIds) throws ParseException {

        ResponseDto<ServiceDocumentDto> response = new ResponseDto<>();

        String docArray[] = documentIds.split(",");
        Iterable<ServiceDocumentEntity> serviceRequestDocumentEntities = serviceDocumentRepository.findAll(Arrays.asList(docArray));

        for (ServiceDocumentEntity serviceDocumentEntity : serviceRequestDocumentEntities) {
            serviceDocumentEntity.setStatus(Constants.EXPIRED);
            serviceDocumentEntity.setModifiedBy(Constants.USER_ADMIN);
            serviceDocumentEntity.setModifiedOn(new Date());

            serviceDocumentRepository.save(serviceDocumentEntity);
        }

        List<ServiceRequestEntityDocumentEntity> listOfDocuments = serviceRequestEntityDocumentRepository.findByDocumentIdIn(Arrays.asList(docArray));

        if (listOfDocuments != null && !listOfDocuments.isEmpty()) {
            for (ServiceRequestEntityDocumentEntity serviceRequestEntityDocumentEntity : listOfDocuments) {
                serviceRequestEntityDocumentEntity.setStatus(Constants.EXPIRED);
                serviceRequestEntityDocumentEntity.setModifiedBy(Constants.USER_ADMIN);
                serviceRequestEntityDocumentEntity.setModifiedOn(new Date());
                serviceRequestEntityDocumentRepository.save(serviceRequestEntityDocumentEntity);
            }
        }

        response.setMessage("Documents deleted successfully");
        response.setStatus(ResponseConstant.SUCCESS);

        return response;
    }

    @Override
    public DownloadDocumentDto downloadImageFileDocument(DocumentDownloadRequestDto documentDownloadRequestDto) throws Exception {
        return downloadImageFileServiceDocuments(documentDownloadRequestDto);
    }

    private String uploadDocument(ServiceDocumentRequestDto serviceDocumentDto) throws Exception {
        Map<String, String> docUploadResponse = uploadServiceDocument(serviceDocumentDto);
        String storageRefId = docUploadResponse.get("storageRefId");
        return storageRefId;
    }

    private ServiceDocumentEntity populateServiceDocumentDtl(String documentId, String docTypeId, String fileName, Long serviceRequestId, String storageRefId) {
        ServiceDocumentEntity serviceRequestDocumentEntity = new ServiceDocumentEntity();

        serviceRequestDocumentEntity.setDocumentId(StringUtils.isEmpty(documentId) ? UUID.randomUUID().toString().replaceAll("-", "") : documentId);
        serviceRequestDocumentEntity.setDocumentTypeId(Long.valueOf(docTypeId));
        serviceRequestDocumentEntity.setDocumentName(fileName);
        serviceRequestDocumentEntity.setServiceRequestId(serviceRequestId);
        serviceRequestDocumentEntity.setStorageRefId(storageRefId);
        serviceRequestDocumentEntity.setStatus(Constants.ACTIVE);
        serviceRequestDocumentEntity.setCreatedBy(Constants.USER_ADMIN);
        serviceRequestDocumentEntity.setCreatedOn(new Date());
        serviceRequestDocumentEntity.setModifiedBy(Constants.USER_ADMIN);
        serviceRequestDocumentEntity.setModifiedOn(new Date());

        return serviceRequestDocumentEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseDto<ServiceRequestReportResponseDto> generateServiceRequestReport(ServiceRequestReportRequestDto serviceRequestReportDto) throws Exception {
        ResponseDto<ServiceRequestReportResponseDto> response = new ResponseDto<ServiceRequestReportResponseDto>();
        BaseFileGenerator baseFileGenerator = serviceRequestReportFactory.getServiceRequestReportHandler(ServiceRequestReport.getServiceRequestReport(serviceRequestReportDto.getReportType()));
        ServiceRequestReportResponseDto serviceRequestReportResponseDto = baseFileGenerator.generateFile(serviceRequestReportDto);

        response.setData(serviceRequestReportResponseDto);
        response.setStatus(ResponseConstant.SUCCESS);
        response.setMessage(ResponseConstant.SUCCESS);
        return response;
    }

    private Map<String, String> uploadServiceDocument(ServiceDocumentRequestDto serviceDocumentDto) throws Exception {
        Map<String, String> values = new HashMap<String, String>();

        DBObject metadata = new BasicDBObject();
        serviceDocumentDto.setFileInputStream(new ByteArrayInputStream(serviceDocumentDto.getDocumentByteArray()));

        if (serviceDocumentDto.getFileName() != null) {

            String docMongoRefId = mongoStorageDao.insertDocument(serviceDocumentDto.getFileInputStream(), serviceDocumentDto.getFileName(), serviceDocumentDto.getFileContentType(), metadata);
            values.put("storageRefId", docMongoRefId);
        } else {
            throw new Exception("Upload document file name is empty for ::" + serviceDocumentDto.getServiceRequestId());
        }

        return values;
    }

    @Override
    public void uploadMultipleServiceDocuments(Long serviceRequestId, File file, Long docTypeId, String contentType) throws Exception {
        ServiceDocumentEntity serviceDocumentEntity = null;
        InputStream fileInputStream = new FileInputStream(file);
        byte[] byteData = IOUtils.toByteArray(fileInputStream);

        String fileName = file.getName();
        ServiceDocumentRequestDto shipmentDocumentRequest = new ServiceDocumentRequestDto();
        shipmentDocumentRequest.setServiceRequestId(String.valueOf(serviceRequestId));
        shipmentDocumentRequest.setFileContentType(contentType);
        shipmentDocumentRequest.setFileName(fileName);
        shipmentDocumentRequest.setDocumentByteArray(byteData);

        String storageRefId = uploadDocument(shipmentDocumentRequest);
        if (!StringUtils.isEmpty(storageRefId)) {
            serviceDocumentEntity = populateServiceDocumentDtl(null, String.valueOf(docTypeId), fileName, serviceRequestId, storageRefId);
            serviceDocumentEntity = serviceDocumentRepository.save(serviceDocumentEntity);
        } else {
            throw new Exception("Failed to insert new document to Mongo " + fileName);
        }
    }

}
