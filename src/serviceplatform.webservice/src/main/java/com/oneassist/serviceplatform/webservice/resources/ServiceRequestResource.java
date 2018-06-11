package com.oneassist.serviceplatform.webservice.resources;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.RestResourceException;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.AssetClaimEligibilityDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CostToServiceDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestSearchDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.request.AssigneeRepairCostRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.request.CompleteWHCInspectionRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceRequestAuthorizationResponseDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import com.oneassist.serviceplatform.contracts.response.base.BaseResponse;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerCompleteWHCInspectionResponse;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerCostToCustomerResponse;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerServiceReqDetailResponse;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerServiceReqGetAuthCodeResponse;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerServiceReqSearchResponse;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerServiceReqValidateAuthCodeResponse;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerServiceResDetailResponse;
import com.oneassist.serviceplatform.services.servicerequest.IServiceRequestService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * LogisticResource Resource This call will manage all the shipment related web services
 * 
 * @author satish.kumar
 */
@Path("/servicerequests")
@Component
@Api(tags = { "/servicerequests : All About Managing Service Request Information" })
public class ServiceRequestResource extends BaseResource {

    private final Logger logger = Logger.getLogger(ServiceRequestResource.class);

    @Autowired
    private IServiceRequestService serviceRequestService;

    private static final String REQUIRE_ADDITIONAL_INFO = "Y";

