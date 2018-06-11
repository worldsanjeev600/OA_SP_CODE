package com.oneassist.serviceplatform.commons.filegenerator;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.HeightRule;
import com.aspose.words.ParagraphAlignment;
import com.aspose.words.Table;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.mongodb.gridfs.GridFSDBFile;
import com.oneassist.serviceplatform.commons.cache.PinCodeMasterCache;
import com.oneassist.serviceplatform.commons.cache.ProductMasterCache;
import com.oneassist.serviceplatform.commons.cache.base.CacheFactory;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.custom.CustomRepository;
import com.oneassist.serviceplatform.commons.entities.DocTypeMstEntity;
import com.oneassist.serviceplatform.commons.entities.GenericKeySetEntity;
import com.oneassist.serviceplatform.commons.entities.GenericKeySetValueEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceAddressEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceDocumentEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.ServiceDocumentType;
import com.oneassist.serviceplatform.commons.mastercache.GenericKeySetCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceTaskMasterCache;
import com.oneassist.serviceplatform.commons.mongo.repositories.IMongoStorageDao;
import com.oneassist.serviceplatform.commons.proxies.OasysAdminProxy;
import com.oneassist.serviceplatform.commons.proxies.OasysProxy;
import com.oneassist.serviceplatform.commons.repositories.ServiceAddressRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceDocumentRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.utils.DateUtils;
import com.oneassist.serviceplatform.commons.utils.DecimalFormatter;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.utils.StringUtils;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Diagnosis;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Issues;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestReportRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestReportResponseDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceTaskDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.SpareParts;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceAddressDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceDocumentDto;
import com.oneassist.serviceplatform.contracts.response.UserProfileData;
import com.oneassist.serviceplatform.externalcontracts.HomeApplianceDetailDTO;
import com.oneassist.serviceplatform.externalcontracts.MembershipDetailsDTO;
import com.oneassist.serviceplatform.externalcontracts.PartnerBusinessUnit;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;
import com.oneassist.serviceplatform.externalcontracts.ProductMasterDto;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author Alok Singh
 */
@Component
public class JobsheetFileGenerator extends BaseFileGenerator {

    private final Logger logger = Logger.getLogger(JobsheetFileGenerator.class);

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private GenericKeySetCache genericKeySetCache;

    @Autowired
    private ServiceAddressRepository serviceAddressRepository;

    @Autowired
    private ServiceDocumentRepository serviceDocumentRepository;

    @Autowired
    private ServiceTaskMasterCache serviceTaskMasterCache;

    @Autowired
    private CustomRepository customRepositoryImpl;

    @Autowired
    private ProductMasterCache productMasterCache;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private OasysProxy oasysProxy;

    @Autowired
    private OasysAdminProxy oasysAdminProxy;

    @Autowired
    private ServiceRequestHelper serviceRequestHelper;

    @Autowired
    private IMongoStorageDao mongoStorageDao;

    @Autowired
    private CacheFactory cacheFactory;

    @Autowired
    private PinCodeMasterCache pinCodeMasterCache;

    private final String SPARES_DESCRIPTION = "Spares Description";
    private final String QUANTITY = "Quantity";
    private final String UNIT_PRICE = "Unit Price";
    private final String AMOUNT = "Amount";
    private final String JOBSHEET = "JOBSHEET";
    private final String ACTIVE = "A";
    private final String YES_FLAG = "Yes";
    private final String NO_FLAG = "No";
    private final String Y_FLAG = "Y";
    private final String TAXRATE_IN_PERCENTAGE = "TAXRateInPercentage";
    private static final String LONG_DATE = "dd-MM-yyyy HH:mm:ss";

