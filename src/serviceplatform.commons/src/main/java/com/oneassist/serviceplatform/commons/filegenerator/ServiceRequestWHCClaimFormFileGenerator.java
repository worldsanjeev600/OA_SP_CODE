package com.oneassist.serviceplatform.commons.filegenerator;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.oneassist.serviceplatform.commons.cache.PinCodeMasterCache;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.GenericKeySetEntity;
import com.oneassist.serviceplatform.commons.entities.GenericKeySetValueEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceAddressEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.WorkflowStageStatus;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.mastercache.GenericKeySetCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTypeMasterCache;
import com.oneassist.serviceplatform.commons.proxies.OasysProxy;
import com.oneassist.serviceplatform.commons.repositories.ServiceAddressRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.utils.DateUtils;
import com.oneassist.serviceplatform.commons.utils.DecimalFormatter;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.utils.StringUtils;
import com.oneassist.serviceplatform.commons.workflowmanager.IWorkflowManager;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestReportRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestReportResponseDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceAddressDetailDto;
import com.oneassist.serviceplatform.externalcontracts.CustMemView;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;

/**
 * @author Alok Singh
 */
@Component
public class ServiceRequestWHCClaimFormFileGenerator extends BaseFileGenerator {

	private final Logger logger = Logger.getLogger(ServiceRequestWHCClaimFormFileGenerator.class);

	private final ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	@Autowired
	private GenericKeySetCache genericKeySetCache;

	@Autowired
	private ServiceAddressRepository serviceAddressRepository;

	@Autowired
	private ServiceRequestTypeMasterCache serviceRequestTypeMasterCache;

	@Autowired
	private OasysProxy oasysProxy;

	@Autowired
	private IWorkflowManager workflowManager;

	@Autowired
	private ServiceRequestHelper serviceRequestHelper;

	@Autowired
	private PinCodeMasterCache pinCodeMasterCache;

	private final String WHC_CLAIM_FORM = "WHC_CLAIM_FORM";

	@Override
	public ServiceRequestReportResponseDto generateFile(ServiceRequestReportRequestDto serviceRequestReportDto) throws Exception {

		ServiceRequestEntity serviceRequestEntity = serviceRequestRepository.findServiceRequestEntityByServiceRequestId(serviceRequestReportDto.getServiceRequestId());
		ServiceResponseDto serviceResponseDto = serviceRequestHelper.convertObject(serviceRequestEntity, ServiceResponseDto.class);

		Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMstEntityMap = serviceRequestTypeMasterCache
				.getAll();
		String serviceRequestType = null;
		for (Map.Entry<String, ServiceRequestTypeMstEntity> entry : serviceRequestTypeMstEntityMap
				.entrySet()) {
			ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = entry.getValue();

			if (serviceRequestTypeMstEntity.getServiceRequestTypeId().longValue() == serviceRequestEntity
					.getServiceRequestTypeId().longValue()) {
				serviceRequestType = entry.getKey();
				break;
			}
		}
		serviceResponseDto.setServiceRequestType(serviceRequestType);
		
		if(!serviceRequestType.equals(ServiceRequestType.HA_AD.getRequestType()) 
				|| !serviceRequestType.equals(ServiceRequestType.HA_BD.getRequestType())
				|| !serviceRequestType.equals(ServiceRequestType.HA_FR.getRequestType())
				|| !serviceRequestType.equals(ServiceRequestType.HA_BR.getRequestType())
				){
			
		}else{
			throw new BusinessServiceException("Invalid service request type for Claim Form..", new InputValidationException());
		}

		ServiceAddressEntity serviceAddressEntity = serviceAddressRepository.findByServiceAddressId(Long.valueOf(serviceResponseDto.getWorkflowData().getVisit().getServiceAddress()));
		ServiceAddressDetailDto serviceAddressDetailDto = modelMapper.map(serviceAddressEntity, ServiceAddressDetailDto.class);
		serviceResponseDto.setServiceAddress(serviceAddressDetailDto);


		GenericKeySetEntity genericKeySet = genericKeySetCache.get("SFTP_LOCATION");
		String templateFilePath = null;
		if (genericKeySet != null) {
			List<GenericKeySetValueEntity> genericKeySetValues = genericKeySet.getGenericKeySetValueDetails();
			if (genericKeySetValues != null & !genericKeySetValues.isEmpty()) {
				for (GenericKeySetValueEntity genericKeySetValueEntity : genericKeySetValues) {
					if (genericKeySetValueEntity.getKey().equals(WHC_CLAIM_FORM)) {
						templateFilePath = genericKeySetValueEntity.getValue();
					}
				}
			}
		}
		System.out.println(templateFilePath);
		serviceRequestReportDto.setTemplateFilePath(templateFilePath);
		populateData(serviceResponseDto, serviceRequestReportDto);
		Document doc = doFileGeneration(serviceRequestReportDto);

		Document document = addDynamicContent(doc, serviceResponseDto, serviceRequestReportDto); 
		ServiceRequestReportResponseDto serviceRequestReportResponseDto = generatePdf(document);

		return serviceRequestReportResponseDto;
	}