    /**
     * This method will create service request
     * 
     * @param shipmentRequest
     * @return
     * @throws RestResourceException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Creates new service requests for all service types")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceReqDetailResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response createServiceRequest(ServiceRequestDto serviceRequestDto) throws RestResourceException {
        ResponseDto<ServiceRequestDto> response = new ResponseDto<>();

        try {
            ServiceRequestDto data = serviceRequestService.createServiceRequest(serviceRequestDto);
            response.setData(data);

            this.setSuccessStatus(response, ServiceRequestResponseCodes.CREATE_SERVICE_REQUEST_SUCCESS.getErrorCode(), new Object[] { data.getServiceRequestId().toString() });
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestResponseCodes.CREATE_SERVICE_REQUEST_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    /**
     * This method will update service request
     * 
     * @param ServiceRequestDto
     * @param action
     *            define the parameter to update in Service Request
     * @return
     * @throws RestResourceException
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{serviceRequestId}/{action}")
    @ApiOperation(value = "Provides a convenience to update the Service Request Details")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceResDetailResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response updateServiceRequest(@PathParam("serviceRequestId") @ApiParam(value = "Service Request Id", required = true) Long serviceRequestId,
            @PathParam("action") @ApiParam(value = "action", required = true) String action, @QueryParam("eventCode") @ApiParam(value = "Event Code", required = true) String eventCode,
            ServiceRequestDto serviceRequestUpdateDto) throws RestResourceException {

        logger.debug(">>> ServiceRequestResource update Service Request API Starts: " + serviceRequestId + action + eventCode);
        logger.debug("serviceRequestUpdateDto::" + serviceRequestUpdateDto);

        ResponseDto<ServiceResponseDto> response = new ResponseDto<>();

        try {

            serviceRequestUpdateDto.setServiceRequestId(serviceRequestId);
            ServiceRequestUpdateAction updateAction = ServiceRequestUpdateAction.getServiceRequestUpdateAction(action);

            if (StringUtils.isEmpty(eventCode)) {
                eventCode = ServiceRequestEventCode.DEFAULT.getServiceRequestEvent();
            }

            ServiceRequestEventCode updateEventCode = ServiceRequestEventCode.getServiceRequestEvent(eventCode);

            response.setData(serviceRequestService.performServiceAction(updateAction, serviceRequestUpdateDto, updateEventCode));

            this.setSuccessStatus(response, ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_SUCCESS.getErrorCode());
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode(),ex.getMessage(),null, ex);
        }

        logger.debug("<<< ServiceRequestResource update Service Request API Ends" + serviceRequestId + action + eventCode);
        
        return Response.status(Status.OK).entity(response).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{serviceRequestId}/costtocustomer")
    @ApiOperation(value = "Provides a facility to calculate the cost to customer based on the diagnosis reported by the assignee and the spare parts required")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerCostToCustomerResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response calculateCostToCustomer(@PathParam("serviceRequestId") Long serviceRequestId, AssigneeRepairCostRequestDto assigneeRepairCostRequestDto) throws RestResourceException {

        ResponseDto<CostToServiceDto> response = new ResponseDto<>();

        try {
            CostToServiceDto costToServiceDto = serviceRequestService.calculateCostToCustomer(serviceRequestId, assigneeRepairCostRequestDto);
            response.setData(costToServiceDto);

            this.setSuccessStatus(response, ServiceRequestResponseCodes.CALCULATE_SERVICECOST_CUSTOMER_SUCCESS.getErrorCode());
        } catch (BusinessServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestResponseCodes.CALCULATE_SERVICECOST_CUSTOMER_FAILED.getErrorCode(), ex);
        }
        
        return Response.status(Status.OK).entity(response).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Provides support to search service request based on criteria filter")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceReqSearchResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response getServiceRequests(@QueryParam("serviceRequestId") Long serviceRequestId, @QueryParam("refPrimaryTrackingNo") String refPrimaryTrackingNo, @QueryParam("assignee") Long assignee,
            @QueryParam("servicePartnerCode") Long servicePartnerCode, @QueryParam("servicePartnerBuCode") Long servicePartnerBuCode, @QueryParam("workFlowStage") String workFlowStage,
            @QueryParam("status") String status, @QueryParam("serviceRequestTypes") String serviceRequestTypes, @QueryParam("fromDate") String fromDate, @QueryParam("toDate") String toDate,
            @QueryParam("fromTime") String fromTime, @QueryParam("toTime") String toTime, @QueryParam("page") Integer page, @QueryParam("size") Integer size,
            @QueryParam("workFlowAlerts") String workFlowAlerts, @QueryParam("requiresAdditionalDetails") String requiresAdditionalDetails, @QueryParam("technicianProfileRequired") String technicianProfileRequired,
            @QueryParam("referenceNumbers") String referenceNumbers, @QueryParam("feedbackStatus") String feedbackStatus, @QueryParam("sortBy") String sortBy, @QueryParam("sortOrder") String sortOrder,
            @QueryParam("workFlowStageStatus") String workFlowStageStatus, @QueryParam("initiatingSystem") Integer initiatingSystem, @QueryParam("options") String options) throws RestResourceException {

        ResponseDto<List<ServiceResponseDto>> response = new ResponseDto<>();

        try {
            DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            ServiceRequestSearchDto serviceRequestSearchDto = new ServiceRequestSearchDto();
            serviceRequestSearchDto.setAssignee(assignee);
            serviceRequestSearchDto.setServiceRequestId(serviceRequestId);
            serviceRequestSearchDto.setRefPrimaryTrackingNo(refPrimaryTrackingNo);
            serviceRequestSearchDto.setServicePartnerCode(servicePartnerCode);
            serviceRequestSearchDto.setServicePartnerBuCode(servicePartnerBuCode);
            serviceRequestSearchDto.setWorkflowStage(workFlowStage);
            serviceRequestSearchDto.setServiceRequestType(serviceRequestTypes);
            serviceRequestSearchDto.setStatus(status);
            serviceRequestSearchDto.setRequiresAdditionalDetails(requiresAdditionalDetails);
            serviceRequestSearchDto.setWorkFlowAlert(workFlowAlerts);
            serviceRequestSearchDto.setTechnicianProfileRequired(technicianProfileRequired);
            serviceRequestSearchDto.setReferenceNumbers(referenceNumbers);
            serviceRequestSearchDto.setFeedbackStatus(feedbackStatus);
            serviceRequestSearchDto.setSortBy(sortBy);
            serviceRequestSearchDto.setSortOrder(sortOrder);
            serviceRequestSearchDto.setWorkFlowStageStatus(workFlowStageStatus);
            serviceRequestSearchDto.setInitiatingSystem(initiatingSystem);
            
            if(StringUtils.isNotBlank(options)){
            Set<String> optionSet = new HashSet<>(com.oneassist.serviceplatform.commons.utils.StringUtils.getListFromString(options));
            serviceRequestSearchDto.setOptions(optionSet);
            }
            
            
            if (!Strings.isNullOrEmpty(fromDate)) {
                serviceRequestSearchDto.setFromDate(df.parse(fromDate));
            }

            if (!Strings.isNullOrEmpty(toDate)) {
                serviceRequestSearchDto.setToDate(df.parse(toDate));
            }

            if (!Strings.isNullOrEmpty(fromTime)) {
                serviceRequestSearchDto.setFromTime(fromTime);
            }

            if (!Strings.isNullOrEmpty(toTime)) {
                serviceRequestSearchDto.setToTime(toTime);
            }
            
            PageRequest pageRequest = null;
            
            if (page != null && page > 0 && size != null && size > 0) {
                page = page - 1;
                pageRequest = new PageRequest(page, size);
            }

            response = serviceRequestService.getServiceRequests(serviceRequestSearchDto, pageRequest);

            this.setSuccessStatus(response, ServiceRequestResponseCodes.GET_SERVICE_REQUEST_SUCCESS.getErrorCode());
        } catch (java.text.ParseException ex) {

            throw new RestResourceException(ServiceRequestResponseCodes.GET_SERVICE_REQUEST_INVALID_DATE_FROMAT_FAILED.getErrorCode(), ex);
        } catch (Exception ex) {

            throw new RestResourceException(ServiceRequestResponseCodes.GET_SERVICE_REQUEST_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    /**
     * 
     * @param authorizationCode
     * @param authorizationCodeSequence
     * @return
     * @throws JsonProcessingException
     * @throws RestResourceException
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{serviceRequestId}/validateAuthorizationCode")
    @ApiOperation(value = "Provides support for validating authorization code")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceReqValidateAuthCodeResponse.class, message = "Success"),
            @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response validateAuthorizationCode(@PathParam("serviceRequestId") @ApiParam(value = "Service Request Id", required = true) Long serviceRequestId,
            @QueryParam("authorizationCode") @ApiParam(value = "Authorization Code", required = true) String authorizationCode,
            @QueryParam("authorizationCodeSequence") @ApiParam(value = "1 means, validate service start auth code, 2 means service end auth code", required = true) int authorizationCodeSequence)
            throws JsonProcessingException, RestResourceException {

        ResponseDto<ServiceResponseDto> response = new ResponseDto<>();

        try {

            ServiceResponseDto serviceResponseDto = serviceRequestService.validateAuthorizationCode(serviceRequestId, authorizationCode, authorizationCodeSequence);
            response.setData(serviceResponseDto);

            this.setSuccessStatus(response, ServiceRequestResponseCodes.AUTHORIZATION_CODE_VALIDATION_SUCCESS.getErrorCode());
        } catch (Exception ex) {

            throw new RestResourceException(ServiceRequestResponseCodes.AUTHORIZATION_CODE_VALIDATION_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    /**
     * This method will return authorization code
     * 
     * @param serviceRequestId
     * @throws RestResourceException
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{serviceRequestId}/authorizationCode")
    @ApiOperation(value = "Provides support to Get authorization code")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceReqGetAuthCodeResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response getAuthorizationCode(@PathParam("serviceRequestId") Long serviceRequestId) throws RestResourceException {
        ResponseDto<ServiceRequestAuthorizationResponseDto> response = new ResponseDto<ServiceRequestAuthorizationResponseDto>();

        try {
            ServiceRequestAuthorizationResponseDto authorizationResponseDto = serviceRequestService.getAuthorizationCode(serviceRequestId);

            response.setData(authorizationResponseDto);

            this.setSuccessStatus(response, ServiceRequestResponseCodes.AUTHORIZATION_CODE_FETCH_SUCCESS.getErrorCode());
        } catch (Exception ex) {

            throw new RestResourceException(ServiceRequestResponseCodes.AUTHORIZATION_CODE_FETCH_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    /**
     * This method will send authorization code
     * 
     * @param serviceRequestId
     * @throws RestResourceException
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{serviceRequestId}/sendOTP")
    @ApiOperation(value = "Provides support to send authorization code")
    @ApiResponses({ @ApiResponse(code = 200, response = BaseResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response sendAuthorizationCode(@PathParam("serviceRequestId") Long serviceRequestId) throws RestResourceException {
        ResponseDto<ServiceRequestAuthorizationResponseDto> response = new ResponseDto<ServiceRequestAuthorizationResponseDto>();

        try {
            ServiceRequestAuthorizationResponseDto authorizationResponseDto = serviceRequestService.getAuthorizationCode(serviceRequestId);

            response.setData(authorizationResponseDto);

            this.setSuccessStatus(response, ServiceRequestResponseCodes.AUTHORIZATION_CODE_FETCH_SUCCESS.getErrorCode());
        } catch (Exception ex) {

            throw new RestResourceException(ServiceRequestResponseCodes.AUTHORIZATION_CODE_FETCH_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    /**
     * This method will update service request
     * 
     * @param ServiceRequestUpdateRequestDto
     * @param action
     *            define the parameter to update in Service Request
     * @throws RestResourceException
     * @throws BusinessServiceException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{serviceRequestId}/completeServiceRequest")
    @ApiOperation(value = "Provides a convenience to update the Service Request Details And Pushes inspection details in QUEUE")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerCompleteWHCInspectionResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response completeServiceRequest(@PathParam("serviceRequestId") @ApiParam(value = "Service Request Id", required = true) Long serviceRequestId,
            CompleteWHCInspectionRequestDto completeWHCInspectionRequestDto) throws RestResourceException {

        logger.info(">>> ServiceRequestResource completeServiceRequest API ServiceRequestID: " + serviceRequestId);
        ResponseDto<CompleteWHCInspectionRequestDto> response = new ResponseDto<>();

        try {

            completeWHCInspectionRequestDto.setServiceRequestId(serviceRequestId);
            response.setData(serviceRequestService.completeWHCInspection(completeWHCInspectionRequestDto));

            this.setSuccessStatus(response, ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_SUCCESS.getErrorCode());
        } catch (Exception ex) {

            throw new RestResourceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{serviceRequestId}")
    @ApiOperation(value = "Provides support to search service request based on criteria filter")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceReqSearchResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response getServiceRequests(@PathParam("serviceRequestId") Long serviceRequestId) throws RestResourceException {

        ResponseDto<List<ServiceResponseDto>> response = new ResponseDto<>();

        try {

            ServiceRequestSearchDto serviceRequestSearchDto = new ServiceRequestSearchDto();
            serviceRequestSearchDto.setServiceRequestId(serviceRequestId);
            serviceRequestSearchDto.setRequiresAdditionalDetails(REQUIRE_ADDITIONAL_INFO);

            response = serviceRequestService.getServiceRequests(serviceRequestSearchDto, null);

            this.setSuccessStatus(response, ServiceRequestResponseCodes.GET_SERVICE_REQUEST_SUCCESS.getErrorCode());
        } catch (Exception ex) {

            throw new RestResourceException(ServiceRequestResponseCodes.GET_SERVICE_REQUEST_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/checkclaimeligibility")
    @ApiOperation(value = "Provides support to check if a claim request can be raised")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceReqSearchResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response getRaiseClaimEligibility(@QueryParam(value="eligibilityStartDate") String eligibilityStartDate ,
    		@QueryParam(value = "referenceNo") String referenceNo) throws BusinessServiceException{

        ResponseDto<List<AssetClaimEligibilityDto>> response = new ResponseDto<>();

    	try{
    		Date membershipStartdate = new Date(Long.parseLong(eligibilityStartDate));
    		List<AssetClaimEligibilityDto> invalidAssets = serviceRequestService.checkRaiseClaimEligibility(membershipStartdate, referenceNo);
    		response.setData(invalidAssets);
			if (CollectionUtils.isNotEmpty(invalidAssets)) {
				this.setSuccessStatus(response,
						ServiceRequestResponseCodes.SERVICE_REQUEST_CLAIM_NOT_ELIGIBLE.getErrorCode());
			} else {
				this.setSuccessStatus(response,
						ServiceRequestResponseCodes.SERVICE_REQUEST_CLAIM_ELIGIBLE.getErrorCode());
			}
    	}catch(Exception e){
    		throw new BusinessServiceException(ServiceRequestResponseCodes.FAILED_CLAIM_RETRIEVAL_STATUS.getErrorCode());
    	}
    	
    	return Response.status(Status.OK).entity(response).build();
    	
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Updates existing service requests for all service types")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceReqDetailResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response updateServiceRequest(ServiceRequestDto serviceRequestDto) throws RestResourceException {
        ResponseDto<ServiceRequestDto> response = new ResponseDto<>();

        try {
        	
            serviceRequestService.updateServiceRequest(serviceRequestDto);

            this.setSuccessStatus(response, ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_SUCCESS.getErrorCode(), new Object[] {});
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }
}
