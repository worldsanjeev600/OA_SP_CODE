package com.oneassist.serviceplatform.services.servicerequest;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.AssetClaimEligibilityDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CostToServiceDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestSearchDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.request.AssigneeRepairCostRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.request.CompleteWHCInspectionRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceRequestAuthorizationResponseDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import com.oneassist.serviceplatform.contracts.response.base.PageResponseDto;
import com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers.BaseServiceTypeHandler;

import org.springframework.context.NoSuchMessageException;
import org.springframework.data.domain.PageRequest;

public interface IServiceRequestService {

    public ServiceRequestDto createServiceRequest(ServiceRequestDto createServiceRequestDto) throws BusinessServiceException, JsonProcessingException;

    public ServiceResponseDto performServiceAction(ServiceRequestUpdateAction action, ServiceRequestDto serviceRequestDto, ServiceRequestEventCode eventCode) throws Exception;

    public PageResponseDto<List<ServiceResponseDto>> getServiceRequests(ServiceRequestSearchDto serviceRequestDto, PageRequest pageRequest) throws BusinessServiceException;

    public ServiceResponseDto validateAuthorizationCode(Long servicerequestId, String authorizationCode, int authorizationCodeSequence) throws Exception;

    public ServiceRequestAuthorizationResponseDto getAuthorizationCode(Long servicerequestId) throws NoSuchMessageException, BusinessServiceException;

    public CostToServiceDto calculateCostToCustomer(long serviceRequestId, AssigneeRepairCostRequestDto assigneeRepairCostRequestDto) throws NoSuchMessageException, BusinessServiceException;

    public CompleteWHCInspectionRequestDto completeWHCInspection(CompleteWHCInspectionRequestDto serviceRequestDto) throws NoSuchMessageException, BusinessServiceException, Exception;

    public List<AssetClaimEligibilityDto> checkRaiseClaimEligibility(Date eligibilityStartDate, String referenceDate);

    public boolean validateDuplicateSR(ServiceRequestEntity srEntity);

    public boolean validateBERCaseforSR(ServiceRequestEntity serviceRequestEntity);

    public ServiceRequestDto getServiceRequestByExternalSRId(String externalServiceId) throws BusinessServiceException;

    public Integer updateExternalSRId(String externalServiceId, Date date, String modifiedByBatch, Long serviceRequestId);
    
    public ServiceRequestDto updateServiceRequest(ServiceRequestDto updateServiceRequestDto) throws Exception;
}