	private Document addDynamicContent(Document doc, ServiceResponseDto serviceResponseDto, ServiceRequestReportRequestDto serviceRequestReportDto) throws Exception {
		// NOTE:: Template Document is having a table namely "rootTable". In this by moving to a specific row/ column, we should add dynamic content as per the need.
		DocumentBuilder builder = new DocumentBuilder(doc);
		boolean defaultCheckboxValue = false;
		boolean isAllRisk = false, isBD = false, isFire = false, isBurglary = false;

		if(serviceResponseDto.getServiceRequestType().equals(ServiceRequestType.HA_BD.getRequestType())){
			isBD = true;
		}else if(serviceResponseDto.getServiceRequestType().equals(ServiceRequestType.HA_FR.getRequestType())){
			isFire = true;
		}else if(serviceResponseDto.getServiceRequestType().equals(ServiceRequestType.HA_BR.getRequestType())){
			isBurglary = true;
		}else{
			isAllRisk = true;
		}

		builder.moveToCell(0, 6, 0, 0);
		builder.insertCheckBox("CheckBox", defaultCheckboxValue, isBD, 0);
		builder.moveToCell(0, 6, 1, 0);
		builder.insertCheckBox("CheckBox", defaultCheckboxValue, isAllRisk, 0);
		builder.moveToCell(0, 6, 2, 0);
		builder.insertCheckBox("CheckBox", defaultCheckboxValue, isFire, 0);
		builder.moveToCell(0, 6, 3, 0);
		builder.insertCheckBox("CheckBox", defaultCheckboxValue, isBurglary, 0);
		return builder.getDocument();
	}

