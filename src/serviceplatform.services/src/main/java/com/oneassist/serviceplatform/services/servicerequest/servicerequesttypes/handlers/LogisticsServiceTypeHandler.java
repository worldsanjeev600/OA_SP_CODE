package com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oneassist.serviceplatform.commands.BaseActionCommand;
import com.oneassist.serviceplatform.commands.dtos.CommandInput;
import com.oneassist.serviceplatform.commands.dtos.CommandResult;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import com.oneassist.serviceplatform.services.servicerequest.actioncommands.ServiceRequestActionCommandFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class LogisticsServiceTypeHandler extends BaseServiceTypeHandler {

    @Autowired
    private ServiceRequestActionCommandFactory serviceRequestActionCommandFactory;

    public LogisticsServiceTypeHandler(ServiceRequestType serviceRequestType) {
        super(serviceRequestType);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void validateOnServiceRequestCreate(ServiceRequestDto serviceRequestDto) throws BusinessServiceException {
        // TODO Auto-generated method stub

    }

    @Override
    public ServiceRequestDto createServiceRequest(ServiceRequestDto createServiceRequestDto, ServiceRequestEntity serviceRequestEntity, String requestType) throws JsonProcessingException,
            BusinessServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CommandResult<ServiceResponseDto> updateServiceRequest(ServiceRequestUpdateAction serviceRequestUpdateAction, ServiceRequestDto serviceRequestDto, ServiceRequestEventCode eventCode,
            ServiceRequestEntity serviceRequestEntity) throws Exception {

        // TODO: Step 1: Validation if serviceRequestUpdateAction is applicable for InspectionServiceTypeHandler
        // TODO: Step 2: Validate the events applicable for this handler
        // TODO: Step 3: Validate the list of stages applicable for this handler

        // Based on ServiceRequestUpdateAction it will call different action command
        BaseActionCommand<ServiceRequestDto, ServiceResponseDto> a = serviceRequestActionCommandFactory.getServiceRequestActionCommand(serviceRequestUpdateAction);

        CommandInput<ServiceRequestDto> ci = new CommandInput<>();
        ci.setData(serviceRequestDto);
        ci.setCommandEventCode(eventCode);
        ci.setServiceRequestEntity(serviceRequestEntity);
        return a.execute(ci);
    }

}
