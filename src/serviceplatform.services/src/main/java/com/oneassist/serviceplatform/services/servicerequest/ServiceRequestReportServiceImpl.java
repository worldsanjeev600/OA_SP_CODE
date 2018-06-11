package com.oneassist.serviceplatform.services.servicerequest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.oneassist.serviceplatform.commons.custom.CustomRepository;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestStageMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.LogisticResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestStageMasterCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTypeMasterCache;
import com.oneassist.serviceplatform.commons.repositories.LogisticShipmentRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestTypeMstRepository;
import com.oneassist.serviceplatform.commons.specifications.ServiceRequestSpecifications;
import com.oneassist.serviceplatform.commons.utils.DateUtils;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.validators.ShipmentRequestValidator;
import com.oneassist.serviceplatform.contracts.dtos.DashBoardShipmentDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Dashboard;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DashboardDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestSearchDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.DashBoardInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.DashBoardRequestDto;
import com.oneassist.serviceplatform.externalcontracts.SystemConfigDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

@Service
public class ServiceRequestReportServiceImpl implements IServiceRequestReportService {

    @Autowired
    private ServiceRequestTypeMstRepository serviceRequestTypeMstRepository;

    @Autowired
    private CustomRepository customRepositoryImpl;

    @Autowired
    protected ServiceRequestTypeMasterCache serviceRequestTypeMasterCache;

    @Autowired
    protected ServiceRequestStageMasterCache serviceRequestStageMasterCache;

    @Autowired
    private ShipmentRequestValidator shipmentRequestValidator;

    @Autowired
    ServiceRequestHelper serviceRequestHelper;

    @Autowired
    private LogisticShipmentRepository logisticShipmentRepository;

    @SuppressWarnings("unchecked")
    @Override
    public DashboardDto getDashboardCountDetails() throws BusinessServiceException {

        DashboardDto dashboardDto = new DashboardDto();
        ServiceRequestSearchDto serviceRequestSearchDto = new ServiceRequestSearchDto();
        serviceRequestSearchDto.setStatus("P,IP,OH,CO,X");
        /*
         * ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = serviceRequestTypeMstRepository.findServiceRequestTypeMstByServiceRequestTypeAndStatus( ServiceRequestType.HA_EW.getRequestType(),
         * Constants.ACTIVE);
         */
        Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMasterList = serviceRequestTypeMasterCache.getAll();

        if (serviceRequestTypeMasterList != null && !serviceRequestTypeMasterList.isEmpty()) {
            List serviceRequestTypeList = new ArrayList<Long>();
            serviceRequestTypeList.add(serviceRequestTypeMasterList.get(ServiceRequestType.HA_AD.getRequestType()).getServiceRequestTypeId());
            serviceRequestTypeList.add(serviceRequestTypeMasterList.get(ServiceRequestType.HA_BD.getRequestType()).getServiceRequestTypeId());
            serviceRequestTypeList.add(serviceRequestTypeMasterList.get(ServiceRequestType.HA_BR.getRequestType()).getServiceRequestTypeId());
            serviceRequestTypeList.add(serviceRequestTypeMasterList.get(ServiceRequestType.HA_FR.getRequestType()).getServiceRequestTypeId());
            serviceRequestTypeList.add(serviceRequestTypeMasterList.get(ServiceRequestType.HA_EW.getRequestType()).getServiceRequestTypeId());
            String serviceRequestTypes = StringUtils.join(serviceRequestTypeList, ",");
            serviceRequestSearchDto.setServiceRequestType(serviceRequestTypes);
            List<Object[]> countList = customRepositoryImpl.findServiceRequestCountPerWorkflowStage(ServiceRequestSpecifications.countServiceRequestsByStatus(serviceRequestSearchDto));

            Object[] countForStage = null;

            if (countList != null && !countList.isEmpty()) {
                Iterator<Object[]> serviceIterator = countList.iterator();
                List<Dashboard> dashboardList = new ArrayList<>();
                Map<String, List<ServiceRequestStageMstEntity>> serviceReqeustStageMasterMap = serviceRequestStageMasterCache.getAll();
                Map<String, String> workflowStageMap = new HashMap();
                for (String stageCode : serviceReqeustStageMasterMap.keySet()) {
                    for (ServiceRequestStageMstEntity serviceRequestStageMstEntity : serviceReqeustStageMasterMap.get(stageCode)) {
                        workflowStageMap.put(serviceRequestStageMstEntity.getStageCode(), serviceRequestStageMstEntity.getStageName());
                    }
                }
                while (serviceIterator.hasNext()) {
                    countForStage = serviceIterator.next();
                    Dashboard dashboard = new Dashboard();
                    dashboard.setStageCode((String) countForStage[0]);
                    dashboard.setStageName(workflowStageMap.get(countForStage[0]));
                    dashboard.setCountOfServiceRequests((Long) countForStage[1]);
                    dashboardList.add(dashboard);
                }

                dashboardDto.setDashboardList(dashboardList);
                workflowStageMap = null;
                serviceReqeustStageMasterMap = null;
            } else {
                throw new BusinessServiceException(ServiceRequestResponseCodes.CMS_DASHBOARD_FAILURE.getErrorCode());
            }
        } else {
            throw new BusinessServiceException(ServiceRequestResponseCodes.CMS_DASHBOARD_FAILURE.getErrorCode());
        }

        return dashboardDto;
    }

