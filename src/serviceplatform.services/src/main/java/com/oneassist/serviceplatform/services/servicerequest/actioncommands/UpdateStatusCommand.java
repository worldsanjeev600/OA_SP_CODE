package com.oneassist.serviceplatform.services.servicerequest.actioncommands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commands.BaseActionCommand;
import com.oneassist.serviceplatform.commands.dtos.CommandInput;
import com.oneassist.serviceplatform.commands.dtos.CommandResult;
import com.oneassist.serviceplatform.commons.datanotification.SRDataNotificationManager;
import com.oneassist.serviceplatform.commons.entities.GenericKeySetEntity;
import com.oneassist.serviceplatform.commons.entities.GenericKeySetValueEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventType;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestStatus;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.mastercache.GenericKeySetCache;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.validators.InputValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;

@Service
public class UpdateStatusCommand extends BaseActionCommand<ServiceRequestDto, ServiceResponseDto> {

	@Autowired
	protected MessageSource messageSource;

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	@Autowired
	private InputValidator inputValidator;

	@Autowired
	private ServiceRequestHelper serviceRequestHelper;

	@Autowired
	private SRDataNotificationManager srDataNotificationManager;

	@Autowired
	private GenericKeySetCache genericKeySetCache;

	private final Logger logger = Logger.getLogger(UpdateStatusCommand.class);

	@Override
	protected CommandResult<ServiceResponseDto> canExecuteCommand(CommandInput<ServiceRequestDto> commandInput) throws BusinessServiceException {

		CommandResult<ServiceResponseDto> commandResult = new CommandResult<>();
		ServiceResponseDto response = new ServiceResponseDto();
		List<ErrorInfoDto> errorInfoList = new ArrayList<>();

		commandResult.setData(response);

		try {

			if (commandInput.getData().getStatus() == null) {
				inputValidator.populateMandatoryFieldError("status", errorInfoList);
			}

			if (Strings.isNullOrEmpty(commandInput.getData().getModifiedBy())) {
				inputValidator.populateMandatoryFieldError("modifiedBy", errorInfoList);
			}

			if (commandInput.getData().getServiceRequestId() == null) {
				inputValidator.populateMandatoryFieldError("serviceRequestId", errorInfoList);
			} else {

				ServiceRequestEntity serviceRequestEntity = serviceRequestRepository.findOne(commandInput.getData().getServiceRequestId());
				if (!serviceRequestEntity.getStatus().equals(commandInput.getData().getStatus())) {

					GenericKeySetEntity genericKeySet = genericKeySetCache.get("SERVICE_REQUEST_STATUS");
					if (genericKeySet == null) {
						inputValidator.populateInvalidRequestError(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_GET_STATUS_DETAILS_FAILED.getErrorCode(), errorInfoList);
					} else {
						List<GenericKeySetValueEntity> genericKeySetValues = genericKeySet.getGenericKeySetValueDetails();
						if(genericKeySetValues != null & !genericKeySetValues.isEmpty()){
							boolean validStatusCode = false;
							for(GenericKeySetValueEntity genericKeySetValueEntity:genericKeySetValues){
								if(genericKeySetValueEntity.getKey().equals(commandInput.getData().getStatus())){
									validStatusCode = true;
									break;
								}
							}
							if(!validStatusCode){
								logger.error("Invalid Status Code for updateStatus!! "+ commandInput.getData().getStatus());
								inputValidator.populateInvalidRequestError(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_INVALID_STATUS.getErrorCode(), errorInfoList);
							}
						}
						if (commandInput.getData().getStatus().equals(ServiceRequestStatus.INPROGRESS.getStatus())) {

							commandInput.getData().setServiceRequestType(serviceRequestHelper.getServiceReqeustType(serviceRequestEntity.getServiceRequestTypeId()));
							if (commandInput.getData().getServiceRequestType().equals(ServiceRequestType.HA_EW.getRequestType())
									|| commandInput.getData().getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())
									|| commandInput.getData().getServiceRequestType().equals(ServiceRequestType.HA_BD.getRequestType())) {
								if (serviceRequestEntity.getStatus().equals(ServiceRequestStatus.ONHOLD.getStatus()) && serviceRequestEntity.getWorkflowStageStatus() != null
										&& serviceRequestEntity.getWorkflowStageStatus().equals("VS")) {
									logger.info("Valid Status");

								} else {
									inputValidator.populateInvalidRequestError(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_VISIT_NOT_SCHEDULED.getErrorCode(), errorInfoList);
								}
							}
						}
					}
				} else {
					inputValidator.populateInvalidRequestError(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_DUPLICATE_STATUS_UPDATE.getErrorCode(), errorInfoList);
				}
			}

			if (!errorInfoList.isEmpty()) {
				throw new InputValidationException();
			}

			commandResult.setCanExecute(true);
		} catch (InputValidationException ex) {

			commandResult.setCanExecute(false);            
			throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList, ex);
		}

		return commandResult;
	}

	@Override
	protected CommandResult<ServiceResponseDto> executeCommand(CommandInput<ServiceRequestDto> commandInput) throws BusinessServiceException {

		CommandResult<ServiceResponseDto> commandResult = new CommandResult<>();
		ServiceRequestDto serviceRequestUpdateDto = commandInput.getData();
		ServiceResponseDto response = new ServiceResponseDto();
		commandResult.setData(response);

		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

		int isUpdated = 0;

		if (!Strings.isNullOrEmpty(serviceRequestUpdateDto.getRefPrimaryTrackingNo())) {

			isUpdated = serviceRequestRepository.updateServiceRequestStatusByRefNum(
					serviceRequestUpdateDto.getStatus(), 
					currentTimestamp, 
					serviceRequestUpdateDto.getModifiedBy(),
					serviceRequestUpdateDto.getRefPrimaryTrackingNo());            
		} else if (null != serviceRequestUpdateDto.getServiceRequestId()) {

			isUpdated = serviceRequestRepository.updateServiceRequestStatus(
					serviceRequestUpdateDto.getStatus(), 
					currentTimestamp, 
					serviceRequestUpdateDto.getModifiedBy(),
					serviceRequestUpdateDto.getServiceRequestId());
		}

		if (isUpdated == 1) {
			srDataNotificationManager.notify(
					DataNotificationEventType.UPDATED, 
					null, 
					ServiceRequestUpdateAction.SERVICE_REQUEST_STATUS, 
					serviceRequestUpdateDto.getServiceRequestId());
		} else {
			throw new BusinessServiceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode());
		}

		return commandResult;
	}
}
