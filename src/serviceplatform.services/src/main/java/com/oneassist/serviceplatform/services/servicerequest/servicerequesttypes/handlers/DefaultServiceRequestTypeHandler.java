package com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers;

import java.util.Date;
import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commands.BaseActionCommand;
import com.oneassist.serviceplatform.commands.dtos.CommandInput;
import com.oneassist.serviceplatform.commands.dtos.CommandResult;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestStatus;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import com.oneassist.serviceplatform.services.servicerequest.actioncommands.ServiceRequestActionCommandFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultServiceRequestTypeHandler extends BaseServiceTypeHandler {

    private final Logger logger = Logger.getLogger(DefaultServiceRequestTypeHandler.class);

    @Autowired
    private ServiceRequestActionCommandFactory serviceRequestActionCommandFactory;

    public DefaultServiceRequestTypeHandler() {
        // TODO: Need to be changed.
        super(ServiceRequestType.HA_AD);
    }

    public DefaultServiceRequestTypeHandler(ServiceRequestType serviceRequestType) {
        super(serviceRequestType);
    }

    @Override
    public ServiceRequestDto createServiceRequest(ServiceRequestDto createServiceRequestDto, ServiceRequestEntity serviceRequestEntity, String requestType) throws BusinessServiceException {

        serviceRequestEntity.setStatus(ServiceRequestStatus.CREATED.getStatus());
        serviceRequestEntity.setCreatedOn(new Date());
        serviceRequestEntity.setModifiedOn(new Date());

        serviceRequestRepository.save(serviceRequestEntity);

        createServiceRequestDto.setServiceRequestId(serviceRequestEntity.getServiceRequestId());

        return createServiceRequestDto;
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
        return a.execute(ci);
    }

    @Override
    public void validateOnServiceRequestCreate(ServiceRequestDto serviceRequestDto) throws BusinessServiceException {
        // TODO Auto-generated method stub
        super.validateOnServiceRequestCreate(serviceRequestDto);
        if (Strings.isNullOrEmpty(serviceRequestDto.getRequestDescription())) {
            inputValidator.populateMandatoryFieldError("requestDescription", errorInfoDtoList);
        }

        if (Strings.isNullOrEmpty(serviceRequestDto.getRefPrimaryTrackingNo())) {
            inputValidator.populateMandatoryFieldError("refPrimaryTrackingNo", errorInfoDtoList);
        }

        if (!errorInfoDtoList.isEmpty()) {
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList, new InputValidationException());
        }
    }

}