    @Override
    public ServiceRequestReportResponseDto generateFile(ServiceRequestReportRequestDto serviceRequestReportDto) throws Exception {

        ServiceRequestEntity serviceRequestEntity = serviceRequestRepository.findServiceRequestEntityByServiceRequestId(serviceRequestReportDto.getServiceRequestId());
        ServiceResponseDto serviceResponseDto = serviceRequestHelper.convertObject(serviceRequestEntity, ServiceResponseDto.class);

        ServiceAddressEntity serviceAddressEntity = serviceAddressRepository.findByServiceAddressId(Long.valueOf(serviceResponseDto.getWorkflowData().getVisit().getServiceAddress()));
        ServiceAddressDetailDto serviceAddressDetailDto = modelMapper.map(serviceAddressEntity, ServiceAddressDetailDto.class);
        serviceResponseDto.setServiceAddress(serviceAddressDetailDto);

        // To get all images for Service Request
        List<ServiceDocumentEntity> serviceDocumentEntities = serviceDocumentRepository.findByServiceRequestIdAndStatus(serviceRequestReportDto.getServiceRequestId(), Constants.ACTIVE);
        List<ServiceDocumentDto> serviceDocumentDtos = null;

        if (serviceDocumentEntities != null && !serviceDocumentEntities.isEmpty()) {
            serviceDocumentDtos = modelMapper.map(serviceDocumentEntities, new TypeToken<List<ServiceDocumentDto>>() {
            }.getType());
        }

        serviceResponseDto.setServiceDocuments(serviceDocumentDtos);
        populateData(serviceResponseDto, serviceRequestReportDto);

        GenericKeySetEntity genericKeySet = genericKeySetCache.get("SFTP_LOCATION");
        String templateFilePath = null;
        if (genericKeySet != null) {
            List<GenericKeySetValueEntity> genericKeySetValues = genericKeySet.getGenericKeySetValueDetails();
            if (genericKeySetValues != null & !genericKeySetValues.isEmpty()) {
                for (GenericKeySetValueEntity genericKeySetValueEntity : genericKeySetValues) {
                    if (genericKeySetValueEntity.getKey().equals(JOBSHEET)) {
                        templateFilePath = genericKeySetValueEntity.getValue();
                    }
                }
            }
        }
        serviceRequestReportDto.setTemplateFilePath(templateFilePath);
        Document doc = doFileGeneration(serviceRequestReportDto);

        // Adding dynamic content in document
        Document document = addDynamicContent(doc, serviceResponseDto, serviceRequestReportDto);
        ServiceRequestReportResponseDto serviceRequestReportResponseDto = generatePdf(document);

        return serviceRequestReportResponseDto;
    }

