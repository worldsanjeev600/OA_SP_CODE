package com.oneassist.serviceplatform.webservice.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.oneassist.serviceplatform.commons.enums.LogisticResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.RestResourceException;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.PincodeServiceDto;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.PincodeServiceFulfilmentDto;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.ServicePartnerAllocationRequest;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.ServicePartnerAllocationSearchRequest;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.ServicePartnerAllocationSearchResult;
import com.oneassist.serviceplatform.contracts.response.base.BaseResponse;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.contracts.response.swagger.allocation.SwaggerAllocationAddPincodeServiceFulfilmentResponse;
import com.oneassist.serviceplatform.contracts.response.swagger.allocation.SwaggerAllocationAddPincodeServiceResponse;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerHubAllocationResponse;
import com.oneassist.serviceplatform.services.allocation.IAllocationMasterService;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Allocation Master Resource
 * 
 * @author divya.hl
 */
@Path("/allocationMaster")
@Component
@Api(tags = { "/allocationMaster : All About Managing Allocation Master" })
public class AllocationMasterResource extends BaseResource {

    @Autowired
    private IAllocationMasterService allocationMasterService;

    /**
     * Add pincode service master.
     * 
     * @param pincode
     * @param hubid
     * @param isCoutesyApplicable
     * @return - response of success or failure status.
     * @throws PincodeServiceException
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addPincodeServiceMaster")
    @ApiOperation(value = "Provides support for adding Pincode Service master in bulk")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerAllocationAddPincodeServiceResponse.class, message = "Success"),
            @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response addPincodeServiceMaster(List<PincodeServiceDto> pincodeServiceDto) throws RestResourceException {
        ResponseDto<List<PincodeServiceDto>> response = null;
        try {
            response = allocationMasterService.addPincodeServiceMaster(pincodeServiceDto);
            this.setSuccessStatus(response, ServiceRequestResponseCodes.CREATE_SERVICE_DOCUMENT_SUCCESS.getErrorCode());
        } catch (Exception ex) {
            throw new RestResourceException(LogisticResponseCodes.ALLOCATION_MASTER_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    /**
     * Add pincode service fulfilment master.
     * 
     * @param pincode
     * @param serviceId
     * @param partnerPriority
     * @param partnerCode
     *            * @param subcategory
     * @param serviceTat
     * @return - response of success or failure status.
     * @throws PincodeServiceFulfilmentMasterException
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addPincodeServiceFulfilmentMaster")
    @ApiOperation(value = "Provides support for adding Pincode Service Fulmilment master in bulk")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerAllocationAddPincodeServiceFulfilmentResponse.class, message = "Success"),
            @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response addPincodeServiceFulfilmentMaster(List<PincodeServiceFulfilmentDto> pincodeServiceFulfilmentDtos) throws RestResourceException {
        ResponseDto<List<PincodeServiceFulfilmentDto>> response = new ResponseDto<List<PincodeServiceFulfilmentDto>>();
        try {
            response = allocationMasterService.addPincodeServiceFulfilmentMaster(pincodeServiceFulfilmentDtos);
            this.setSuccessStatus(response, ServiceRequestResponseCodes.CREATE_SERVICE_DOCUMENT_SUCCESS.getErrorCode());
        } catch (RestResourceException ex) {
            throw new RestResourceException(LogisticResponseCodes.ALLOCATION_MASTER_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Provides all the Pincode Service and Fulfilment Data")
    public Response getAllocationMasterData() throws RestResourceException {

        ResponseDto<Map<String, List<Object>>> response = null;
        try {
            response = allocationMasterService.loadAllocationMasterData();
            this.setSuccessStatus(response, ServiceRequestResponseCodes.CREATE_SERVICE_DOCUMENT_SUCCESS.getErrorCode());
        } catch (Exception ex) {
            throw new RestResourceException(LogisticResponseCodes.ALLOCATION_MASTER_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Provides all the Pincode Service and Fulfilment Data")
    public Response modifyServicePartnerAllocation(List<ServicePartnerAllocationRequest> servicePartnerAllocationRequest) throws RestResourceException {

        ResponseDto<Object> response = new ResponseDto<Object>();
        try {
            allocationMasterService.modifyServicePartnerAllocation(servicePartnerAllocationRequest);
            response.setStatus(ResponseConstant.SUCCESS);
            response.setMessage(ResponseConstant.SUCCESS);
        } catch (Exception ex) {
            throw new RestResourceException(LogisticResponseCodes.ALLOCATION_MASTER_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Returns fulfilent master by pincodes,city,state, product type etc,.")
    @Path("/search")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerHubAllocationResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response search(@QueryParam("pincodes") @ApiParam(value = "Pincodes") String pincodes, @QueryParam("states") @ApiParam(value = "states", required = false) String states,
            @QueryParam("cities") @ApiParam(value = "cities", required = false) String cities, @QueryParam("productCode") @ApiParam(value = "productCode", required = false) String productCode,
            @QueryParam("serviceType") @ApiParam(value = "serviceType", required = false) String serviceType,
            @QueryParam("partnerCode") @ApiParam(value = "partnerCode", required = false) Long partnerCode, @QueryParam("page") Integer page, @QueryParam("size") Integer size)
            throws BusinessServiceException {

        ResponseDto<Collection<ServicePartnerAllocationSearchResult>> response = new ResponseDto<Collection<ServicePartnerAllocationSearchResult>>();
        try {
            ServicePartnerAllocationSearchRequest servicePartnerAllocationSearchRequest = new ServicePartnerAllocationSearchRequest();

            if (!StringUtils.isEmpty(states)) {
                servicePartnerAllocationSearchRequest.setStates(Arrays.asList(states.split(",")));
            }
            if (!StringUtils.isEmpty(cities)) {
                servicePartnerAllocationSearchRequest.setCities(Arrays.asList(cities.split(",")));
            }
            if (!StringUtils.isEmpty(pincodes)) {
                servicePartnerAllocationSearchRequest.setPincodes(Arrays.asList(pincodes.split(",")));
            }
            servicePartnerAllocationSearchRequest.setPartnerCode(partnerCode);
            servicePartnerAllocationSearchRequest.setProductCode(productCode);
            servicePartnerAllocationSearchRequest.setServiceType(serviceType);
            PageRequest pageRequest = null;
            if (page != null && page > 0 && size != null && size > 0) {
                page = page - 1;
                pageRequest = new PageRequest(page, size);
            }
            response = allocationMasterService.getServicePartnerAllocation(servicePartnerAllocationSearchRequest, pageRequest);
            response.setStatus(ResponseConstant.SUCCESS);
            response.setMessage(ResponseConstant.SUCCESS);
        } catch (Exception ex) {
            throw new RestResourceException(LogisticResponseCodes.ALLOCATION_MASTER_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }
}
