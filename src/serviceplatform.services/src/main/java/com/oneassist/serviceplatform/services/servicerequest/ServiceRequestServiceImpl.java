package com.oneassist.serviceplatform.services.servicerequest;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Comment;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.oneassist.communicationgateway.enums.CommunicationGatewayEventCode;
import com.oneassist.serviceplatform.commands.dtos.CommandResult;
import com.oneassist.serviceplatform.commons.cache.PinCodeMasterCache;
import com.oneassist.serviceplatform.commons.cache.ProductMasterCache;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.datanotification.SRDataNotificationForCRMManager;
import com.oneassist.serviceplatform.commons.datanotification.SRDataNotificationManager;
import com.oneassist.serviceplatform.commons.entities.DocTypeConfigDetailEntity;
import com.oneassist.serviceplatform.commons.entities.DocTypeMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceAddressEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceDocumentEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestAssetEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntityDocumentEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestStageMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTransitionConfigEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.entities.ShipmentEntity;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventType;
import com.oneassist.serviceplatform.commons.enums.FileType;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.Recipient;
import com.oneassist.serviceplatform.commons.enums.ServiceDocumentType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestStatus;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.enums.WorkflowStage;
import com.oneassist.serviceplatform.commons.enums.WorkflowStageStatus;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.mastercache.DocTypeConfigDetailCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestDocumentTypeMasterCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestStageMasterCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTransitionConfigCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTypeMasterCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceTaskMasterCache;
import com.oneassist.serviceplatform.commons.mastercache.TaskListForAllDeploymentCache;
import com.oneassist.serviceplatform.commons.mongo.repositories.IMongoStorageDao;
import com.oneassist.serviceplatform.commons.proxies.CommunicationGatewayProxy;
import com.oneassist.serviceplatform.commons.proxies.OasysAdminProxy;
import com.oneassist.serviceplatform.commons.proxies.OasysProxy;
import com.oneassist.serviceplatform.commons.repositories.LogisticShipmentRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceAddressRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceDocumentRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestAssetRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestTypeMstRepository;
import com.oneassist.serviceplatform.commons.specifications.ServiceAddressSpecifications;
import com.oneassist.serviceplatform.commons.specifications.ServiceRequestAssetSpecifications;
import com.oneassist.serviceplatform.commons.specifications.ServiceRequestSpecifications;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.validators.ServiceRequestValidator;
import com.oneassist.serviceplatform.commons.workflowmanager.IWorkflowManager;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.activiti.ActivitiHistoryCommentDto;
import com.oneassist.serviceplatform.contracts.dtos.activiti.ActivitiHistoryTaskInstanceDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.AssetClaimEligibilityDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.AssetDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CloseRepairSREventDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CostToServiceDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DocumentRejection;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ImageStorageReference;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Inspection;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.InspectionAssessment;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.OASYSCustMemDetails;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.OASYSMembershipDetails;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.PaymentDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Pendency;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestSearchDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceTaskDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.SpareParts;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WHCInspectionSREventDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestAssetRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestAssetResponseDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.request.AssigneeRepairCostRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.request.CompleteWHCInspectionRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceRequestAuthorizationResponseDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.DocumentDownloadRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceAddressDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceDocumentDto;
import com.oneassist.serviceplatform.contracts.response.DownloadDocumentDto;
import com.oneassist.serviceplatform.contracts.response.UserProfileData;
import com.oneassist.serviceplatform.contracts.response.base.PageResponseDto;
import com.oneassist.serviceplatform.externalcontracts.ClaimLifecycleEvent;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;
import com.oneassist.serviceplatform.mappers.EntityToDtoMapper;
import com.oneassist.serviceplatform.services.document.IServiceRequestDocumentService;
import com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers.BaseServiceTypeHandler;
import com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers.ServiceTypeHandlerFactory;
import com.oneassist.serviceplatform.services.shipment.IShipmentService;

