package com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers;

import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceTypeHandlerFactory {

    @Autowired
    private HomeExtendedWarrantyServiceRequestHandler homeExtendedWarrantyServiceRequestHandler;

    @Autowired
    private InspectionServiceTypeHandler inspectionServiceTypeHandler;

    @Autowired
    private DefaultServiceRequestTypeHandler defaultServiceRequestTypeHandler;

    @Autowired
    private HomeBreakdownServiceRequestHandler homeBreakdownServiceRequestHandler;

    @Autowired
    private HomeAccidentalDamageServiceRequestHandler homeAccidentalDamageServiceRequestHandler;

    @Autowired
    private HomeBurglaryServiceTypeHandler homeBurglaryServiceTypeHandler;

    @Autowired
    private HomeFireServiceRequestHandler homeFireServiceRequestHandler;

    @Autowired
    private PEAccidentalDamageServiceRequestHandler peAccidentalDamageServiceRequestHandler;

    @Autowired
    private PEExtendedWarrantyServiceRequestHandler peExtendedWarrantyServiceRequestHandler;

    @Autowired
    private PETheftServiceRequestHandler peTheftServiceRequestHandler;

    public BaseServiceTypeHandler getServiceRequestTypeHandler(ServiceRequestType serviceRequestType) {
        switch (serviceRequestType) {
            case HA_EW:
                return homeExtendedWarrantyServiceRequestHandler;
            case HA_BD:
                return homeBreakdownServiceRequestHandler;
            case HA_AD:
                return homeAccidentalDamageServiceRequestHandler;
            case HA_BR:
                return homeBurglaryServiceTypeHandler;
            case HA_FR:
                return homeFireServiceRequestHandler;
            case WHC_INSPECTION:
                return inspectionServiceTypeHandler;
            case PE_ADLD:
                return peAccidentalDamageServiceRequestHandler;
            case PE_THEFT:
                return peTheftServiceRequestHandler;
            case PE_EW:
                return peExtendedWarrantyServiceRequestHandler;
            default:
                return defaultServiceRequestTypeHandler;
        }
    }
}
