package com.oneassist.serviceplatform.services.document;

import java.io.File;
import java.util.List;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.simple.parser.ParseException;

import com.oneassist.serviceplatform.commons.enums.ServiceReqeustEntityDocumentName;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestReportRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestReportResponseDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.DocumentDownloadRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceDocumentDto;
import com.oneassist.serviceplatform.contracts.response.DownloadDocumentDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;

public interface IServiceRequestDocumentService {

	// public ResponseDto<ServiceDocumentDto>
	// uploadMultipleServiceDocuments(Long serviceRequestId, List<BodyPart>
	// bodyParts, String docId, String docTypeId) throws Exception;

	public void uploadMultipleServiceDocuments(Long serviceRequestId, File file, Long docTypeId, String contentType)
			throws Exception;

	public ResponseDto<DownloadDocumentDto> downloadServiceRequestDocument(String documentIds, String fileTypeRequired)
			throws Exception;

	public ResponseDto<ServiceDocumentDto> deleteServiceRequestDocument(String documentIds) throws ParseException;

	ResponseDto<ServiceDocumentDto> updateMultipleServiceDocuments(List<BodyPart> bodyParts, String docId)
			throws Exception;

	public ResponseDto<ServiceRequestReportResponseDto> generateServiceRequestReport(
			ServiceRequestReportRequestDto serviceRequestReportDto) throws Exception;

	public DownloadDocumentDto downloadImageFileDocument(DocumentDownloadRequestDto documentDownloadRequestDto)
			throws Exception;



	public DownloadDocumentDto getDownLoadDocumentDtoByFileType(DocumentDownloadRequestDto documentDownloadRequestDto,
			String fileTypeRequired) throws Exception;

	ResponseDto<ServiceDocumentDto> uploadMultipleServiceDocuments(Long serviceRequestId, FormDataMultiPart multiPart,
			String documentId, String docTypeId, ServiceReqeustEntityDocumentName entityName, String entityId,
			String storageReferenceId, String documentKey, String isCMSCallRequired, String requestFileName)
			throws Exception;

}