    private Document addDynamicContent(Document doc, ServiceResponseDto serviceResponseDto, ServiceRequestReportRequestDto serviceRequestReportDto) throws Exception {
        // NOTE:: Template Document is having a table namely "rootTable". In this by moving to a specific row/ column, we should add dynamic content as per the need.
        DocumentBuilder builder = new DocumentBuilder(doc);
        List<Issues> customerIssues = serviceResponseDto.getWorkflowData().getVisit().getIssueReportedByCustomer();
        if (customerIssues != null) {
            builder.moveToCell(0, 17, 0, 0);// Moving the cursor to the row#18;column#1 of the "rootTable" to print customer issue
            Table table = builder.startTable();
            table.setBorder(0, 0, 0, null, false);
            for (Issues issue : customerIssues) {
                ServiceTaskDto serviceTaskDto = serviceTaskMasterCache.get(issue.getIssueId());
                builder.insertCell();
                builder.write(serviceTaskDto.getTaskName());
                builder.endRow();
            }
            builder.endTable();
        }

        // populate Technician Diagonosis
        List<Diagnosis> technicianDiagnosis = serviceResponseDto.getWorkflowData().getRepairAssessment().getDiagonosisReportedbyAssignee();
        if (technicianDiagnosis != null) {
            builder.moveToCell(0, 17, 1, 0); // Moving the cursor to the row#18;column#2 of the "rootTable" to print technician diagnosis
            Table table = builder.startTable();
            table.setBorder(0, 0, 0, null, false);
            for (Diagnosis diagnosis : technicianDiagnosis) {
                ServiceTaskDto serviceTaskDto = serviceTaskMasterCache.get(diagnosis.getDiagnosisId());
                builder.insertCell();
                builder.write(serviceTaskDto.getTaskName());
                builder.endRow();
            }

            builder.endTable();
        }

        // Populate Images 1. Pre-repair; 2. Post-repair; 3. Spare parts images
        List<ServiceDocumentDto> serviceDocumentDtos = serviceResponseDto.getServiceDocuments();
        if (serviceDocumentDtos != null) {
            List<DocTypeMstEntity> docTypeMasterList = customRepositoryImpl.findAllByDocType();
            long preRepairDocTypeId = 0, postRepairDocTypeId = 0, sparePartDocTypeId = 0;
            for (DocTypeMstEntity docTypeMstEntity : docTypeMasterList) {
                if (docTypeMstEntity.getDocName().equals(ServiceDocumentType.DAMAGED_SPARE_PART.getServiceDocumentType())) {
                    sparePartDocTypeId = docTypeMstEntity.getDocTypeId().longValue();
                } else if (docTypeMstEntity.getDocName().equals(ServiceDocumentType.PRE_REPAIR_IMAGE.getServiceDocumentType())) {
                    preRepairDocTypeId = docTypeMstEntity.getDocTypeId().longValue();
                } else if (docTypeMstEntity.getDocName().equals(ServiceDocumentType.POST_REPAIR_IMAGE.getServiceDocumentType())) {
                    postRepairDocTypeId = docTypeMstEntity.getDocTypeId().longValue();
                }
            }
            String preImageMongoIds = "", postImageMongoIds = "", sparePartImageMongoIds = "";
            for (ServiceDocumentDto serviceDocumentDto : serviceDocumentDtos) {
                if (serviceDocumentDto.getDocumentTypeId().longValue() == preRepairDocTypeId) {
                    preImageMongoIds = preImageMongoIds + serviceDocumentDto.getStorageRefId() + ",";
                } else if (serviceDocumentDto.getDocumentTypeId().longValue() == postRepairDocTypeId) {
                    postImageMongoIds = postImageMongoIds + serviceDocumentDto.getStorageRefId() + ",";
                } else if (serviceDocumentDto.getDocumentTypeId().longValue() == sparePartDocTypeId) {
                    sparePartImageMongoIds = sparePartImageMongoIds + serviceDocumentDto.getStorageRefId() + ",";
                }
            }
            String[] preImagesArray = null;
            if (!Strings.isNullOrEmpty(preImageMongoIds)) {
                preImageMongoIds = preImageMongoIds.substring(0, preImageMongoIds.lastIndexOf(","));
                preImagesArray = preImageMongoIds.split(",");
            }
            if (preImagesArray != null) {
                // Moving the cursor to the row#23 of the "rootTable" to print pre-repair images
                renderImages(builder, preImagesArray, 22);
            }

            String[] postImagesArray = null;
            if (!Strings.isNullOrEmpty(postImageMongoIds)) {
                postImageMongoIds = postImageMongoIds.substring(0, postImageMongoIds.lastIndexOf(","));
                postImagesArray = postImageMongoIds.split(",");
            }
            if (postImagesArray != null) {
                renderImages(builder, postImagesArray, 24);
            }

            // Download spare parts images..
            String[] sparePartImagesArray = null;
            if (!Strings.isNullOrEmpty(sparePartImageMongoIds)) {
                sparePartImageMongoIds = sparePartImageMongoIds.substring(0, sparePartImageMongoIds.lastIndexOf(","));
                sparePartImagesArray = sparePartImageMongoIds.split(",");
            }
            if (sparePartImagesArray != null) {
                renderImages(builder, sparePartImagesArray, 26);
            }
        }
        Document document = insertSpareParts(doc, serviceResponseDto, serviceRequestReportDto);
        return document;
    }

