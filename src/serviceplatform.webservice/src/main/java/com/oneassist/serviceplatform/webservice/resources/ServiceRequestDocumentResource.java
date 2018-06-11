package com.oneassist.serviceplatform.webservice.resources;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.oneassist.serviceplatform.commons.enums.FileType;
import com.oneassist.serviceplatform.commons.enums.ServiceReqeustEntityDocumentName;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestReport;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.exception.RestResourceException;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestReportRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestReportResponseDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceDocumentDto;
import com.oneassist.serviceplatform.contracts.response.DownloadDocumentDto;
import com.oneassist.serviceplatform.contracts.response.base.BaseResponse;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerServiceReqUploadResponse;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import com.oneassist.serviceplatform.services.document.IServiceRequestDocumentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Common resource
 * 
 * @author divya.hl
 */
/**
 * @author Srikanth.kottu
 */
@Path("/servicerequests")
@Component
@Api(tags = { "/servicerequests : All About Managing Service Request Information" })
public class ServiceRequestDocumentResource extends BaseResource {

	private final Logger logger = Logger.getLogger(ServiceRequestDocumentResource.class);

	@Autowired
	private IServiceRequestDocumentService serviceRequestDocumentService;

	/**
	 * This Method Provides support for uploading a Service Request document
	 * 
	 * @param multiPart
	 * @param serviceRequestId
	 * @param documentDto
	 * @return
	 * @throws RestResourceException
	 */
	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{serviceRequestId}/documents")
	@ApiOperation(value = "Provides support for upload a service request document")
	@ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceReqUploadResponse.class, message = "Success"),
			@ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
	public Response createOrReplaceServiceRequestDocument(final FormDataMultiPart multiPart,
			@PathParam("serviceRequestId") @ApiParam(value = "Service Request Id", required = true) Long serviceRequestId,
			@QueryParam("documentId") @ApiParam(value = "Document Id", required = false) String documentId,
			@QueryParam("documentTypeId") @ApiParam(value = "Document Type Id", required = false) String documentTypeId,
			@QueryParam("entityName") @ApiParam(value = "Entity Name", required = false) ServiceReqeustEntityDocumentName entityName,
			@QueryParam("entityId") @ApiParam(value = "Entity Id", required = false) String entityId,
			@QueryParam("storageReferenceId") @ApiParam(value = "MongoDB reference ID", required = false) String storageReferenceId,
			@QueryParam("documentKey") @ApiParam(value = "Document Key", required = false) String documentKey,
			@QueryParam("isCMSCallRequired") @ApiParam(value = "Is call to CMS to update the document details required", required = false) String isCMSCallRequired,
			@QueryParam("fileName") @ApiParam(value = "File Name", required = false) String requestFileName)
			throws RestResourceException {

		ResponseDto<ServiceDocumentDto> response = new ResponseDto<>();

		logger.error("Received upload request  for :serviceRequestId:" + serviceRequestId + ",documentId:" + documentId
				+ ",documentTypeId:" + documentTypeId + ",multiPart:"
				+ ((multiPart != null && multiPart.getBodyParts() != null) ? multiPart.getBodyParts().size()
						: "is empty")
				+ ", entityName: " + entityName + ", entityId: " + entityId + ", storageReferenceId: "
				+ storageReferenceId + ", fileName: " + requestFileName +", isCMSCallRequired: "+isCMSCallRequired+", documentKey: "+documentKey);
		try {

			response = serviceRequestDocumentService.uploadMultipleServiceDocuments(serviceRequestId, multiPart,
					documentId, documentTypeId, entityName, entityId, storageReferenceId, documentKey, isCMSCallRequired,requestFileName);

			if (response.getStatus() == null && !response.getStatus().equals(ResponseConstant.FAILED)) {
				this.setSuccessStatus(response,
						ServiceRequestResponseCodes.CREATE_SERVICE_DOCUMENT_SUCCESS.getErrorCode());
			}

		} catch (SQLIntegrityConstraintViolationException | DataIntegrityViolationException
				| ConstraintViolationException ex) {

			throw new RestResourceException(ServiceRequestResponseCodes.INVALID_DOCUMENT_TYPE.getErrorCode(), ex);

		} catch (Exception ex) {

			logger.error("Exception while uploading/updating document: ", ex);
			throw new RestResourceException(ServiceRequestResponseCodes.UPLOAD_SERVICE_DOCUMENT_FAILED.getErrorCode(),
					ex);
		}

		return Response.status(Status.OK).entity(response).build();
	}

	/**
	 * This Method Provides support for downloading a Service Request document
	 * 
	 * @param serviceRequestId
	 * @param documentRefId
	 * @param fileTypeRequired
	 * @return
	 * @throws RestResourceException
	 */
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/documents/{documentIds}")
	@ApiOperation(value = "Provides support for download a service request document")
	@ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceReqUploadResponse.class, message = "Success"),
			@ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
	public Response getServiceRequestDocument(@QueryParam("fileTypeRequired") String fileTypeRequired,
			@PathParam("documentIds") String documentIds) throws RestResourceException {
		ResponseDto<DownloadDocumentDto> response = new ResponseDto<DownloadDocumentDto>();
		String fileName = "Documents.zip";

		try {
			if (StringUtils.isEmpty(fileTypeRequired)) {
				fileTypeRequired = FileType.ZIPFILE.getFileTypeName();
			}
			response = serviceRequestDocumentService.downloadServiceRequestDocument(documentIds, fileTypeRequired);

			if (fileTypeRequired != null && (fileTypeRequired.equalsIgnoreCase(FileType.BYTEARRAYFILE.getFileTypeName())
					|| fileTypeRequired.equalsIgnoreCase(FileType.IMAGEFILE.getFileTypeName())
					|| fileTypeRequired.equalsIgnoreCase(FileType.THUMBNAILIMAGEFILE.getFileTypeName()))) {
				fileName = response.getData().getFileName();
			}
		} catch (Exception ex) {
			throw new RestResourceException(ServiceRequestResponseCodes.SERVICE_DOCUMENT_DELETE_FAILED.getErrorCode(),
					ex);
		}

		return Response.ok(response.getData().getInputStream(), MediaType.APPLICATION_OCTET_STREAM)
				.header("content-disposition", "attachment;filename=" + fileName).build();
	}

	/**
	 * This Method Provides support for Deleting a Service Request document
	 * 
	 * @param serviceRequestId
	 * @param documentDto
	 * @return
	 * @throws RestResourceException
	 */
	@DELETE
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/documents/{documentIds}")
	@ApiOperation(value = "Provides support for delete a service request document")
	@ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceReqUploadResponse.class, message = "Success"),
			@ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
	public Response deleteServiceRequestDocument(@PathParam("documentIds") String documentIds)
			throws RestResourceException {

		ResponseDto<ServiceDocumentDto> response = new ResponseDto<>();

		try {

			response = serviceRequestDocumentService.deleteServiceRequestDocument(documentIds);
			this.setSuccessStatus(response, ServiceRequestResponseCodes.SERVICE_DOCUMENT_DELETE_SUCCESS.getErrorCode());
		} catch (Exception ex) {
			throw new RestResourceException(ServiceRequestResponseCodes.SERVICE_DOCUMENT_DELETE_FAILED.getErrorCode(),
					ex);
		}

		return Response.status(Status.OK).entity(response).build();
	}

	/**
	 * This Method Provides support for uploading a Service Request document
	 * 
	 * @param multiPart
	 * @param serviceRequestId
	 * @param documentDto
	 * @return
	 * @throws RestResourceException
	 */
	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/documents/{documentId}")
	@ApiOperation(value = "Provides support for upload a Service Request document")
	@ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceReqUploadResponse.class, message = "Success"),
			@ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
	public Response updateServiceRequestDocument(final FormDataMultiPart multiPart,
			@PathParam("documentId") String documentId) throws RestResourceException {

		ResponseDto<ServiceDocumentDto> response = new ResponseDto<>();
		List<BodyPart> bodyParts = multiPart.getBodyParts();

		try {

			response = serviceRequestDocumentService.updateMultipleServiceDocuments(bodyParts, documentId);
			this.setSuccessStatus(response, ServiceRequestResponseCodes.CREATE_SERVICE_DOCUMENT_SUCCESS.getErrorCode());
		} catch (Exception ex) {
			throw new RestResourceException(ServiceRequestResponseCodes.UPLOAD_SERVICE_DOCUMENT_FAILED.getErrorCode(),
					ex);
		}

		return Response.status(Status.OK).entity(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/{serviceRequestId}/documents")
	@ApiOperation(value = "Provides support to generate a service request Jobsheet and Tax Invoice report")
	@ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceReqUploadResponse.class, message = "Success"),
			@ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
	public Response generateServiceRequestReport(@PathParam("serviceRequestId") Long serviceRequestId,
			@QueryParam("reportType") ServiceRequestReport reportType) throws RestResourceException {
		ServiceRequestReportRequestDto serviceRequestReportDto = new ServiceRequestReportRequestDto();
		ResponseDto<ServiceRequestReportResponseDto> serviceRequestReportResponseDto = new ResponseDto<ServiceRequestReportResponseDto>();
		try {
			serviceRequestReportDto.setServiceRequestId(serviceRequestId);
			serviceRequestReportDto.setReportType(reportType.getServiceRequestReport());
			serviceRequestReportResponseDto = serviceRequestDocumentService
					.generateServiceRequestReport(serviceRequestReportDto);
		} catch (Exception ex) {
			throw new RestResourceException(ServiceRequestResponseCodes.SERVICE_DOCUMENT_DELETE_FAILED.getErrorCode(),
					ex);
		}

		return Response.ok(serviceRequestReportResponseDto.getData().getInputStream()).header("content-disposition",
				"attachment;filename="+reportType+"_" + serviceRequestReportDto.getData().get("$serviceRequestId") + ".pdf")
				.build();
	}
}