@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ServiceRequestServiceImpl implements IServiceRequestService {

	private final Logger logger = Logger.getLogger(ServiceRequestServiceImpl.class);

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	@Autowired
	private ServiceRequestValidator serviceRequestValidator;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ServiceRequestTypeMasterCache serviceRequestTypeMasterCache;

	@Autowired
	private ServiceRequestDocumentTypeMasterCache serviceRequestDocumentTypeMasterCache;

	@Autowired
	private PinCodeMasterCache pinCodeMasterCache;

	@Autowired
	private ServiceDocumentRepository serviceDocumentRepository;

	@Autowired
	private ServiceRequestHelper serviceRequestHelper;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private ServiceRequestAssetRepository serviceRequestAssetRepository;

	@Autowired
	private ServiceAddressRepository serviceAddressRepository;

	@Autowired
	private TaskListForAllDeploymentCache taskListForAllDeploymentCache;

	@Autowired
	private IWorkflowManager workflowManager;

	@Autowired
	@Qualifier("mongoStorageDao")
	private IMongoStorageDao mongoStorageDao;

	@Autowired
	ProductMasterCache productMasterCache;

	@Autowired
	private ServiceTypeHandlerFactory serviceTypeHandlerFactory;

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private SRDataNotificationManager srDataNotificationManager;

	@Autowired
	private SRDataNotificationForCRMManager sRDataNotificationForCRMManager;

	@Autowired
	private EntityToDtoMapper entityToDtoMapper;

	@Autowired
	private ServiceRequestTypeMstRepository serviceRequestTypeMstRepository;

	@Value("${ADVICE_AMOUNT}")
	private String adviceAmount;

	@Value("${ADVICE_CHARGE_ID}")
	private String adviceChargeId;

	private static final String REQUIRE_ADDITIONAL_INFO = "Y";
	private static final String TECHNICIAN_PROFILE_REQUIRED = "Y";

	@Value("${publishCloseRepairSR}")
	private String publishCloseRepairSR;

	@Autowired
	private CommunicationGatewayProxy communicationGatewayProxy;

	@Autowired
	protected ServiceRequestStageMasterCache serviceRequestStageMasterCache;

	@Autowired
	private OasysProxy oasysProxy;

	@Autowired
	private OasysAdminProxy oasysAdminProxy;

	@Autowired
	IServiceRequestDocumentService serviceRequestDocumentService;

	@Autowired
	private LogisticShipmentRepository shipmentRepository;

	private static final String ACTIVITI_YES_FLAG = "Yes";

	private static final String ACTIVITI_NO_FLAG = "No";

	@Autowired
	private ServiceTaskMasterCache serviceTaskMasterCache;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	protected ServiceRequestTransitionConfigCache serviceRequestTransitionConfigCache;

	@Autowired
	private DocTypeConfigDetailCache docTypeConfigDetailCache;

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
	public ServiceRequestDto createServiceRequest(ServiceRequestDto serviceRequestDto)
			throws BusinessServiceException, JsonProcessingException {
		ServiceRequestDto response = null;

		try {
			ServiceRequestEntity serviceRequestEntity = modelMapper.map(serviceRequestDto,
					new TypeToken<ServiceRequestEntity>() {
					}.getType());
			ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = serviceRequestTypeMasterCache
					.get(serviceRequestDto.getServiceRequestType());

			if (serviceRequestTypeMstEntity != null && serviceRequestTypeMstEntity.getServiceRequestType() != null) {
				serviceRequestEntity.setServiceRequestTypeId(serviceRequestTypeMstEntity.getServiceRequestTypeId());
				ServiceRequestType serviceRequestType = ServiceRequestType
						.getServiceRequestType(serviceRequestTypeMstEntity.getServiceRequestType());
				BaseServiceTypeHandler serviceRequestTypeHandler = serviceTypeHandlerFactory
						.getServiceRequestTypeHandler(serviceRequestType);

				response = serviceRequestTypeHandler.doCreateServiceRequest(serviceRequestDto, serviceRequestEntity,
						serviceRequestTypeMstEntity.getServiceRequestType());
				Long serviceRequestId = serviceRequestDto.getServiceRequestId();
				serviceRequestDto = serviceRequestHelper.convertObject(serviceRequestEntity, ServiceRequestDto.class);

				if (serviceRequestDto.getServiceRequestId() == null || serviceRequestDto.getServiceRequestId() == 0) {
					serviceRequestDto.setServiceRequestId(serviceRequestId);
				}

				serviceRequestDto.setServiceRequestType(serviceRequestType.getRequestType());
			} else {
				throw new BusinessServiceException(
						ServiceRequestResponseCodes.INVALID_SERVICE_REQUEST_TYPE.getErrorCode(),
						new InputValidationException());
			}
		} catch (Exception ex) {
			serviceRequestDto.setMessage(ex.getMessage());
			throw ex;
		} finally {
			srDataNotificationManager.notify(DataNotificationEventType.NEW, serviceRequestDto);
			sRDataNotificationForCRMManager.notify(DataNotificationEventType.NEW, serviceRequestDto);
		}

		return response;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
	public ServiceResponseDto performServiceAction(ServiceRequestUpdateAction updateAction,
			ServiceRequestDto serviceRequestUpdateDto, ServiceRequestEventCode updateEventCode) throws Exception {

		logger.info(">>> In ServiceRequestServiceImpl: Update Service Request(String, serviceRequestUpdateDto, String)"
				+ updateAction + serviceRequestUpdateDto.getServiceRequestId() + updateEventCode);
		ServiceRequestEntity serviceRequestEntity = serviceRequestRepository
				.findOne(serviceRequestUpdateDto.getServiceRequestId());
		if (serviceRequestEntity == null) {
			logger.error("Invalid Service Request Id");
			throw new BusinessServiceException(
					ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_INVALID_SERVICE_REQUEST_ID.getErrorCode(),
					new InputValidationException());
		} else {
			String serviceRequestTypeStr = null;
			Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMstEntityMap = serviceRequestTypeMasterCache
					.getAll();
			ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = null;
			for (Map.Entry<String, ServiceRequestTypeMstEntity> entry : serviceRequestTypeMstEntityMap.entrySet()) {
				serviceRequestTypeMstEntity = entry.getValue();
				if (serviceRequestTypeMstEntity.getServiceRequestTypeId().longValue() == serviceRequestEntity
						.getServiceRequestTypeId().longValue()) {
					serviceRequestTypeStr = entry.getKey();
					break;
				}
			}
			ServiceRequestType serviceRequestType = ServiceRequestType.getServiceRequestType(serviceRequestTypeStr);
			serviceRequestUpdateDto.setServiceRequestType(serviceRequestType.getRequestType());
			if (updateAction.equals(ServiceRequestUpdateAction.CLOSE_SERVICE_REQUEST)) {
				updateEventCode = ServiceRequestEventCode.HA_CLAIMS_CLOSE_SERVICE_REQUEST;
			} else if (updateAction.equals(ServiceRequestUpdateAction.UPDATE_SERVICE_REQUEST_ON_EVENT)
					&& (ServiceRequestEventCode.UPDATE_STAGE_COMPLETE.equals(updateEventCode)
							|| ServiceRequestEventCode.UPDATE_STAGE_INCOMPLETE.equals(updateEventCode))) {
				populateNextStage(serviceRequestUpdateDto, serviceRequestEntity, updateEventCode);
			}

			BaseServiceTypeHandler serviceRequestTypeHandler = serviceTypeHandlerFactory
					.getServiceRequestTypeHandler(serviceRequestType);
			CommandResult<ServiceResponseDto> response = serviceRequestTypeHandler.doUpdateServiceRequest(updateAction,
					serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
			if (updateAction.equals(ServiceRequestUpdateAction.RESCHEDULE_SERVICE)
					&& serviceRequestType.equals(ServiceRequestType.HA_AD) && WorkflowStage.VERIFICATION
							.getWorkflowStageCode().equals(serviceRequestEntity.getWorkflowStage())) {
				updateEventCode = ServiceRequestEventCode.CUSTOMER_RESCHEDULE_VISIT_POST_DEDUCTIBLE_PAYMENT;
			}
			completeActivitiTask(serviceRequestUpdateDto, updateEventCode);

			return response.getData();
		}
	}

	private void populateNextStage(ServiceRequestDto serviceRequestUpdateDto, ServiceRequestEntity serviceRequestEntity,
			ServiceRequestEventCode updateAction) {
		String currentStage = serviceRequestEntity.getWorkflowStage();
		WorkflowStage stage = WorkflowStage.getWorkflowStageByCode(currentStage);
		switch (stage) {
		case DOCUMENT_UPLOAD:
			List<ErrorInfoDto> errorInfoList = serviceRequestValidator.validateMandatoryDocuments(serviceRequestEntity);
			if (CollectionUtils.isEmpty(errorInfoList)) {
				String eventName = (updateAction.equals(ServiceRequestEventCode.UPDATE_STAGE_COMPLETE)
						? (stage.getWorkflowTaskName().toUpperCase().replace(" ", "_")) + "_COMPLETED" : "_FAILED");

				List<ServiceRequestTransitionConfigEntity> transitionEntities = serviceRequestTransitionConfigCache
						.get(serviceRequestUpdateDto.getServiceRequestType());
				if (!CollectionUtils.isEmpty(transitionEntities)) {
					for (ServiceRequestTransitionConfigEntity transition : transitionEntities) {
						if (currentStage.equalsIgnoreCase(transition.getTransitionFromStage())
								&& eventName.equalsIgnoreCase(transition.getEventName())) {
							serviceRequestUpdateDto.setWorkflowStage(transition.getTransitionToStage());
							serviceRequestUpdateDto.setWorkflowStageStatus(transition.getTransitionToStageStatus());
							break;
						}
					}
				}

				try {
					WorkflowData workflowData = serviceRequestHelper
							.getWorkflowDataByServiceRequest(serviceRequestEntity);
					workflowData.getVerification()
							.setDescription(messageSource.getMessage(String.valueOf("PE_VR_VP_MSG"), null, null));
					serviceRequestEntity.setWorkflowData(objectMapper.writeValueAsString(workflowData));
				} catch (Exception e) {
					logger.error(
							"Error updating verfication description: " + serviceRequestUpdateDto.getServiceRequestId());
				}
				serviceRequestUpdateDto.setPendency(null);
			} else {
				throw new BusinessServiceException(
						ServiceRequestResponseCodes.DOCUMENT_UPLOAD_COMPLETION_VALIDATION_FAILED.getErrorCode(),
						errorInfoList, new InputValidationException());
			}

		default:
			break;

		}
	}

	private void completeActivitiTask(ServiceRequestDto serviceRequestUpdateDto, ServiceRequestEventCode eventCode)
			throws Exception {

		logger.error("Inside completeActivitiTask::" + eventCode);
		CommunicationGatewayEventCode commEventCode = null;
		HashMap<String, Object> additionalAttributes = null;

		try {
			switch (eventCode) {
			case TECHNICIAN_AT_LOCATION_AND_BEGINS_SERVICE: {
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.VISIT);

				commEventCode = CommunicationGatewayEventCode.SP_TECH_START_SERVICE;
			}
				break;
			case COMPLETED_AFTER_REPAIR_ASSESSMENT: {// its when technician
														// completed repair
														// assessment, & then
														// customer denying
														// service

				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IC_APPROVAL, WorkflowStageStatus.REJECTED.getWorkflowStageStatus());
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_CUSOTMER_OPT_SELFREPAIR, Constants.YES_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_CLAUSE_PASSED, ACTIVITI_YES_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_REPAIR_COMPLETED, Constants.NO_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_CUSTOMER_ASKS_REFUND, Constants.NO_FLAG);

				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.REPAIR_ASSESSMENT);

				CloseRepairSREventDto closeRepairSREventDto = new CloseRepairSREventDto();
				closeRepairSREventDto.setSrNo(Long.valueOf(serviceRequestUpdateDto.getRefPrimaryTrackingNo()));
				closeRepairSREventDto.setSrStatus(ServiceRequestStatus.CLOSEDUNRESOLVED.getValue());
				oasysProxy.publishCloseRepairSR(publishCloseRepairSR, closeRepairSREventDto);
				commEventCode = CommunicationGatewayEventCode.SP_CUSTOMER_DENIED_PAYMENT;
			}
				break;
			case IC_APPROVAL_WAITING:
			case IC_APPROVAL_WAITING_WITH_SPARE_NEEDED:
			case IC_APPROVAL_WAITING_WITH_TRANSPORTATION_NEEDED: {
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IC_APPROVAL, WorkflowStageStatus.REJECTED.getWorkflowStageStatus());
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_CUSOTMER_OPT_SELFREPAIR, Constants.NO_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_CLAUSE_PASSED, ACTIVITI_NO_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_REPAIR_COMPLETED, Constants.NO_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_CUSTOMER_ASKS_REFUND, Constants.NO_FLAG);
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.REPAIR_ASSESSMENT);
				if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_EW.getRequestType())) {
					commEventCode = CommunicationGatewayEventCode.SP_IC_APPROVAL_WAITING;
				}
			}
				break;
			case IC_DECISION_APPROVED: {
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_CLAUSE_PASSED, ACTIVITI_YES_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IC_APPROVAL, WorkflowStageStatus.APPROVED.getWorkflowStageStatus());
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.IS_LAST_STAGE_IC_DECISION, Constants.YES_FLAG);

				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.IC_DECISION);
				if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_EW.getRequestType())) {
					commEventCode = CommunicationGatewayEventCode.SP_IC_DECISION_APPROVED;
				}
			}
				break;
			case IC_DECISION_REJECTED:
			case IC_DECISION_REJECTED_SELF_REPAIR: {
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IC_APPROVAL, WorkflowStageStatus.REJECTED.getWorkflowStageStatus());
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.IC_DECISION);
				CloseRepairSREventDto closeRepairSREventDto = new CloseRepairSREventDto();
				closeRepairSREventDto.setSrNo(Long.valueOf(serviceRequestUpdateDto.getRefPrimaryTrackingNo()));
				closeRepairSREventDto.setSrStatus(ServiceRequestStatus.CLOSEDREJECTED.getValue());
				oasysProxy.publishCloseRepairSR(publishCloseRepairSR, closeRepairSREventDto);

				if (serviceRequestUpdateDto.getServiceRequestType() != null && serviceRequestUpdateDto
						.getServiceRequestType().equals(ServiceRequestType.HA_BD.getRequestType())) {
					commEventCode = CommunicationGatewayEventCode.SP_BD_IC_DECISION_REJECTED;
				} else {
					commEventCode = CommunicationGatewayEventCode.SP_IC_DECISION_REJECTED;
				}
			}
				break;
			case IC_DECISION_BER_APPROVED: {
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IC_APPROVAL, WorkflowStageStatus.BER_APPROVED.getWorkflowStageStatus());
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.IC_DECISION);

				commEventCode = CommunicationGatewayEventCode.SP_IC_DECISION_BER;
			}
				break;
			case IC_DECISION_BER_APPROVED_SELF_REPAIR: {
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IC_APPROVAL, WorkflowStageStatus.BER_APPROVED.getWorkflowStageStatus());
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.IC_DECISION);
				CloseRepairSREventDto closeRepairSREventDto = new CloseRepairSREventDto();
				closeRepairSREventDto.setSrNo(Long.valueOf(serviceRequestUpdateDto.getRefPrimaryTrackingNo()));
				closeRepairSREventDto.setSrStatus(ServiceRequestStatus.CLOSEDRESOLVED.getValue());
				oasysProxy.publishCloseRepairSR(publishCloseRepairSR, closeRepairSREventDto);

			}
				break;
			case APPLIANCE_PICKED_FOR_BER: {
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_CUSOTMER_OPT_SELFREPAIR, Constants.NO_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_CLAUSE_PASSED, ACTIVITI_YES_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_REPAIR_COMPLETED, Constants.NO_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_CUSTOMER_ASKS_REFUND, Constants.NO_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IC_APPROVAL, WorkflowStageStatus.BER_APPROVED.getWorkflowStageStatus());
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.REPAIR_ASSESSMENT);
				CloseRepairSREventDto closeRepairSREventDto = new CloseRepairSREventDto();
				closeRepairSREventDto.setSrNo(Long.valueOf(serviceRequestUpdateDto.getRefPrimaryTrackingNo()));
				closeRepairSREventDto.setSrStatus(ServiceRequestStatus.CLOSEDRESOLVED.getValue());
				oasysProxy.publishCloseRepairSR(publishCloseRepairSR, closeRepairSREventDto);
			}

				break;
			case REPAIR_SUCCESSFUL: {
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_CLAUSE_PASSED, ACTIVITI_YES_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_CUSOTMER_OPT_SELFREPAIR, Constants.NO_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_REPAIR_COMPLETED, Constants.YES_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_CUSTOMER_ASKS_REFUND, Constants.NO_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IC_APPROVAL, WorkflowStageStatus.REJECTED.getWorkflowStageStatus());
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.REPAIR_ASSESSMENT);

				CloseRepairSREventDto closeRepairSREventDto = new CloseRepairSREventDto();
				closeRepairSREventDto.setSrNo(Long.valueOf(serviceRequestUpdateDto.getRefPrimaryTrackingNo()));
				closeRepairSREventDto.setSrStatus(ServiceRequestStatus.CLOSEDRESOLVED.getValue());
				oasysProxy.publishCloseRepairSR(publishCloseRepairSR, closeRepairSREventDto);

				if (serviceRequestUpdateDto.getServiceRequestType() != null && serviceRequestUpdateDto
						.getServiceRequestType().equals(ServiceRequestType.HA_BD.getRequestType())) {
					commEventCode = CommunicationGatewayEventCode.SP_BD_REPAIR_SUCCESSFUL;
				} else {
					commEventCode = CommunicationGatewayEventCode.SP_REPAIR_SUCCESSFUL;
				}
			}

				break;
			case CUSTOMER_ASKS_REFUND:

				commEventCode = CommunicationGatewayEventCode.SP_CUSTOMER_ASKS_REFUND;
				break;

			case APPLIANCE_PICKED_FOR_REFUND: {
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IC_APPROVAL, WorkflowStageStatus.REJECTED.getWorkflowStageStatus());
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_CLAUSE_PASSED, ACTIVITI_YES_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_CUSOTMER_OPT_SELFREPAIR, Constants.NO_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_REPAIR_COMPLETED, Constants.NO_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_CUSTOMER_ASKS_REFUND, Constants.YES_FLAG);
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.REPAIR_ASSESSMENT);
				CloseRepairSREventDto closeRepairSREventDto = new CloseRepairSREventDto();
				closeRepairSREventDto.setSrNo(Long.valueOf(serviceRequestUpdateDto.getRefPrimaryTrackingNo()));
				closeRepairSREventDto.setSrStatus(ServiceRequestStatus.CLOSEDUNRESOLVED.getValue());
				oasysProxy.publishCloseRepairSR(publishCloseRepairSR, closeRepairSREventDto);

			}
				break;
			case TRANSPORTATION_REQUIRED: {
				commEventCode = CommunicationGatewayEventCode.SP_TRANSPORTATION_REQUIRED;
			}
				break;
			case SPARE_PART_NOT_AVAILABLE: {
				additionalAttributes = new HashMap<String, Object>();
				additionalAttributes.put(Constants.SPARE_PART_ATTRIBUTE_NAME,
						getSpareParts(serviceRequestUpdateDto.getServiceRequestId(),
								serviceRequestUpdateDto.getRefPrimaryTrackingNo()));
				// communicationGatewayProxy.sendCommunication(serviceRequestUpdateDto,
				// CommunicationGatewayEventCode.SP_SPARE_NOT_AVAILABLE,
				// additionalAttributes);
				commEventCode = CommunicationGatewayEventCode.SP_SPARE_NOT_AVAILABLE;
			}
				break;
			case SPARE_PART_AVAILABLE: {
				final SimpleDateFormat formatter = new SimpleDateFormat(Constants.SERVICE_SCHEDULE_DATETIME_FORMAT);
				final String date = formatter.format(new Date());
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.SPARE_AVAILABLE_DATETIME, date);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.IS_LAST_STAGE_IC_DECISION, Constants.NO_FLAG);
				additionalAttributes = new HashMap<String, Object>();
				additionalAttributes.put(Constants.SPARE_PART_ATTRIBUTE_NAME,
						getSpareParts(serviceRequestUpdateDto.getServiceRequestId(),
								serviceRequestUpdateDto.getRefPrimaryTrackingNo()));
				commEventCode = CommunicationGatewayEventCode.SP_SPARE_AVAILABLE;
			}
				break;
			case ESTIMATED_INVOICE_UPLOAD: {
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.VISIT);
			}
				break;
			case ESTIMATED_INVOICE_VERIFICATION_SUCCESS: {
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_ESTIMATEDINVOICE_VERIFIED, Constants.YES_FLAG);
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.IC_DOC);
			}
				break;
			case ESTIMATED_INVOICE_VERIFICATION_FAIL: {
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_ESTIMATEDINVOICE_VERIFIED, Constants.NO_FLAG);
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.IC_DOC);
			}
				break;
			case IC_DECISION_APPROVED_SELF_REPAIR: {
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IC_APPROVAL, WorkflowStageStatus.APPROVED.getWorkflowStageStatus());
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.IC_DECISION);
			}
				break;
			case COMPLETE_CLAIM_SETTLEMENT: {
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.CLAIM_SETTLEMENT);
				commEventCode = CommunicationGatewayEventCode.SP_CLOSE_SR;
			}
				break;
			case HA_BD_ACCIDENTAL_DAMAGE: {
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_CLAUSE_PASSED, ACTIVITI_YES_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_CUSOTMER_OPT_SELFREPAIR, Constants.NO_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_REPAIR_COMPLETED, Constants.NO_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_CUSTOMER_ASKS_REFUND, Constants.NO_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_ACCIDENTAL_DAMAGE, Constants.YES_FLAG);
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.REPAIR_ASSESSMENT);
				CloseRepairSREventDto closeRepairSREventDto = new CloseRepairSREventDto();
				closeRepairSREventDto.setSrNo(Long.valueOf(serviceRequestUpdateDto.getRefPrimaryTrackingNo()));
				closeRepairSREventDto.setSrStatus(ServiceRequestStatus.CLOSEDUNRESOLVED.getValue());
				oasysProxy.publishCloseRepairSR(publishCloseRepairSR, closeRepairSREventDto);
				commEventCode = CommunicationGatewayEventCode.SP_BD_FOR_AD;
			}
				break;
			case COMPLETE_DOCUMENT_UPLOAD:
			case COMPLETE_DOCUMENT_UPLOAD_SELFREPAIR: {
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.DOCUMENT_UPLOAD);
				if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_EW.getRequestType())) {
					commEventCode = CommunicationGatewayEventCode.SP_COMPLETE_DOCUMENT_UPLOAD;
				}
			}
				break;
			case VERIFICATION_SUCCESSFUL:
			case INVOICE_VERIFICATION_SUCCESS:
			case INVOICE_VERIFICATION_SUCCESS_SELFREPAIR: {
				if (serviceRequestUpdateDto.getServiceRequestType() != null && !serviceRequestUpdateDto
						.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
					workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
							Constants.WORKFLOW_DOC_VERIFICATION_ACTIVITI_VAR,
							WorkflowStageStatus.APPROVED.getWorkflowStageStatus());
					workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
							Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE,
							serviceRequestUpdateDto.getModifiedBy(), WorkflowStage.VERIFICATION);
					if (serviceRequestUpdateDto.getServiceRequestType()
							.equals(ServiceRequestType.HA_EW.getRequestType())) {
						commEventCode = CommunicationGatewayEventCode.SP_VERIFICATION_SUCCESSFUL;
					}
				}
			}

				break;
			case CUSTOMER_RESCHEDULE_VISIT_POST_DEDUCTIBLE_PAYMENT: {
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.WORKFLOW_DOC_VERIFICATION_ACTIVITI_VAR,
						WorkflowStageStatus.APPROVED.getWorkflowStageStatus());
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.VERIFICATION);
			}
				break;
			case INVOICE_VERIFICATION_FAIL:
			case INVOICE_VERIFICATION_FAIL_SELFREPAIR:
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.WORKFLOW_DOC_VERIFICATION_ACTIVITI_VAR,
						WorkflowStageStatus.REJECTED.getWorkflowStageStatus());
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.VERIFICATION);
				break;
			case HA_CLAIMS_CLOSE_SERVICE_REQUEST: {
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IC_APPROVAL, WorkflowStageStatus.REJECTED.getWorkflowStageStatus());
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_CLAUSE_PASSED, ACTIVITI_YES_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_CUSOTMER_OPT_SELFREPAIR, Constants.NO_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_IS_REPAIR_COMPLETED, Constants.NO_FLAG);
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_CUSTOMER_ASKS_REFUND, Constants.NO_FLAG);

				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.ACTIVITI_VAR_CLOSE_SERVICE_REQUEST, Constants.YES_FLAG);
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						serviceRequestUpdateDto.getRemarks(), serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.CLOSE_SERVICE_REQEUST);

				CloseRepairSREventDto closeRepairSREventDto = new CloseRepairSREventDto();
				closeRepairSREventDto.setSrNo(Long.valueOf(serviceRequestUpdateDto.getRefPrimaryTrackingNo()));
				closeRepairSREventDto.setSrStatus(ServiceRequestStatus.CLOSEDUNRESOLVED.getValue());
				oasysProxy.publishCloseRepairSR(publishCloseRepairSR, closeRepairSREventDto);
				commEventCode = CommunicationGatewayEventCode.SP_CLOSE_SR;

				break;
			}

			case VERIFICATION_UNSUCCESSFUL: {
				workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(),
						Constants.WORKFLOW_DOC_VERIFICATION_ACTIVITI_VAR,
						WorkflowStageStatus.VERIFICATION_UNSUCCESSFUL.getWorkflowStageStatus());
				workflowManager.completeActivitiTask(Long.parseLong(serviceRequestUpdateDto.getWorkflowProcessId()),
						Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, serviceRequestUpdateDto.getModifiedBy(),
						WorkflowStage.VERIFICATION);

				break;
			}

			default:
				logger.info("No valid event code to update activiti.");
			}// end of switch

			if (commEventCode != null) {
				communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestUpdateDto, commEventCode,
						additionalAttributes);
			}
		} catch (Exception ex) {
			logger.info("Exception in complete activiti for " + serviceRequestUpdateDto.getServiceRequestId(), ex);

			throw ex;
		}
	}

	public List<ActivitiHistoryTaskInstanceDto> getAllTasks(ServiceRequestDto serviceRequestDto,
			String processInstanceId) {

		List<ActivitiHistoryTaskInstanceDto> activitiHistoryTaskInstanceDtos = new ArrayList<>();
		String processDefKey = serviceRequestDto.getWorkflowProcDefKey();

		if (processInstanceId != null && processDefKey != null) {
			Map<String, List<String>> taskVersionNameMap = taskListForAllDeploymentCache.getAll();
			List<String> taskNameList = taskVersionNameMap.get(processDefKey);
			List<String> toRemoveTaskNames = new ArrayList<String>();
			List<ServiceRequestStageMstEntity> serviceReqeustStageMasterList = serviceRequestStageMasterCache
					.get(serviceRequestDto.getServiceRequestType());
			Map<String, Object> stageNameOrderMap = new HashMap();
			if (serviceReqeustStageMasterList != null) {
				for (ServiceRequestStageMstEntity serviceRequestStageMstEntity : serviceReqeustStageMasterList) {
					stageNameOrderMap.put(serviceRequestStageMstEntity.getStageName(),
							serviceRequestStageMstEntity.getStageOrder());
				}
			}
			List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
					.processInstanceId(processInstanceId).list();
			String isCurrentStageClaimSettlement = WorkflowStage.CLAIM_SETTLEMENT.getWorkflowTaskName();
			Date maxEndTime = null;
			String completedUserId = null;
			boolean isActivitiCompleted = true;
			for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
				ActivitiHistoryTaskInstanceDto activitiHistoryTaskInstanceDto = new ActivitiHistoryTaskInstanceDto();
				activitiHistoryTaskInstanceDto
						.setStageOrderId((Long) stageNameOrderMap.get(historicTaskInstance.getName()));
				activitiHistoryTaskInstanceDto.setProcessInstanceId(processInstanceId);
				activitiHistoryTaskInstanceDto.setAssignee(historicTaskInstance.getAssignee());
				activitiHistoryTaskInstanceDto.setTaskName(historicTaskInstance.getName());
				activitiHistoryTaskInstanceDto.setStartTime(historicTaskInstance.getStartTime());
				activitiHistoryTaskInstanceDto.setComments(getComments(historicTaskInstance.getId()));
				toRemoveTaskNames.add(historicTaskInstance.getName());

				if (historicTaskInstance.getEndTime() == null) {
					activitiHistoryTaskInstanceDto.setStatus(Constants.INPROGRESS);
					activitiHistoryTaskInstanceDto.setLiveStatus(serviceRequestDto.getWorkflowStageStatus());
					isCurrentStageClaimSettlement = historicTaskInstance.getName();
					isActivitiCompleted = false;
				} else {
					activitiHistoryTaskInstanceDto.setEndTime(historicTaskInstance.getEndTime());
					activitiHistoryTaskInstanceDto.setStatus(Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE);
					activitiHistoryTaskInstanceDto.setLiveStatus(Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE);
					if (maxEndTime == null && !historicTaskInstance.getName()
							.equalsIgnoreCase(WorkflowStage.CLAIM_SETTLEMENT.getWorkflowTaskName())) {
						maxEndTime = historicTaskInstance.getEndTime();
					}

					if (maxEndTime != null && historicTaskInstance.getEndTime().after(maxEndTime)
							&& !historicTaskInstance.getName()
									.equalsIgnoreCase(WorkflowStage.CLAIM_SETTLEMENT.getWorkflowTaskName())) {
						maxEndTime = historicTaskInstance.getEndTime();
						completedUserId = historicTaskInstance.getAssignee();
					}
				}

				activitiHistoryTaskInstanceDtos.add(activitiHistoryTaskInstanceDto);
			}

			ActivitiHistoryTaskInstanceDto activitiHistoryTaskInstanceDto = new ActivitiHistoryTaskInstanceDto();
			activitiHistoryTaskInstanceDto
					.setStageOrderId((Long) stageNameOrderMap.get(WorkflowStage.COMPLETED.getWorkflowTaskName()));
			activitiHistoryTaskInstanceDto.setProcessInstanceId(processInstanceId);
			activitiHistoryTaskInstanceDto.setTaskName(WorkflowStage.COMPLETED.getWorkflowTaskName());
			long currentStageOrderId = (Long) stageNameOrderMap.get(isCurrentStageClaimSettlement);

			if (currentStageOrderId < ((Long) stageNameOrderMap.get(WorkflowStage.COMPLETED.getWorkflowTaskName()))
					.longValue()) {
				activitiHistoryTaskInstanceDto.setStatus(Constants.NOT_STARTED);
				activitiHistoryTaskInstanceDto.setLiveStatus(Constants.NOT_STARTED);
			} else {
				activitiHistoryTaskInstanceDto.setStatus(Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE);
				activitiHistoryTaskInstanceDto.setLiveStatus(Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE);
				activitiHistoryTaskInstanceDto.setStartTime(maxEndTime);
				activitiHistoryTaskInstanceDto.setEndTime(maxEndTime);
				activitiHistoryTaskInstanceDto.setAssignee(completedUserId);
				toRemoveTaskNames.add(WorkflowStage.IC_DECISION.getWorkflowTaskName());
			}

			activitiHistoryTaskInstanceDtos.add(activitiHistoryTaskInstanceDto);

			if (isCurrentStageClaimSettlement.equals(WorkflowStage.CLAIM_SETTLEMENT.getWorkflowTaskName()))
				toRemoveTaskNames.add(WorkflowStage.CLAIM_SETTLEMENT.getWorkflowTaskName());

			if (serviceRequestDto.getWorkflowData() != null && serviceRequestDto.getWorkflowData().getVisit() != null) {
				String isSelfService = serviceRequestDto.getWorkflowData().getVisit().getIsSelfService();
				if (isSelfService != null && isSelfService.equals(Constants.YES_FLAG)) {
					toRemoveTaskNames.add(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowTaskName());
				} else {
					toRemoveTaskNames.add(WorkflowStage.IC_DOC.getWorkflowTaskName());
				}
			}

			taskNameList.removeAll(toRemoveTaskNames);
			if (!isActivitiCompleted) {
				for (String taskName : taskNameList) {
					activitiHistoryTaskInstanceDto = new ActivitiHistoryTaskInstanceDto();
					activitiHistoryTaskInstanceDto.setStageOrderId((Long) stageNameOrderMap.get(taskName));
					activitiHistoryTaskInstanceDto.setProcessInstanceId(processInstanceId);
					activitiHistoryTaskInstanceDto.setTaskName(taskName);
					activitiHistoryTaskInstanceDto.setStatus(Constants.NOT_STARTED);
					activitiHistoryTaskInstanceDto.setLiveStatus(Constants.NOT_STARTED);
					activitiHistoryTaskInstanceDtos.add(activitiHistoryTaskInstanceDto);
				}
			}

			Collections.sort(activitiHistoryTaskInstanceDtos, new Comparator<ActivitiHistoryTaskInstanceDto>() {

				@Override
				public int compare(ActivitiHistoryTaskInstanceDto o1, ActivitiHistoryTaskInstanceDto o2) {

					return o1.getStartTime() != null ? o1.getStartTime().compareTo(o2.getStartTime())
							: o1.getStageOrderId().compareTo(o2.getStageOrderId());
				}
			});
		}

		return activitiHistoryTaskInstanceDtos;
	}

	private List<ActivitiHistoryCommentDto> getComments(String taskInstanceId) {

		List<Comment> comments = taskService.getTaskComments(taskInstanceId);
		List<ActivitiHistoryCommentDto> activitiHistoryCommentDtos = new ArrayList<>();
		for (Comment comment : comments) {
			ActivitiHistoryCommentDto activitiHistoryCommentDto = new ActivitiHistoryCommentDto();
			activitiHistoryCommentDto.setFullMessage(comment.getFullMessage());
			activitiHistoryCommentDto.setProcInstanceId(comment.getProcessInstanceId());
			activitiHistoryCommentDto.setTime(comment.getTime());
			activitiHistoryCommentDtos.add(activitiHistoryCommentDto);
		}

		return activitiHistoryCommentDtos;
	}

	@Override
	public PageResponseDto<List<ServiceResponseDto>> getServiceRequests(ServiceRequestSearchDto serviceRequestSearchDto,
			PageRequest pageRequest) throws BusinessServiceException {

		PageResponseDto<List<ServiceResponseDto>> responseDto = new PageResponseDto<>();
		List<ErrorInfoDto> errorInfoList = serviceRequestValidator
				.doValidateServiceRequestSearch(serviceRequestSearchDto);

		if (null != errorInfoList && errorInfoList.size() > 0) {

			throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList,
					new InputValidationException());
		} else {
			Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMasterList = serviceRequestTypeMasterCache
					.getAll();

			if (!Strings.isNullOrEmpty(serviceRequestSearchDto.getServiceRequestType())) {
				List<String> serviceRequestTypeCodeList = Arrays
						.asList(serviceRequestSearchDto.getServiceRequestType().split(","));
				List serviceRequestTypeIdList = new ArrayList<Long>();

				for (String serviceRequestType : serviceRequestTypeCodeList) {
					serviceRequestTypeIdList
							.add(serviceRequestTypeMasterList.get(serviceRequestType).getServiceRequestTypeId());
				}

				serviceRequestSearchDto.setServiceRequestType(StringUtils.join(serviceRequestTypeIdList, ","));
			}

			Page<ServiceRequestEntity> page = serviceRequestRepository
					.findAll(ServiceRequestSpecifications.filterServiceRequests(serviceRequestSearchDto), pageRequest);

			List<ServiceRequestEntity> list = page.getContent();
			responseDto.setTotalElements(page.getTotalElements());
			responseDto.setTotalPages(page.getTotalPages());

			List<String> membershipIds = new ArrayList<>();
			String referenceNos = null;
			Map<Long, ServiceRequestEntity> serviceRequestEntityMap = new HashMap<>();
			for (ServiceRequestEntity serviceRequestEntity : list) {
				serviceRequestEntityMap.put(serviceRequestEntity.getServiceRequestId(), serviceRequestEntity);
				membershipIds.add(serviceRequestEntity.getReferenceNo());
			}
			referenceNos = com.oneassist.serviceplatform.commons.utils.StringUtils.convertListToString(membershipIds,
					Constants.COMMA);
			List<ServiceRequestDto> serviceRequestDtoList = modelMapper.map(list,
					new TypeToken<List<ServiceRequestDto>>() {
					}.getType());

			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(serviceRequestDtoList)) {
				for (ServiceRequestDto serviceRequest : serviceRequestDtoList) {
					Map<String, Pendency> pendency = serviceRequest.getPendency();
					if (pendency != null) {
						Pendency documentUploadPendency = pendency.get("documentUpload");
						if (documentUploadPendency != null) {
							if (documentUploadPendency.getDocuments() != null) {
								for (DocumentRejection document : documentUploadPendency.getDocuments()) {
									document.setDocumentName(serviceRequestDocumentTypeMasterCache.getAll()
											.get(document.getDocumentTypeId()).getDocName());
								}
							}
						}

					}
				}
			}

			/*
			 * modelMapper.addConverter(new WorkFlowStringToObjectConverter());
			 * List<ServiceRequestDto> serviceRequestDtoList =
			 * modelMapper.map(list, new TypeToken<List<ServiceRequestDto>>() {
			 * }.getType());
			 */
			Map<Long, Long> addressIDServiceRequestMap = new HashMap<>();
			List<Long> addressIDList = new ArrayList<>();

			List<ServiceResponseDto> serviceResponseDtoList = new ArrayList<ServiceResponseDto>();
			ServiceResponseDto serviceResponseDto = null;

			Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMstEntityMap = serviceRequestTypeMasterCache
					.getAll();
			ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = null;
			Map<Long, Map<Long, AssetDetailDto>> membershipAssetsMap = null;

			if (serviceRequestSearchDto.getOptions() != null
					&& serviceRequestSearchDto.getOptions().contains("showAssets")) {

				if (StringUtils.isNotBlank(referenceNos)) {
					OASYSCustMemDetails custMembershipDetails = oasysProxy.getMembershipAssetDetails(referenceNos, null,
							null, false);
					if (custMembershipDetails != null) {
						membershipAssetsMap = new HashMap<>();
						for (OASYSMembershipDetails membership : custMembershipDetails.getMemberships()) {
							if (!membershipAssetsMap.containsKey(membership.getMemId())) {
								Map<Long, AssetDetailDto> assetsMap = new HashMap<>();
								for (AssetDetailDto assetDto : membership.getAssets()) {
									assetsMap.put(assetDto.getAssetId(), assetDto);
								}
								membershipAssetsMap.put(membership.getMemId(), assetsMap);
							}
						}
					}
				}
			}

			if (null != serviceRequestDtoList && serviceRequestDtoList.size() > 0) {

				for (int i = 0; i < serviceRequestDtoList.size(); i++) {
					serviceResponseDto = new ServiceResponseDto();
					ServiceRequestDto serviceRequestDto = serviceRequestDtoList.get(i);
					serviceResponseDto = modelMapper.map(serviceRequestDto, new TypeToken<ServiceResponseDto>() {
					}.getType());

					if (serviceRequestSearchDto.getOptions() != null
							&& serviceRequestSearchDto.getOptions().contains("showAssets")) {
						List<ServiceRequestAssetEntity> requestAssetEntities = serviceRequestEntityMap
								.get(serviceRequestDto.getServiceRequestId()).getServiceRequestAssetEntity();

						List<ServiceRequestAssetResponseDto> responseAssets = modelMapper.map(requestAssetEntities,
								new TypeToken<List<ServiceRequestAssetResponseDto>>() {
								}.getType());

						if (membershipAssetsMap != null && membershipAssetsMap
								.get(Long.parseLong(serviceResponseDto.getReferenceNo())) != null) {
							for (ServiceRequestAssetResponseDto assetResponse : responseAssets) {
								if (assetResponse != null
										&& membershipAssetsMap.get(Long.parseLong(serviceResponseDto.getReferenceNo()))
												.get(Long.parseLong(assetResponse.getAssetReferenceNo())) != null) {
									entityToDtoMapper.mapAssetDetailDtoToServiceRequestAssetResponseDto(assetResponse,
											membershipAssetsMap.get(Long.parseLong(serviceResponseDto.getReferenceNo()))
													.get(Long.parseLong(assetResponse.getAssetReferenceNo())));
								}
							}
						}
						serviceResponseDto.setAssets(responseAssets);
					}

					String serviceRequestType = null;
					String serviceRequestTypeName = null;

					for (Map.Entry<String, ServiceRequestTypeMstEntity> entry : serviceRequestTypeMstEntityMap
							.entrySet()) {
						serviceRequestTypeMstEntity = entry.getValue();

						if (serviceRequestTypeMstEntity.getServiceRequestTypeId().longValue() == serviceRequestDto
								.getServiceRequestTypeId().longValue()) {
							serviceRequestType = entry.getKey();
							serviceRequestTypeName = serviceRequestTypeMstEntity.getServiceRequestTypeName();

							break;
						}
					}

					serviceResponseDto.setServiceRequestType(serviceRequestType);
					serviceResponseDto.setServiceRequestTypeName(serviceRequestTypeName);
					if (serviceRequestDto.getWorkflowData() != null
							&& serviceRequestDto.getWorkflowData().getVisit() != null
							&& serviceRequestDto.getWorkflowData().getVisit().getDateOfIncident() != null) {
						serviceResponseDto
								.setDateOfIncident(serviceRequestDto.getWorkflowData().getVisit().getDateOfIncident());
					}

					if (ServiceRequestType.PE_ADLD.getRequestType().equals(serviceRequestType)
							|| ServiceRequestType.PE_EW.getRequestType().equals(serviceRequestType)
							|| ServiceRequestType.PE_THEFT.getRequestType().equals(serviceRequestType)) {
						if (serviceRequestDto.getWorkflowData() != null
								&& serviceRequestDto.getWorkflowData().getDocumentUpload() != null && serviceRequestDto
										.getWorkflowData().getDocumentUpload().getDateOfIncident() != null) {
							serviceResponseDto.setDateOfIncident(
									serviceRequestDto.getWorkflowData().getDocumentUpload().getDateOfIncident());
						}
					}

					if (REQUIRE_ADDITIONAL_INFO
							.equalsIgnoreCase(serviceRequestSearchDto.getRequiresAdditionalDetails())) {
						// serviceResponseDto.setServiceAddress(getAddress(serviceRequestDto));
						// // set address
						String referenceCode = null;
						if (serviceResponseDto.getAssets() != null && serviceResponseDto.getAssets().size() > 0) {
							referenceCode = serviceResponseDto.getAssets().get(0).getProductCode();
						}

						serviceResponseDto.setServiceDocuments(getDocuments(serviceRequestDto, serviceRequestType,
								serviceRequestSearchDto.getOptions(), serviceResponseDto.getInsurancePartnerCode(),
								referenceCode));
						serviceRequestDto.setServiceRequestType(serviceRequestType);
						if (serviceRequestDto.getWorkflowProcessId() != null) {
							serviceResponseDto.setActivitiHistoryTasks(
									this.getAllTasks(serviceRequestDto, serviceRequestDto.getWorkflowProcessId()));
						}

						serviceResponseDto.setShipmentDetails(shipmentService
								.getShipmentsByServiceRequestId(serviceResponseDto.getServiceRequestId()));
					}

					if (TECHNICIAN_PROFILE_REQUIRED
							.equalsIgnoreCase(serviceRequestSearchDto.getTechnicianProfileRequired())
							&& serviceRequestDto.getAssignee() != null && serviceRequestDto.getAssignee() > 0) {
						UserProfileData technicianProfile = oasysAdminProxy
								.getTechnicianUserProfile(String.valueOf(serviceRequestDto.getAssignee()));

						try {
							if (technicianProfile != null
									&& StringUtils.isNotBlank(technicianProfile.getProfileMongoId())) {
								DocumentDownloadRequestDto documentDownloadRequestDto = new DocumentDownloadRequestDto();
								String[] mongoId = { technicianProfile.getProfileMongoId() };
								documentDownloadRequestDto.setDocumentIds(mongoId);
								DownloadDocumentDto downloadDoc = serviceRequestDocumentService
										.getDownLoadDocumentDtoByFileType(documentDownloadRequestDto,
												FileType.IMAGEFILE.getFileTypeName());
								technicianProfile.setImageByteArray(downloadDoc.getInputStream());
							}
						} catch (Exception e) {
							throw new BusinessServiceException(
									"Unable to fetch Technician profile Document from Mongo. Technician Userid:"
											+ technicianProfile.getUserId());
						}
						serviceResponseDto.setTechnicianProfileDetails(technicianProfile);
					}

					if (null != serviceRequestDto.getWorkflowData()
							&& null != serviceRequestDto.getWorkflowData().getVisit()
							&& null != serviceRequestDto.getWorkflowData().getVisit().getServiceAddress()) {
						try {
							Long addressId = Long
									.valueOf(serviceRequestDto.getWorkflowData().getVisit().getServiceAddress());
							addressIDServiceRequestMap.put(serviceRequestDto.getServiceRequestId(), addressId);
							addressIDList.add(addressId);
						} catch (NumberFormatException e) {
							logger.error("Not a Valid Address ID "
									+ serviceRequestDto.getWorkflowData().getVisit().getServiceAddress()
									+ " for Service Request ID: " + serviceRequestDto.getServiceRequestId());
						}
					}

					if (StringUtils.isNotBlank(serviceRequestDto.getAdviceId())) {
						PaymentDto paymentDto = new PaymentDto();
						populateAdviceDto(paymentDto, serviceRequestDto);
						if (ServiceRequestType.PE_ADLD.getRequestType().equals(serviceRequestType)
								|| ServiceRequestType.PE_EW.getRequestType().equals(serviceRequestType)
								|| ServiceRequestType.PE_THEFT.getRequestType().equals(serviceRequestType)) {
							serviceResponseDto.getWorkflowData().getInsuranceDecision().setPayment(paymentDto);
						} else {
							serviceResponseDto.getWorkflowData().getVerification().setPayment(paymentDto);
						}

					}

					serviceResponseDtoList.add(serviceResponseDto);
				}

				Map<Long, ServiceAddressEntity> addressMap = new HashMap<>();

				if (!addressIDList.isEmpty()) {
					List<ServiceAddressEntity> addressEntities = serviceAddressRepository
							.findAll(ServiceAddressSpecifications.filterServiceAddressSRSearch(addressIDList));

					if (addressEntities != null && !addressEntities.isEmpty()) {
						for (ServiceAddressEntity addressEntity : addressEntities) {
							addressMap.put(addressEntity.getServiceAddressId(), addressEntity);
						}
					}
				}

				for (int i = 0; i < serviceResponseDtoList.size(); i++) {
					serviceResponseDto = serviceResponseDtoList.get(i);
					if (addressIDServiceRequestMap.containsKey(serviceResponseDto.getServiceRequestId())) {
						Long addrId = addressIDServiceRequestMap.get(serviceResponseDto.getServiceRequestId());

						if (addressMap.containsKey(addrId)) {
							if (addressMap.get(addrId) != null) {
								ServiceAddressDetailDto serviceAddressDetailDto = modelMapper
										.map(addressMap.get(addrId), ServiceAddressDetailDto.class);
								try {
									PincodeMasterDto pincodeMasterDto = pinCodeMasterCache
											.get(addressMap.get(addrId).getPincode());
									if (pincodeMasterDto != null) {
										serviceAddressDetailDto.setDistrict(pincodeMasterDto.getCityName());
										serviceAddressDetailDto.setState(pincodeMasterDto.getStateName());
										serviceResponseDto.setServiceAddress(serviceAddressDetailDto);
									}
								} catch (Exception e) {
									logger.error("Exception while getting city/state name.", e);
								}
							}
						}
					}
				}
			}

			/*
			 * if (serviceResponseDtoList == null ||
			 * serviceResponseDtoList.isEmpty()) { throw new
			 * BusinessServiceException("No Service Requests Found for the given Search Criteria"
			 * ); }
			 */

			responseDto.setData(serviceResponseDtoList);
		}

		return responseDto;
	}

	private List<ServiceDocumentDto> getDocuments(ServiceRequestDto serviceRequestDto, String serviceType,
			Set<String> set, Long insurancePartnerCode, String referenceCode) {

		logger.info("start getting documents for serviceType: " + serviceType + " and insurancePartnerCode: "
				+ insurancePartnerCode + " and referenceCode: " + referenceCode);

		List<ServiceDocumentEntity> serviceDocumentEntities = null;
		List<ServiceDocumentDto> serviceDocumentDtos = null;

		if (set != null && set.contains("onlyCustomerDocumentsRequired")) {
			List<DocTypeConfigDetailEntity> docTypeConfigDetailEntities = docTypeConfigDetailCache.get(serviceType);

			List<Long> docTypeIds = new ArrayList<Long>();
			for (DocTypeConfigDetailEntity docTypeConfigDetailEntity : docTypeConfigDetailEntities) {

				if ((ServiceRequestType.PE_ADLD.getRequestType().equals(serviceType)
						|| ServiceRequestType.PE_EW.getRequestType().equals(serviceType)
						|| ServiceRequestType.PE_THEFT.getRequestType().equals(serviceType))
						&& insurancePartnerCode != null && referenceCode != null) {

					if (docTypeConfigDetailEntity.getIsMandatory().equals("Y")
							&& docTypeConfigDetailEntity.getInsurancePartnerCode().equals(insurancePartnerCode)
							&& docTypeConfigDetailEntity.getReferenceCode().equals(referenceCode)) {
						docTypeIds.add(docTypeConfigDetailEntity.getDocTypeId());
					}
				}

				else if (ServiceRequestType.HA_EW.getRequestType().equals(serviceType)
						|| ServiceRequestType.HA_BR.getRequestType().equals(serviceType)
						|| ServiceRequestType.HA_BD.getRequestType().equals(serviceType)
						|| ServiceRequestType.HA_EW.getRequestType().equals(serviceType)
						|| ServiceRequestType.HA_FR.getRequestType().equals(serviceType)) {

					if (docTypeConfigDetailEntity.getIsMandatory().equals("Y")) {
						docTypeIds.add(docTypeConfigDetailEntity.getDocTypeId());
					}

				}

			}

			if (!docTypeIds.isEmpty()) {
				serviceDocumentEntities = serviceDocumentRepository.findByServiceRequestIdAndDocumentTypeIdInAndStatus(
						serviceRequestDto.getServiceRequestId(), docTypeIds, Constants.ACTIVE);
			}
		} else {
			serviceDocumentEntities = serviceDocumentRepository
					.findByServiceRequestIdAndStatus(serviceRequestDto.getServiceRequestId(), Constants.ACTIVE);
		}

		if (serviceDocumentEntities != null && !serviceDocumentEntities.isEmpty()) {
			serviceDocumentDtos = modelMapper.map(serviceDocumentEntities, new TypeToken<List<ServiceDocumentDto>>() {
			}.getType());
		}

		return serviceDocumentDtos;
	}

	@Override
	public ServiceResponseDto validateAuthorizationCode(Long servicerequestId, String authorizationCode,
			int authorizationCodeSequence) throws Exception {

		// ServiceResponseDto serviceResponseDto = new ServiceResponseDto();
		List<ErrorInfoDto> errorInfoList = serviceRequestValidator.doValidateAuthorizationValidationRequest(
				servicerequestId, authorizationCode, authorizationCodeSequence);

		if (null != errorInfoList && errorInfoList.size() > 0) {

			throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList,
					new InputValidationException());
		} else {

			ServiceRequestEntity serviceRequestEntity = serviceRequestRepository
					.findServiceRequestEntityByServiceRequestId(Long.valueOf(servicerequestId));

			if (serviceRequestEntity != null) {
				serviceRequestHelper.validateAuthorizationCode(authorizationCode, authorizationCodeSequence,
						serviceRequestEntity);

			} else {
				throw new BusinessServiceException(
						ServiceRequestResponseCodes.SERVICE_REQUEST_ID_NOT_EXISTS.getErrorCode(),
						new Object[] { "" + servicerequestId });
			}
		}

		return null;
	}

	@Override
	public ServiceRequestAuthorizationResponseDto getAuthorizationCode(Long servicerequestId)
			throws NoSuchMessageException, BusinessServiceException {

		ServiceRequestAuthorizationResponseDto authorizationResponseDto = new ServiceRequestAuthorizationResponseDto();
		List<ErrorInfoDto> errorInfoList = serviceRequestValidator
				.doValidateGetAuthorizationCodeRequest(servicerequestId);

		if (null != errorInfoList && errorInfoList.size() > 0) {

			throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList,
					new InputValidationException());
		} else {
			ServiceRequestEntity serviceRequestEntity = serviceRequestRepository
					.findServiceRequestEntityByServiceRequestId(Long.valueOf(servicerequestId));

			if (serviceRequestEntity != null) {
				ServiceRequestDto serviceRequestDto = serviceRequestHelper.convertObject(serviceRequestEntity,
						ServiceRequestDto.class);
				if (null != serviceRequestDto.getWorkflowData().getVisit().getServiceStartCode()
						&& null != serviceRequestDto.getWorkflowData().getRepair().getServiceEndCode()) {
					ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = getServiceRequestTypeByTypeId(
							serviceRequestEntity.getServiceRequestTypeId());
					ServiceRequestType serviceRequestType = ServiceRequestType
							.getServiceRequestType(serviceRequestTypeMstEntity.getServiceRequestType());
					serviceRequestDto.setServiceRequestType(serviceRequestTypeMstEntity.getServiceRequestType());
					switch (serviceRequestType) {

					case HA_AD:
					case HA_BD:
					case HA_BR:
					case HA_FR:
					case WHC_INSPECTION:
						communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestDto,
								CommunicationGatewayEventCode.SP_WHC_RESEND_OTP, null);
						break;
					case HA_EW:
						communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestDto,
								CommunicationGatewayEventCode.SP_EW_RESEND_OTP, null);
						break;
					default:
						break;

					}
					authorizationResponseDto
							.setServiceStartCode(serviceRequestDto.getWorkflowData().getVisit().getServiceStartCode());
					authorizationResponseDto
							.setServiceEndCode(serviceRequestDto.getWorkflowData().getRepair().getServiceEndCode());
				} else {
					throw new BusinessServiceException(
							ServiceRequestResponseCodes.AUTHORIZATION_CODE_FETCH_FAILED.getErrorCode());
				}

			} else {
				throw new BusinessServiceException(
						ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_INVALID_SERVICE_REQUEST_ID.getErrorCode());
			}
		}

		return authorizationResponseDto;
	}

	private ServiceRequestTypeMstEntity getServiceRequestTypeByTypeId(Long serviceTypeId) {
		ServiceRequestTypeMstEntity typeEntity = null;
		Collection<ServiceRequestTypeMstEntity> serivceTypeEntities = serviceRequestTypeMasterCache.getAll().values();
		for (ServiceRequestTypeMstEntity serviceRequestTypeEntity : serivceTypeEntities) {
			if (serviceTypeId.longValue() == serviceRequestTypeEntity.getServiceRequestTypeId().longValue()) {
				typeEntity = serviceRequestTypeEntity;
				break;
			}
		}
		return typeEntity;
	}

	@Override
	public CostToServiceDto calculateCostToCustomer(long serviceRequestId,
			AssigneeRepairCostRequestDto assigneeRepairCostRequestDto)
			throws NoSuchMessageException, BusinessServiceException {

		ServiceRequestEntity serviceRequestEntity = serviceRequestRepository.findOne(serviceRequestId);
		if (serviceRequestEntity == null) {
			throw new BusinessServiceException(ServiceRequestResponseCodes.SERVICE_REQUEST_ID_NOT_EXISTS.getErrorCode(),
					new Object[] { "" + serviceRequestId });
		} else {
			String serviceRequestTypeStr = null;
			Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMstEntityMap = serviceRequestTypeMasterCache
					.getAll();
			ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = null;
			for (Map.Entry<String, ServiceRequestTypeMstEntity> entry : serviceRequestTypeMstEntityMap.entrySet()) {
				serviceRequestTypeMstEntity = entry.getValue();
				if (serviceRequestTypeMstEntity.getServiceRequestTypeId().longValue() == serviceRequestEntity
						.getServiceRequestTypeId().longValue()) {
					serviceRequestTypeStr = entry.getKey();
					break;
				}
			}
			ServiceRequestType serviceRequestType = ServiceRequestType.getServiceRequestType(serviceRequestTypeStr);
			BaseServiceTypeHandler serviceRequestTypeHandler = serviceTypeHandlerFactory
					.getServiceRequestTypeHandler(serviceRequestType);
			CostToServiceDto response = serviceRequestTypeHandler.doCalculateCostToCustomer(serviceRequestId,
					assigneeRepairCostRequestDto, serviceRequestEntity);
			return response;
		}
	}

	private ServiceAddressDetailDto getAddress(ServiceRequestDto serviceRequestDto) {

		String serviceAddressId = null;
		ServiceAddressDetailDto serviceAddressDetailDto = null;

		if (null != serviceRequestDto.getWorkflowData() && null != serviceRequestDto.getWorkflowData().getVisit()
				&& null != serviceRequestDto.getWorkflowData().getVisit().getServiceAddress()) {
			serviceAddressId = serviceRequestDto.getWorkflowData().getVisit().getServiceAddress();
			ServiceAddressEntity serviceAddressEntity = serviceAddressRepository
					.findByServiceAddressId(Long.valueOf(serviceAddressId));

			try {
				PincodeMasterDto pincodeMasterDto = pinCodeMasterCache.get(serviceAddressEntity.getPincode());
				if (pincodeMasterDto != null) {
					serviceAddressDetailDto = modelMapper.map(serviceAddressEntity, ServiceAddressDetailDto.class);
					serviceAddressDetailDto.setDistrict(pincodeMasterDto.getCityName());
					serviceAddressDetailDto.setState(pincodeMasterDto.getStateName());
				}
			} catch (Exception ex) {

				logger.error("Exception while getting city/state name for " + serviceRequestDto.toString(), ex);
			}
		}

		return serviceAddressDetailDto;
	}

	private String getSpareParts(Long serviceRequestId, String refPrimaryTrackingNo)
			throws JsonParseException, JsonMappingException, ParseException, IOException, BusinessServiceException {

		String spareParts = "";
		ServiceRequestSearchDto serviceRequestSearchDto = new ServiceRequestSearchDto();
		serviceRequestSearchDto.setServiceRequestId(serviceRequestId);
		// serviceRequestSearchDto.setRequiresAdditionalDetails(REQUIRE_ADDITIONAL_INFO);

		PageResponseDto<List<ServiceResponseDto>> serviceResponse = getServiceRequests(serviceRequestSearchDto, null);

		if (serviceResponse != null && serviceResponse.getData() != null
				&& !CollectionUtils.isEmpty(serviceResponse.getData())) {

			int i = 0;
			for (ServiceResponseDto serviceResponseDto : serviceResponse.getData()) {
				if (!CollectionUtils
						.isEmpty(serviceResponseDto.getWorkflowData().getRepairAssessment().getSparePartsRequired())) {
					for (SpareParts sparePart : serviceResponseDto.getWorkflowData().getRepairAssessment()
							.getSparePartsRequired()) {
						ServiceTaskDto serviceTaskDto = serviceTaskMasterCache.get(sparePart.getSparePartId());

						if (i != 0) {
							spareParts += ",";
						}

						spareParts += serviceTaskDto.getTaskName();
						i++;
					}
				}
			}
		}

		return spareParts;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
	public CompleteWHCInspectionRequestDto completeWHCInspection(
			CompleteWHCInspectionRequestDto completeWHCInspectionRequestDto) throws Exception {

		logger.info(">>> In ServiceRequestServiceImpl: Update Service Request(ServiceRequestUpdateRequestDto)"
				+ completeWHCInspectionRequestDto);

		List<ErrorInfoDto> errorInfoList = serviceRequestValidator
				.doValidateWHCInspection(completeWHCInspectionRequestDto);
		logger.error("Processing inspection completion request for ::" + completeWHCInspectionRequestDto);

		if (null != errorInfoList && !errorInfoList.isEmpty()) {
			throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList,
					new InputValidationException());
		} else {
			ServiceRequestEntity serviceRequestEntity = serviceRequestRepository
					.findOne(completeWHCInspectionRequestDto.getServiceRequestId());
			if (serviceRequestEntity == null) {
				throw new BusinessServiceException(
						ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_INVALID_SERVICE_REQUEST_ID.getErrorCode());
			} else if (!ServiceRequestStatus.COMPLETED.getStatus().equalsIgnoreCase(serviceRequestEntity.getStatus())) {
				completeWHCInspectionRequestDto.setRefPrimaryTrackingNo(serviceRequestEntity.getRefPrimaryTrackingNo());
				completeWHCInspectionRequestDto.setMemStatus("Activate");
				completeWHCInspectionRequestDto = processInspectCompletion(completeWHCInspectionRequestDto,
						serviceRequestEntity);
			} else {
				throw new BusinessServiceException(
						ServiceRequestResponseCodes.SERVICE_REQUEST_ALREADY_COMPLETE.getErrorCode(), errorInfoList,
						new InputValidationException());
			}
		}

		return completeWHCInspectionRequestDto;
	}

	private CompleteWHCInspectionRequestDto processInspectCompletion(
			CompleteWHCInspectionRequestDto completeWHCInspectionRequestDto, ServiceRequestEntity serviceRequestEntity)
			throws Exception {

		System.out.println("completeWHCInspectionRequestDto:: " + completeWHCInspectionRequestDto);
		int isUpdated = 0;
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

		WorkflowData workflowData = new WorkflowData();
		workflowData.setInspectionAssessment(null);

		ServiceRequestDto serviceRequestDto = new ServiceRequestDto();
		serviceRequestDto.setServiceRequestId(completeWHCInspectionRequestDto.getServiceRequestId());
		serviceRequestDto.setModifiedBy(completeWHCInspectionRequestDto.getModifiedBy());
		serviceRequestDto.setRefPrimaryTrackingNo(completeWHCInspectionRequestDto.getRefPrimaryTrackingNo());
		serviceRequestDto.setWorkflowData(workflowData);
		serviceRequestDto.setWorkflowJsonString(serviceRequestEntity.getWorkflowData());
		serviceRequestDto.setServiceRequestType(ServiceRequestType.WHC_INSPECTION.getRequestType());

		ServiceRequestEventCode eventCode = null;

		if (completeWHCInspectionRequestDto.getInspectionAssessment() == null) {
			populateInspectionAssessments(completeWHCInspectionRequestDto);
		} else {
			populateMongoRefId(completeWHCInspectionRequestDto);
		}

		List<ErrorInfoDto> errorInfoList = serviceRequestValidator
				.validteCompleteInspectionAssetDetails(completeWHCInspectionRequestDto, serviceRequestEntity);

		if (null != errorInfoList && !errorInfoList.isEmpty()) {
			throw new BusinessServiceException(
					ServiceRequestResponseCodes.COMPLETE_INSPECTION_VALIDATION_FAILED.getErrorCode(), errorInfoList,
					new InputValidationException());
		} else {
			if (completeWHCInspectionRequestDto.getInspectionAssessment() == null
					|| completeWHCInspectionRequestDto.getInspectionAssessment().getAssets() == null
					|| completeWHCInspectionRequestDto.getInspectionAssessment().getAssets().isEmpty()) {

				eventCode = ServiceRequestEventCode.INSPECTION_FAILED;
			} else {
				eventCode = ServiceRequestEventCode.INSPECTION_COMPLETED;
			}

			serviceRequestHelper.populateJsonWithEventCode(serviceRequestDto, eventCode, serviceRequestEntity);
			serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestDto, eventCode,
					serviceRequestEntity);

			isUpdated = serviceRequestRepository.updateServiceRequestStatusWorkflowAlertAndActualEndDate(
					serviceRequestDto.getStatus(), serviceRequestDto.getWorkflowStage(),
					serviceRequestDto.getWorkflowStageStatus(), serviceRequestDto.getWorkflowJsonString(), null,
					currentTimestamp, currentTimestamp, serviceRequestDto.getModifiedBy(),
					serviceRequestDto.getServiceRequestId());

			if (isUpdated == 1) {
				if (ServiceRequestEventCode.INSPECTION_FAILED.equals(eventCode)) {
					completeWHCInspectionRequestDto.setMemStatus("Failed");
				}

				publishWHCInspectionSR(completeWHCInspectionRequestDto, serviceRequestEntity);
				serviceRequestDto.setStatus(serviceRequestDto.getStatus());
				serviceRequestDto.setWorkflowStage(serviceRequestDto.getWorkflowStage());
				serviceRequestDto.setWorkflowStageStatus(serviceRequestDto.getWorkflowStageStatus());

				objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

				serviceRequestDto.setWorkflowJsonString(serviceRequestDto.getWorkflowJsonString());
				serviceRequestDto.setWorkflowData(
						objectMapper.readValue(serviceRequestDto.getWorkflowJsonString(), WorkflowData.class));
				serviceRequestDto.setServiceRequestType(
						serviceRequestHelper.getServiceReqeustType(serviceRequestEntity.getServiceRequestTypeId()));

				srDataNotificationManager.notify(DataNotificationEventType.UPDATED, serviceRequestDto,
						ServiceRequestUpdateAction.UPDATE_SERVICE_REQUEST_ON_EVENT);
			} else {

				throw new BusinessServiceException(messageSource.getMessage(
						String.valueOf(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode()),
						new Object[] { "" }, null));
			}
		}

		return completeWHCInspectionRequestDto;
	}

	private void populateInspectionAssessments(CompleteWHCInspectionRequestDto completeWHCInspectionRequestDto) {
		ServiceRequestAssetRequestDto serviceRequestAssetRequestDto = new ServiceRequestAssetRequestDto();
		serviceRequestAssetRequestDto.setServiceRequestId(completeWHCInspectionRequestDto.getServiceRequestId());
		List<ServiceRequestAssetEntity> serviceRequestAssetEntities = serviceRequestAssetRepository
				.findAll(ServiceRequestAssetSpecifications.filterServiceRequests(serviceRequestAssetRequestDto));

		if (serviceRequestAssetEntities != null && !serviceRequestAssetEntities.isEmpty()) {
			InspectionAssessment inspectionAssessment = new InspectionAssessment();

			List<Inspection> inspectionList = new ArrayList<Inspection>();
			for (ServiceRequestAssetEntity serviceRequestAssetEntity : serviceRequestAssetEntities) {
				Inspection inspection = new Inspection();
				inspection.setBrand(serviceRequestAssetEntity.getMake());
				inspection.setIsAccidentalDamage(serviceRequestAssetEntity.getIsAccidentalDamage());
				inspection.setIsFunctional(serviceRequestAssetEntity.getIsFunctional());
				inspection.setIsInformationCorrect(serviceRequestAssetEntity.getIsInformationCorrect());
				inspection.setModelNo(serviceRequestAssetEntity.getModelNo());
				inspection.setProdInspectionStatus(serviceRequestAssetEntity.getAssetInspectionStatus());
				inspection.setProductAge(serviceRequestAssetEntity.getAssetAge());
				inspection.setProductCode(serviceRequestAssetEntity.getProductCode());
				inspection.setProductSize(serviceRequestAssetEntity.getAssetSize());
				inspection.setProductTechnology(serviceRequestAssetEntity.getAssetTechnology());
				inspection.setProductUnit(serviceRequestAssetEntity.getAssetUnit());
				inspection.setSerialNo(serviceRequestAssetEntity.getSerialNo());
				List<ServiceRequestEntityDocumentEntity> serviceRequestEntityDocumentEntities = serviceRequestAssetEntity
						.getServiceRequestAssetDocuments();

				List<ServiceDocumentEntity> serviceDocumentEntities = serviceDocumentRepository
						.findByServiceRequestIdAndStatus(completeWHCInspectionRequestDto.getServiceRequestId(),
								Constants.ACTIVE);

				if (serviceRequestEntityDocumentEntities != null && !serviceRequestEntityDocumentEntities.isEmpty()) {

					List<ImageStorageReference> assetImageList = new ArrayList<ImageStorageReference>();

					for (ServiceRequestEntityDocumentEntity serviceRequestEntityDocumentEntity : serviceRequestEntityDocumentEntities) {
						ImageStorageReference imageRef = new ImageStorageReference();
						imageRef.setDocumentId(serviceRequestEntityDocumentEntity.getDocumentId());
						String documentId = serviceRequestEntityDocumentEntity.getDocumentId();

						if (serviceDocumentEntities != null && !serviceDocumentEntities.isEmpty()) {

							for (ServiceDocumentEntity serviceDocumentEntity : serviceDocumentEntities) {

								if (documentId.equals(serviceDocumentEntity.getDocumentId())) {
									imageRef.setImageMongoRefId(serviceDocumentEntity.getStorageRefId());
									imageRef.setFileName(serviceDocumentEntity.getDocumentName());
									imageRef.setDocumentTypeId(serviceDocumentEntity.getDocumentTypeId());
									DocTypeMstEntity docTypeMasterEntity = serviceRequestDocumentTypeMasterCache
											.get("" + serviceDocumentEntity.getDocumentTypeId());

									imageRef.setImageName(docTypeMasterEntity.getDocName().equals(
											ServiceDocumentType.DEVICE_FRONT_IMAGE.getServiceDocumentType()) ? "front"
													: docTypeMasterEntity.getDocName()
															.equals(ServiceDocumentType.DEVICE_UNIT_LABEL
																	.getServiceDocumentType()) ? "label"
																			: docTypeMasterEntity.getDocName());
								}
							}
						}

						assetImageList.add(imageRef);
					}

					inspection.setImageStorageRef(assetImageList);
				}

				inspectionList.add(inspection);
			}

			inspectionAssessment.setAssets(inspectionList);
			completeWHCInspectionRequestDto.setInspectionAssessment(inspectionAssessment);
		}
	}

	private void publishWHCInspectionSR(CompleteWHCInspectionRequestDto completeWHCInspectionRequestDto,
			ServiceRequestEntity serviceRequestEntity) throws Exception {
		WHCInspectionSREventDto publishwhcInspectionSREventDto = new WHCInspectionSREventDto();

		System.out.println("completeWHCInspectionRequestDto:: " + completeWHCInspectionRequestDto);
		logger.error("completeWHCInspectionRequestDto:: " + completeWHCInspectionRequestDto);
		ServiceRequestDto serviceRequestDto = serviceRequestHelper.convertObject(serviceRequestEntity,
				ServiceRequestDto.class);

		ServiceAddressDetailDto addressDetails = getAddress(serviceRequestDto);
		addressDetails.setCreatedOn(null);
		addressDetails.setModifiedOn(null);

		publishwhcInspectionSREventDto.setServiceRequestId(completeWHCInspectionRequestDto.getServiceRequestId());
		publishwhcInspectionSREventDto.setAddressDetails(addressDetails);
		publishwhcInspectionSREventDto.setEventName(ClaimLifecycleEvent.WHC_COMPLETE_INSPECTION_SR);
		publishwhcInspectionSREventDto
				.setRefPrimaryTrackingNo(completeWHCInspectionRequestDto.getRefPrimaryTrackingNo());
		publishwhcInspectionSREventDto.setMemStatus(completeWHCInspectionRequestDto.getMemStatus());
		publishwhcInspectionSREventDto
				.setInspectionAssessment(completeWHCInspectionRequestDto.getInspectionAssessment());

		serviceRequestHelper.publishMessageInCustomerPropagationQueue(publishwhcInspectionSREventDto);
	}

	private void populateMongoRefId(CompleteWHCInspectionRequestDto completeWHCInspectionRequestDto) {
		List<ServiceDocumentEntity> serviceDocumentEntities = serviceDocumentRepository.findByServiceRequestIdAndStatus(
				completeWHCInspectionRequestDto.getServiceRequestId(), Constants.ACTIVE);
		if (completeWHCInspectionRequestDto != null) {
			InspectionAssessment inspectionAssessment = completeWHCInspectionRequestDto.getInspectionAssessment();
			List<Inspection> inspectionList = inspectionAssessment.getAssets();
			for (Inspection inspection : inspectionList) {
				List<ImageStorageReference> assetImageList = inspection.getImageStorageRef();
				for (ImageStorageReference imageRef : assetImageList) {
					String documentId = imageRef.getDocumentId();
					if (serviceDocumentEntities != null && !serviceDocumentEntities.isEmpty()) {
						for (ServiceDocumentEntity serviceDocumentEntity : serviceDocumentEntities) {
							if (documentId.equals(serviceDocumentEntity.getDocumentId())) {
								imageRef.setDocumentTypeId(serviceDocumentEntity.getDocumentTypeId());
								imageRef.setImageMongoRefId(serviceDocumentEntity.getStorageRefId());
								break;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public List<AssetClaimEligibilityDto> checkRaiseClaimEligibility(Date eligibilityStartDate, String referenceNo) {

		if (eligibilityStartDate == null || StringUtils.isEmpty(referenceNo)) {
			throw new BusinessServiceException("Mandatory Parameter(s) absent");
		}

		List<ServiceRequestEntity> serviceRequestsRaisedByMembershipId = serviceRequestRepository
				.findByReferenceNoAndCreatedOnAfter(referenceNo, eligibilityStartDate);

		Map<String, AssetClaimEligibilityDto> invalidAssets = new HashMap<>();

		for (ServiceRequestEntity serviceRequestEntity : serviceRequestsRaisedByMembershipId) {

			ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = serviceRequestTypeMstRepository
					.findServiceRequestTypeMstByServiceRequestTypeId(serviceRequestEntity.getServiceRequestTypeId());

			if (serviceRequestTypeMstEntity.getServiceRequestType().equals(ServiceRequestType.PE_ADLD.getRequestType())
					|| serviceRequestTypeMstEntity.getServiceRequestType()
							.equals(ServiceRequestType.PE_EW.getRequestType())
					|| serviceRequestTypeMstEntity.getServiceRequestType()
							.equals(ServiceRequestType.PE_THEFT.getRequestType())) {

				String responseJson = oasysAdminProxy.checkClaimEligibility(serviceRequestEntity.getReferenceNo(),
						serviceRequestEntity.getRefSecondaryTrackingNo());

				boolean eligibilty = false;
				if (responseJson != null) {
					JSONObject jsonObject = new JSONObject(responseJson);
					eligibilty = (Boolean) jsonObject.get("eligibility");
				}

				if (!eligibilty) {
					AssetClaimEligibilityDto assetEligibilityDto = new AssetClaimEligibilityDto();
					assetEligibilityDto.setStatusCode(Constants.NO_FLAG);
					assetEligibilityDto.setMessage("Claim Eligibility check failed");
					assetEligibilityDto.setAssetReferenceNo(serviceRequestEntity.getRefSecondaryTrackingNo());
					invalidAssets.put(serviceRequestEntity.getRefSecondaryTrackingNo(), assetEligibilityDto);
				}

			} else {
				List<ServiceRequestAssetEntity> serviceRequestAssetList = serviceRequestEntity
						.getServiceRequestAssetEntity();

				if (!validateBERCaseforSR(serviceRequestEntity)) {
					for (ServiceRequestAssetEntity serviceRequestAssetEntity : serviceRequestAssetList) {
						AssetClaimEligibilityDto assetEligibilityDto = new AssetClaimEligibilityDto();
						assetEligibilityDto.setAssetReferenceNo(serviceRequestAssetEntity.getAssetReferenceNo());
						assetEligibilityDto.setStatusCode(Constants.NO_FLAG);
						assetEligibilityDto.setMessage(
								"You cannot raise a claim as appliance/device was marked beyond economic repair during the last service.");
						invalidAssets.put(serviceRequestAssetEntity.getAssetReferenceNo(), assetEligibilityDto);
					}
				} else if (validateDuplicateSR(serviceRequestEntity)) {
					for (ServiceRequestAssetEntity serviceRequestAssetEntity : serviceRequestAssetList) {
						;
						AssetClaimEligibilityDto assetEligibilityDto = new AssetClaimEligibilityDto();
						assetEligibilityDto.setAssetReferenceNo(serviceRequestAssetEntity.getAssetReferenceNo());
						assetEligibilityDto.setStatusCode(Constants.NO_FLAG);
						assetEligibilityDto.setMessage("On-going Service Request");

						if (!invalidAssets.containsKey(serviceRequestAssetEntity.getAssetReferenceNo())) {
							invalidAssets.put(serviceRequestAssetEntity.getAssetReferenceNo(), assetEligibilityDto);
						}
					}
				}
			}

		}
		return new ArrayList<>(invalidAssets.values());
	}

	@Override
	public boolean validateDuplicateSR(ServiceRequestEntity srEntity) {
		if ((ServiceRequestStatus.CLOSED.getStatus().equalsIgnoreCase(srEntity.getStatus()))
				|| (ServiceRequestStatus.COMPLETED.getStatus().equalsIgnoreCase(srEntity.getStatus()))) {
			return false;
		}

		return true;
	}

	@Override
	public boolean validateBERCaseforSR(ServiceRequestEntity serviceRequestEntity) {
		WorkflowData workflowData = serviceRequestHelper.getWorkflowDataByServiceRequest(serviceRequestEntity);
		if (workflowData.getInsuranceDecision() != null && WorkflowStageStatus.BER_APPROVED.getWorkflowStageStatus()
				.equalsIgnoreCase(workflowData.getInsuranceDecision().getStatus())) {
			return false;
		}

		return true;
	}

	@Override
	public ServiceRequestDto getServiceRequestByExternalSRId(String externalServiceId) throws BusinessServiceException {
		ServiceRequestDto serviceRequestDto = null;
		ServiceRequestEntity serviceRequestEntity = serviceRequestRepository
				.findByExternalSRReferenceId(externalServiceId);
		if (serviceRequestEntity != null) {
			serviceRequestDto = serviceRequestHelper.convertObject(serviceRequestEntity, ServiceRequestDto.class);
		} else {
			throw new BusinessServiceException(
					ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_INVALID_SERVICE_REQUEST_ID.getErrorCode(),
					new InputValidationException());
		}
		return serviceRequestDto;
	}

	@Override
	public Integer updateExternalSRId(String externalServiceId, Date modifiedOn, String modifiedBy,
			Long serviceRequestId) {
		return serviceRequestRepository.updateExternalSRReferenceId(externalServiceId, modifiedOn, modifiedBy,
				serviceRequestId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
	public ServiceRequestDto updateServiceRequest(ServiceRequestDto updateServiceRequestDto) throws Exception {
		ServiceRequestDto response = null;

		try {
			ServiceRequestEntity existingServiceRequestEntity = serviceRequestRepository
					.findByServiceRequestId(updateServiceRequestDto.getServiceRequestId());
			if (existingServiceRequestEntity == null) {
				throw new BusinessServiceException("Corresponding serviceRequestId does not exist:"
						+ updateServiceRequestDto.getServiceRequestId());
			}

			ServiceRequestEntity newServiceRequestEntity = modelMapper.map(updateServiceRequestDto,
					new TypeToken<ServiceRequestEntity>() {
					}.getType());
			ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = null;
			Set<ServiceRequestTypeMstEntity> serviceRequestTypeMstEntityList = new HashSet<>(
					serviceRequestTypeMasterCache.getAll().values());
			for (ServiceRequestTypeMstEntity serviceRequestMst : serviceRequestTypeMstEntityList) {
				if (serviceRequestMst.getServiceRequestTypeId().longValue() == existingServiceRequestEntity
						.getServiceRequestTypeId().longValue()) {
					serviceRequestTypeMstEntity = serviceRequestMst;
					break;
				}
			}

			if (serviceRequestTypeMstEntity != null && serviceRequestTypeMstEntity.getServiceRequestType() != null) {
				ServiceRequestType serviceRequestType = ServiceRequestType
						.getServiceRequestType(serviceRequestTypeMstEntity.getServiceRequestType());
				BaseServiceTypeHandler serviceRequestTypeHandler = serviceTypeHandlerFactory
						.getServiceRequestTypeHandler(serviceRequestType);

				response = serviceRequestTypeHandler.updateServiceRequest(updateServiceRequestDto,
						newServiceRequestEntity, existingServiceRequestEntity);
				updateServiceRequestDto = serviceRequestHelper.convertObject(newServiceRequestEntity,
						ServiceRequestDto.class);

				updateServiceRequestDto.setServiceRequestType(serviceRequestType.getRequestType());
			} else {
				throw new BusinessServiceException(
						ServiceRequestResponseCodes.INVALID_SERVICE_REQUEST_TYPE.getErrorCode(),
						new InputValidationException());
			}
		} catch (Exception ex) {
			updateServiceRequestDto.setMessage(ex.getMessage());
			throw ex;
		} finally {
			srDataNotificationManager.notify(DataNotificationEventType.UPDATED, response);
		}

		return response;
	}

	private void populateAdviceDto(PaymentDto adviceDto, ServiceRequestDto serviceRequestDto) {
		ServiceRequestType serviceRequestType = ServiceRequestType
				.getServiceRequestType(serviceRequestDto.getServiceRequestType());
		if (ServiceRequestType.PE_ADLD.equals(serviceRequestType) || ServiceRequestType.PE_EW.equals(serviceRequestType)
				|| ServiceRequestType.PE_THEFT.equals(serviceRequestType)) {
			adviceDto.setAdviceAmount(serviceRequestDto.getWorkflowData().getInsuranceDecision().getIcExcessAmt());
		} else {
			adviceDto.setAdviceAmount(Double.valueOf(adviceAmount));
			adviceDto.setChargeId(Integer.valueOf(adviceChargeId));
		}
		adviceDto.setAdviceId(serviceRequestDto.getAdviceId());
		adviceDto.setMembershipId(serviceRequestDto.getReferenceNo());
	}

}