	private void populateData(ServiceResponseDto serviceResponseDto, ServiceRequestReportRequestDto serviceRequestReportDto) throws ParseException {
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("$serviceRequestId", serviceResponseDto.getRefPrimaryTrackingNo());
		ServiceAddressDetailDto serviceAddressDetailDto = serviceResponseDto.getServiceAddress();
		data.put("$customerName", StringUtils.getEmptyIfNull(serviceAddressDetailDto.getAddresseeFullName()));
		data.put("$addressLine1", StringUtils.getEmptyIfNull(serviceAddressDetailDto.getAddressLine1()));
		data.put("$addressLine2", StringUtils.getEmptyIfNull(serviceAddressDetailDto.getAddressLine2()));
		data.put("$addressLine3", StringUtils.getEmptyIfNull(serviceAddressDetailDto.getLandmark()));
		data.put("$pincode", StringUtils.getEmptyIfNull(serviceAddressDetailDto.getPincode()));

		// Call to get State and City Name using PINCODE
		try {
			data.put("$city", "");
			data.put("$state", "");
			if (serviceAddressDetailDto.getPincode() != null) {
				PincodeMasterDto stateCityResponse = pinCodeMasterCache.get(serviceAddressDetailDto.getPincode());
				if (stateCityResponse != null) {
					data.put("$city", StringUtils.getEmptyIfNull(stateCityResponse.getCityName()));
					data.put("$state", StringUtils.getEmptyIfNull(stateCityResponse.getStateName()));
				}
			}
		} catch (Exception e) {
			logger.warn("Not getting state and city details, so by passing the details with error: ", e);
		}

		data.put("$phone", serviceAddressDetailDto.getMobileNo() == null ? "" : String.valueOf(serviceAddressDetailDto.getMobileNo()));
		data.put("$email", StringUtils.getEmptyIfNull(serviceAddressDetailDto.getEmail()));

		data.put("$requestDescription", serviceResponseDto.getRequestDescription());
		Date dateTimeOfIncident = DateUtils.fromLongFormattedString(serviceResponseDto.getWorkflowData().getVisit().getDateOfIncident());
		String dateOfIncident = DateUtils.toShortFormattedString(dateTimeOfIncident);
		data.put("$dateOfIncident", StringUtils.getEmptyIfNull(dateOfIncident));
		data.put("$placeOfIncident", StringUtils.getEmptyIfNull(serviceResponseDto.getWorkflowData().getVisit().getPlaceOfIncident()));

		data.put("$refundAmount","");
		data.put("$costtocompany","");
		String icApproval = workflowManager.getVariable(serviceResponseDto.getWorkflowProcessId(),	Constants.ACTIVITI_VAR_IC_APPROVAL);
		//String icApproval = serviceResponseDto.getWorkflowData().getInsuranceDecision().getStatus();
		if(((ServiceRequestType.HA_AD.getRequestType().equalsIgnoreCase(serviceResponseDto.getServiceRequestType()) || 
				ServiceRequestType.HA_BD.getRequestType().equalsIgnoreCase(serviceResponseDto.getServiceRequestType()))
				&& (icApproval != null && icApproval.equalsIgnoreCase(WorkflowStageStatus.BER_APPROVED.getWorkflowStageStatus())))
				||
				ServiceRequestType.HA_BR.getRequestType().equalsIgnoreCase(serviceResponseDto.getServiceRequestType()) ||
				ServiceRequestType.HA_FR.getRequestType().equalsIgnoreCase(serviceResponseDto.getServiceRequestType())){
			String refundAmount = serviceResponseDto.getWorkflowData().getCompleted() == null? "0.0" :serviceResponseDto.getWorkflowData().getCompleted().getRefundAmount() == null ? "0.0" : serviceResponseDto.getWorkflowData().getCompleted()
					.getRefundAmount();
			Double dRefundAmount = Double.parseDouble(refundAmount);
			data.put("$refundAmount", DecimalFormatter.getFormattedValue(dRefundAmount));
		}else{
			String costToCompany = serviceResponseDto.getWorkflowData().getRepairAssessment().getCostToCompany() == null ? "0.0" : serviceResponseDto.getWorkflowData().getRepairAssessment()
					.getCostToCompany();
			Double dCostToCompany = Double.parseDouble(costToCompany);
			data.put("$costtocompany", DecimalFormatter.getFormattedValue(dCostToCompany));
		}

		try{
			data.put("$policyStartDate", "");
			data.put("$policyEndDate","");
			CustMemView custMemView = oasysProxy.getCustomerMembershipDetail(serviceResponseDto.getReferenceNo());
			String policyStartDate = null, policyEndDate = null;
			if (custMemView != null){
				Date startDate = custMemView.getStartDate();
				Date endDate = custMemView.getEndDate();
				policyStartDate = DateUtils.toShortFormattedString(startDate);
				policyEndDate = DateUtils.toShortFormattedString(endDate);
			}
			data.put("$policyStartDate", StringUtils.getEmptyIfNull(policyStartDate));
			data.put("$policyEndDate", StringUtils.getEmptyIfNull(policyEndDate));
		}catch(Exception e){
			logger.error("Error while getting membership details::: ", e);
		}
		serviceRequestReportDto.setData(data);
	}

}