    @Override
    public DashBoardInfoDto getDashBoardInfo(DashBoardRequestDto dashBoardRequestDto) throws ParseException, NoSuchMessageException, BusinessServiceException {

        DashBoardInfoDto dashBoardInfoDto = new DashBoardInfoDto();
        DashBoardShipmentDetailDto dashBoardShipmentDetailDto;
        List<DashBoardShipmentDetailDto> dashBoard = new ArrayList<>();
        Date shipmentModifiedDt = null;
        long totalShipments = 0;
        List<String> shipmentStages = new ArrayList<>();
        Map<String, String> shipmentStageMap = new HashMap<>();
        List<Object[]> dashBoardShipmentDetailList;
        List<String> existingShipmentStatuses = new ArrayList<>();

        List<ErrorInfoDto> errorInfoList = shipmentRequestValidator.doValidateDashBoardRequest(dashBoardRequestDto);

        if (null != errorInfoList && errorInfoList.size() > 0) {
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList, new InputValidationException());
        } else {
            shipmentModifiedDt = DateUtils.fromShortFormattedString(dashBoardRequestDto.getShipmentModifiedDate());

            List<SystemConfigDto> shipmentStatusConfigList = serviceRequestHelper.getShipmentStage(null, 0);

            if (shipmentStatusConfigList == null || shipmentStatusConfigList.isEmpty()) {
                throw new BusinessServiceException(LogisticResponseCodes.SHIPMENT_CURRENT_STAGES_NOTCONFIGUED.getErrorCode());
            }

            for (SystemConfigDto shipmentConfig : shipmentStatusConfigList) {
                if (shipmentConfig.getParamName() != null) {
                    shipmentStages.add(shipmentConfig.getParamName());
                    shipmentStageMap.put(shipmentConfig.getParamName(), shipmentConfig.getParamValue());
                }
            }

            if (shipmentStages.isEmpty()) {
                throw new BusinessServiceException(LogisticResponseCodes.SHIPMENT_CURRENT_STAGES_NOTCONFIGUED.getErrorCode());
            }

            dashBoardShipmentDetailList = logisticShipmentRepository.findDashBoardInfoByStatus(dashBoardRequestDto.getShipmentStatus(), shipmentStages, shipmentModifiedDt);

            if (dashBoardShipmentDetailList != null && !dashBoardShipmentDetailList.isEmpty()) {

                for (Object[] dashBoardShipmentDetail : dashBoardShipmentDetailList) {
                    dashBoardShipmentDetailDto = new DashBoardShipmentDetailDto();
                    String shipmentStatusCode = (String) dashBoardShipmentDetail[0];

                    dashBoardShipmentDetailDto.setShipmentStatus(shipmentStageMap.get(shipmentStatusCode));
                    dashBoardShipmentDetailDto.setCount(((Long) dashBoardShipmentDetail[1]).intValue());
                    totalShipments = totalShipments + dashBoardShipmentDetailDto.getCount();
                    dashBoard.add(dashBoardShipmentDetailDto);
                    existingShipmentStatuses.add(shipmentStatusCode);
                }
            }

            for (String shipmentStage : shipmentStages) {
                if (!existingShipmentStatuses.contains(shipmentStage)) {
                    dashBoardShipmentDetailDto = new DashBoardShipmentDetailDto();
                    dashBoardShipmentDetailDto.setShipmentStatus(shipmentStageMap.get(shipmentStage));
                    dashBoardShipmentDetailDto.setCount(0);
                    dashBoard.add(dashBoardShipmentDetailDto);
                }
            }

            dashBoardInfoDto.setTotalShipments(totalShipments);
            dashBoardInfoDto.setDashboard(dashBoard);
        }

        return dashBoardInfoDto;
    }
}
