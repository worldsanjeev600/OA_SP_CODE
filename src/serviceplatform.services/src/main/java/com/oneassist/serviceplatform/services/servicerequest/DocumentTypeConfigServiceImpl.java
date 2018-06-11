package com.oneassist.serviceplatform.services.servicerequest;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oneassist.serviceplatform.commons.entities.DocTypeConfigDetailEntity;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.mastercache.DocTypeConfigDetailCache;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DocumentTypeConfigDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.DocumentTypeConfigResponse;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;

@Service
public class DocumentTypeConfigServiceImpl implements IDocumentTypeConfigService {

	private final Logger logger = Logger.getLogger(DocumentTypeConfigServiceImpl.class);

	@Autowired
	private DocTypeConfigDetailCache docTypeConfigDetailCache;

	@Override
	public ResponseDto<List<DocumentTypeConfigResponse>> getAllBySearchCriteria(
			DocumentTypeConfigDto documentTypeConfigDto) throws BusinessServiceException {

		ResponseDto<List<DocumentTypeConfigResponse>> response = null;

		List<DocTypeConfigDetailEntity> docTypeConfigDetailEntities = docTypeConfigDetailCache
				.get(documentTypeConfigDto.getServiceRequestTypeId());

		List<DocTypeConfigDetailEntity> filteredDocTypeConfigDetailEntityList = new ArrayList<DocTypeConfigDetailEntity>();
		if (docTypeConfigDetailEntities != null && docTypeConfigDetailEntities.size() > 0) {

			if (documentTypeConfigDto.getReferenceCode() != null
					|| documentTypeConfigDto.getInsurancePartnerCode() != null) {

				for (DocTypeConfigDetailEntity docTypeConfigDetailEntity : docTypeConfigDetailEntities) {

					if (documentTypeConfigDto.getReferenceCode() != null
							&& documentTypeConfigDto.getInsurancePartnerCode() == null) {

						if (documentTypeConfigDto.getReferenceCode()
								.equals(docTypeConfigDetailEntity.getReferenceCode()))
							filteredDocTypeConfigDetailEntityList.add(docTypeConfigDetailEntity);
					}

					else if (documentTypeConfigDto.getReferenceCode() == null
							&& documentTypeConfigDto.getInsurancePartnerCode() != null) {

						if (documentTypeConfigDto.getInsurancePartnerCode()
								.equals(docTypeConfigDetailEntity.getInsurancePartnerCode()))
							filteredDocTypeConfigDetailEntityList.add(docTypeConfigDetailEntity);
					}

					else if (documentTypeConfigDto.getReferenceCode() != null
							&& documentTypeConfigDto.getInsurancePartnerCode() != null) {

						if (documentTypeConfigDto.getInsurancePartnerCode()
								.equals(docTypeConfigDetailEntity.getInsurancePartnerCode())
								&& documentTypeConfigDto.getReferenceCode()
										.equals(docTypeConfigDetailEntity.getReferenceCode()))
							filteredDocTypeConfigDetailEntityList.add(docTypeConfigDetailEntity);

					}

				}

			} else {
				logger.info("Document details for service Type :  " + documentTypeConfigDto.getServiceRequestTypeId()
						+ " and values :" + docTypeConfigDetailEntities);
				filteredDocTypeConfigDetailEntityList.addAll(docTypeConfigDetailEntities);
			}

			List<DocTypeConfigDetailEntity> finalDocTypeConfigDetailEntityList = new ArrayList<DocTypeConfigDetailEntity>();
			if (documentTypeConfigDto.getIsMandatory() != null) {
				
				for (DocTypeConfigDetailEntity docTypeConfigDetailEntity : filteredDocTypeConfigDetailEntityList) {

					if (docTypeConfigDetailEntity.getIsMandatory().equals(documentTypeConfigDto.getIsMandatory())) {
						finalDocTypeConfigDetailEntityList.add(docTypeConfigDetailEntity);
					}

				}

			}

			else {
				finalDocTypeConfigDetailEntityList.addAll(filteredDocTypeConfigDetailEntityList);
			}

			List<DocumentTypeConfigResponse> documentTypeConfigResponseDtos = makeDocumentTypeListResponse(
					finalDocTypeConfigDetailEntityList);

			response = new ResponseDto<List<DocumentTypeConfigResponse>>();
			response.setStatus(ResponseConstant.SUCCESS);
			response.setData(documentTypeConfigResponseDtos);
			response.setMessage("Total #" + documentTypeConfigResponseDtos.size() + " fetched successfully");

			/*
			 * if list of entities are null or empty then set response data null
			 */
		} else {
			response = new ResponseDto<List<DocumentTypeConfigResponse>>();
			response.setStatus(ResponseConstant.SUCCESS);
			response.setData(new ArrayList());
			response.setMessage("No data found");
		}

		logger.info("End : service class");
		return response;
	}

	private List<DocumentTypeConfigResponse> makeDocumentTypeListResponse(
			List<DocTypeConfigDetailEntity> docTypeConfigDetailEntities) {

		List<DocumentTypeConfigResponse> documentTypeConfigResponseDtos = new ArrayList<DocumentTypeConfigResponse>();

		for (DocTypeConfigDetailEntity docTypeConfigDetailEntity : docTypeConfigDetailEntities) {

			/* Set only required parameters in response dto */
			DocumentTypeConfigResponse documentTypeConfigResponse = new DocumentTypeConfigResponse();
			documentTypeConfigResponse.setDocTypeId(docTypeConfigDetailEntity.getDocTypeId());
			documentTypeConfigResponse.setDocName(docTypeConfigDetailEntity.getDocTypeMstEntity().getDocName());
			documentTypeConfigResponse.setInsurancePartnerCode(docTypeConfigDetailEntity.getInsurancePartnerCode());
			documentTypeConfigResponse.setIsMandatory(docTypeConfigDetailEntity.getIsMandatory());
			documentTypeConfigResponse.setReferenceCode(docTypeConfigDetailEntity.getReferenceCode());
			documentTypeConfigResponse.setDescription(docTypeConfigDetailEntity.getDocTypeMstEntity().getDescription());
			documentTypeConfigResponse.setDocKey(docTypeConfigDetailEntity.getDocTypeMstEntity().getDocKey());
			documentTypeConfigResponse.setDescription(docTypeConfigDetailEntity.getDocTypeMstEntity().getDescription());
			documentTypeConfigResponseDtos.add(documentTypeConfigResponse);

		}

		return documentTypeConfigResponseDtos;
	}

}