    private void renderImages(DocumentBuilder builder, String[] imageIds, int rowIndex) {
        try {
            List<GridFSDBFile> documents = mongoStorageDao.getDocuments(imageIds);
            if (!CollectionUtils.isEmpty(documents)) {
                builder.moveToCell(0, rowIndex, 0, 0);
                Table table = builder.startTable();
                table.setBorder(0, 0, 0, null, false);
                for (GridFSDBFile document : documents) {
                    try {
                        String extension = Files.getFileExtension(document.getFilename());
                        BufferedImage bImageFromConvert = ImageIO.read(document.getInputStream());
                        bImageFromConvert = resizeImage(bImageFromConvert);
                        ByteArrayOutputStream baosImage = new ByteArrayOutputStream();
                        ImageIO.write(bImageFromConvert, extension, baosImage);
                        baosImage.flush();
                        byte[] imageInByte = baosImage.toByteArray();
                        baosImage.close();
                        builder.insertCell();
                        builder.getCellFormat().setWidth(85.0);
                        builder.insertImage(imageInByte);
                    } catch (Exception e) {
                        logger.error("Exception while rendering image " + document.getFilename(), e);
                    }
                }
                builder.endRow();
                builder.endTable();
            }
        } catch (Exception e) {
            logger.error("Exception while getting post repair image", e);
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage) {
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        BufferedImage resizedImage = new BufferedImage(100, 100, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, 100, 100, null);// To draw 100px*100px image
        g.dispose();
        return resizedImage;
    }

    @SuppressWarnings("deprecation")
    private Document insertSpareParts(Document pdfdoc, ServiceResponseDto serviceResponseDto, ServiceRequestReportRequestDto serviceRequestReportDto) throws Exception {
        logger.info("JobsheetFileGenerator : insertSpareParts() called..");
        DocumentBuilder builder = new DocumentBuilder(pdfdoc);
        List<SpareParts> sparePartsList = serviceResponseDto.getWorkflowData().getRepairAssessment().getSparePartsRequired();
        if (sparePartsList != null) {
            builder.moveToCell(0, 27, 0, 0);// Moving the cursor to the row#28 of the "rootTable" to print customer issue
            builder.getFont().setBold(true);
            Table outerTable = builder.startTable();// Table to display spare parts; table width is 1900px which will splitted into 4 columns
            builder.insertCell();
            outerTable.setAllowAutoFit(true);
            builder.getCellFormat().setWidth(1000.0);// setting width
            builder.getRowFormat().setHeight(13.0);
            builder.getRowFormat().setHeightRule(HeightRule.EXACTLY);
            builder.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
            builder.getFont().setSize(11);
            builder.getFont().setName("Calibri");
            builder.write(SPARES_DESCRIPTION);

            builder.insertCell();
            builder.getCellFormat().setWidth(200.0);// setting width
            builder.write(QUANTITY);

            builder.insertCell();
            builder.getCellFormat().setWidth(200.0);// setting width
            builder.write(UNIT_PRICE);

            builder.insertCell();
            builder.getCellFormat().setWidth(500.0);// setting width
            builder.write(AMOUNT);
            builder.endRow();

            builder.getRowFormat().setAllowBreakAcrossPages(true);
            ServiceTaskDto serviceTaskDto = null;
            for (SpareParts sparePart : sparePartsList) {
                builder.insertCell();
                builder.getCellFormat().setWidth(1000.0);
                builder.getRowFormat().setLeftPadding(8.0);
                builder.getParagraphFormat().setAlignment(ParagraphAlignment.LEFT);
                builder.getFont().setBold(false);

                serviceTaskDto = serviceTaskMasterCache.get(sparePart.getSparePartId());
                builder.write(serviceTaskDto.getTaskName());

                builder.insertCell();
                builder.getCellFormat().setWidth(200.0);
                builder.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
                builder.write("1");

                builder.insertCell();
                builder.getCellFormat().setWidth(200.0);
                builder.write(DecimalFormatter.getEmptyDecimalIfNull(sparePart.getCost()));

                builder.insertCell();
                builder.getCellFormat().setWidth(500.0);
                builder.write(DecimalFormatter.getEmptyDecimalIfNull(sparePart.getCost()));
                builder.endRow();
            }
            builder.endTable();
        }
        return builder.getDocument();
    }

    private void populateData(ServiceResponseDto serviceResponseDto, ServiceRequestReportRequestDto serviceRequestReportDto) throws ParseException {

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("$city", "");
        data.put("$state", "");
        data.put("$productName", "");
        data.put("$brand", "");
        data.put("$model", "");
        data.put("$serialNo", "");
        data.put("$purchaseDate", "");

        data.put("$serviceRequestId", serviceResponseDto.getRefPrimaryTrackingNo());
        data.put("$srCreateDatetime", serviceResponseDto.getCreatedOn() == null ? "" : DateUtils.toLongFormattedString(serviceResponseDto.getCreatedOn()));
        data.put("$datetimeOfIncident", StringUtils.getEmptyIfNull(serviceResponseDto.getWorkflowData().getVisit().getDateOfIncident()));

        Date dateTimeOfIncident = DateUtils.fromLongFormattedString(serviceResponseDto.getWorkflowData().getVisit().getDateOfIncident());
        String dateOfIncident = DateUtils.toShortFormattedString(dateTimeOfIncident);
        data.put("$dateOfIncident", StringUtils.getEmptyIfNull(dateOfIncident));

        GenericKeySetEntity genericKeySet = genericKeySetCache.get("SERVICE_REQUEST_STATUS");
        String status = null;
        if (genericKeySet != null) {
            List<GenericKeySetValueEntity> genericKeySetValues = genericKeySet.getGenericKeySetValueDetails();
            if (genericKeySetValues != null & !genericKeySetValues.isEmpty()) {
                for (GenericKeySetValueEntity genericKeySetValueEntity : genericKeySetValues) {
                    if (genericKeySetValueEntity.getKey().equals(serviceResponseDto.getStatus())) {
                        status = genericKeySetValueEntity.getValue();
                        break;
                    }
                }
            }
        }
        data.put("$status", StringUtils.getEmptyIfNull(status));

        data.put("$jobCompletedDatetime", serviceResponseDto.getActualEndDateTime() == null ? "" : DateUtils.toLongFormattedString(serviceResponseDto.getActualEndDateTime()));

        String subStatus = getJobSubStatus(serviceResponseDto.getWorkflowStageStatus());
        data.put("$substatus", StringUtils.getEmptyIfNull(subStatus));

        data.put("$membershipId", serviceResponseDto.getReferenceNo());

        UserProfileData userProfileData = oasysAdminProxy.getTechnicianUserProfile(String.valueOf(serviceResponseDto.getAssignee()));

        String fullName = "";

        if (userProfileData != null) {
            fullName = StringUtils.concatenate(new String[] { userProfileData.getFirstName(), userProfileData.getMiddleName(), userProfileData.getLastName() }, " ");
        }

        data.put("$assignee", fullName);

        // Customer's address details
        ServiceAddressDetailDto serviceAddressDetailDto = serviceResponseDto.getServiceAddress();
        data.put("$customerName", StringUtils.getEmptyIfNull(serviceAddressDetailDto.getAddresseeFullName()));
        data.put("$addressLine1", StringUtils.getEmptyIfNull(serviceAddressDetailDto.getAddressLine1()));
        data.put("$addressLine2", StringUtils.getEmptyIfNull(serviceAddressDetailDto.getAddressLine2()));
        data.put("$addressLine3", StringUtils.getEmptyIfNull(serviceAddressDetailDto.getLandmark()));
        data.put("$pincode", StringUtils.getEmptyIfNull(serviceAddressDetailDto.getPincode()));

        // Call to get State and City Name
        try {
            if (serviceAddressDetailDto.getPincode() != null) {
                PincodeMasterDto stateCityResponse = pinCodeMasterCache.get(serviceAddressDetailDto.getPincode());
                if (stateCityResponse != null) {
                    data.put("$city", StringUtils.getEmptyIfNull(stateCityResponse.getCityName()));
                    data.put("$state", StringUtils.getEmptyIfNull(stateCityResponse.getStateName()));
                }
            }
        } catch (Exception e) {
            logger.warn("Not getting state and city details, so by passing the details with error: ", e);
        }

        data.put("$phone", serviceAddressDetailDto.getMobileNo() == null ? "" : String.valueOf(serviceAddressDetailDto.getMobileNo()));
        data.put("$email", StringUtils.getEmptyIfNull(serviceAddressDetailDto.getEmail()));

        try {
            MembershipDetailsDTO membershipDetails = oasysProxy.getCustomerHomeApplianceAssetDetail(serviceResponseDto.getReferenceNo(), serviceResponseDto.getRefSecondaryTrackingNo());
            if (membershipDetails != null) {
                List<HomeApplianceDetailDTO> assetDetails = membershipDetails.getAssetDetails();
                if (assetDetails != null) {
                    HomeApplianceDetailDTO assetDetail = assetDetails.get(0);
                    if (assetDetail != null) {
                        if (assetDetail.getProductCode() != null) {
                            ProductMasterDto productMasterDto = productMasterCache.get(assetDetail.getProductCode());
                            if (productMasterDto != null) {
                                String productName = productMasterDto.getProductName();
                                data.put("$productName", productName);
                            }
                        }

                        data.put("$brand", StringUtils.getEmptyIfNull(assetDetail.getMake()));
                        data.put("$model", StringUtils.getEmptyIfNull(assetDetail.getModel()));
                        data.put("$serialNo", StringUtils.getEmptyIfNull(assetDetail.getDeviceId()));
                        Date invoiceDate = DateUtils.fromString(assetDetail.getInvoiceDate(), LONG_DATE);
                        data.put("$purchaseDate", assetDetail.getInvoiceDate() == null ? "" : DateUtils.toShortFormattedString(invoiceDate));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Not getting product information, so by passing the details with error: ", e);
        }

        String isPhysicalDamage = Y_FLAG.equals(serviceResponseDto.getWorkflowData().getRepairAssessment().getAccidentalDamage()) ? YES_FLAG : NO_FLAG;
        data.put("$isPhysicalDamage", isPhysicalDamage);
        String costToCompany = serviceResponseDto.getWorkflowData().getRepairAssessment().getCostToCompany() == null ? "0.0" : serviceResponseDto.getWorkflowData().getRepairAssessment()
                .getCostToCompany();
        Double dCostToCompany = Double.parseDouble(costToCompany);
        data.put("$costtocompany", DecimalFormatter.getFormattedValue(dCostToCompany));

        String taxRatePercentage = messageSource.getMessage(TAXRATE_IN_PERCENTAGE, new Object[] { "" }, null);
        Double dTaxOnCostToCompany = dCostToCompany * Double.valueOf(taxRatePercentage) / 100;
        data.put("$taxoncosttocompany", DecimalFormatter.getFormattedValue(dTaxOnCostToCompany));
        data.put("$totalcosttocompany", DecimalFormatter.getFormattedValue(dCostToCompany + dTaxOnCostToCompany));

        String costToCustomer = serviceResponseDto.getWorkflowData().getRepairAssessment().getCostToCustomer() == null ? "0.0" : serviceResponseDto.getWorkflowData().getRepairAssessment()
                .getCostToCustomer();
        Double dCostToCustomer = Double.parseDouble(costToCustomer);
        data.put("$costtocustomer", DecimalFormatter.getFormattedValue(dCostToCustomer));
        Double dTaxOnCostToCustomer = dCostToCustomer * Double.valueOf(taxRatePercentage) / 100;
        data.put("$taxoncosttocustomer", DecimalFormatter.getFormattedValue(dTaxOnCostToCustomer));
        data.put("$totalcosttocustomer", DecimalFormatter.getFormattedValue(dCostToCustomer + dTaxOnCostToCustomer));

        String labourCost = serviceResponseDto.getWorkflowData().getRepairAssessment().getLabourCost() == null
                || serviceResponseDto.getWorkflowData().getRepairAssessment().getLabourCost().getCost() == null ? "0.0" : serviceResponseDto.getWorkflowData().getRepairAssessment().getLabourCost()
                .getCost();
        Double dLabourCost = Double.parseDouble(labourCost);

        data.put("$labourCharges", DecimalFormatter.getFormattedValue(dLabourCost));

        String transportCost = serviceResponseDto.getWorkflowData().getRepairAssessment().getTransport() == null
                || serviceResponseDto.getWorkflowData().getRepairAssessment().getTransport().getCost() == null ? "0.0" : serviceResponseDto.getWorkflowData().getRepairAssessment().getTransport()
                .getCost();
        Double dTransportCost = Double.parseDouble(transportCost);
        data.put("$transportationCharges", DecimalFormatter.getFormattedValue(dTransportCost));
        if (serviceResponseDto.getServicePartnerBuCode() != null && serviceResponseDto.getServicePartnerBuCode() != 0) {
            Map<String, PartnerBusinessUnit> partnerBUCache = cacheFactory.get(Constants.PARTNER_BU_CACHE).getAll();
            if (!CollectionUtils.isEmpty(partnerBUCache)) {
                PartnerBusinessUnit partnerBu = partnerBUCache.get(String.valueOf(serviceResponseDto.getServicePartnerBuCode()));
                if (partnerBu != null) {
                    data.put("$partnerName", partnerBu.getBusinessUnitName());
                }
            }
        }

        serviceRequestReportDto.setData(data);
    }

    private String getJobSubStatus(String statusCode) {
        if (statusCode != null) {
            GenericKeySetEntity genericKeySetEntity = genericKeySetCache.get("WORKFLOW_STAGE_STATUS");
            if (genericKeySetEntity != null) {
                List<GenericKeySetValueEntity> valueEntities = genericKeySetEntity.getGenericKeySetValueDetails();

                if (!CollectionUtils.isEmpty(valueEntities)) {

                    for (GenericKeySetValueEntity valueEntity : valueEntities) {

                        if (Constants.ACTIVE.equalsIgnoreCase(valueEntity.getStatus())) {
                            if (statusCode.equals(valueEntity.getKey())) {
                                return valueEntity.getValue();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
