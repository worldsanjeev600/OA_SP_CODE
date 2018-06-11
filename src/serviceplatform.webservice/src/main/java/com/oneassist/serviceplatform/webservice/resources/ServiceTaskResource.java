package com.oneassist.serviceplatform.webservice.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.ServiceTaskEntity;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.exception.RestResourceException;
import com.oneassist.serviceplatform.commons.repositories.ServiceTaskRepository;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceTaskDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Path("/servicetasks")
@Component
@Api(tags = { "/servicetasks : All About Managing Service Task Info" })
public class ServiceTaskResource extends BaseResource {

    @Autowired
    private ServiceTaskRepository serviceTaskRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getByProdCodeAndTaskTypeAndStatus(@QueryParam("referenceCode") String referenceCode, @QueryParam("taskType") String taskType,
            @ApiParam(value = "Product Variant Id") @QueryParam("productVariantId") Long productVariantId, @ApiParam(value = "Status") @QueryParam("status") String status)
            throws RestResourceException {

        ResponseDto<List<ServiceTaskDto>> response = new ResponseDto<>();
        List<ServiceTaskEntity> serviceTasks = null;
        try {
            if (!StringUtils.isEmpty(referenceCode) && !StringUtils.isEmpty(taskType) && !StringUtils.isEmpty(status)) {
                serviceTasks = serviceTaskRepository.findByReferenceCodeAndTaskTypeAndStatus(referenceCode, taskType, status);
            } else if (!StringUtils.isEmpty(taskType) && !StringUtils.isEmpty(status)) {
                serviceTasks = serviceTaskRepository.findByTaskTypeAndStatus(taskType, status);
            } else if (!StringUtils.isEmpty(productVariantId) && !StringUtils.isEmpty(status)) {
                serviceTasks = serviceTaskRepository.findByProductVariantIdAndStatus(productVariantId, status);
            } else if (!StringUtils.isEmpty(referenceCode) && !StringUtils.isEmpty(status)) {
                serviceTasks = serviceTaskRepository.findByReferenceCodeAndStatus(referenceCode, status);
            } else {
                serviceTasks = serviceTaskRepository.findByStatus(Constants.ACTIVE);
            }
            if (null != serviceTasks && !serviceTasks.isEmpty()) {
                List<ServiceTaskDto> serviceTaskDtos = modelMapper.map(serviceTasks, new TypeToken<List<ServiceTaskDto>>() {
                }.getType());

                response.setStatus(ResponseConstant.SUCCESS);
                response.setData(serviceTaskDtos);
                response.setMessage("Total #" + serviceTaskDtos.size() + " service task(s) fetched successfully");
            } else {
                response.setStatus(ResponseConstant.FAILED);
                response.setMessage("No data found");
                response.setData(null);
            }
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestResponseCodes.SERVICE_DOCUMENT_DELETE_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }
}
