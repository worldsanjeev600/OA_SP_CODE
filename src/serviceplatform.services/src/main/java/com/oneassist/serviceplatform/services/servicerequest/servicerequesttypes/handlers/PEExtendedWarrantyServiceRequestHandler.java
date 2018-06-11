package com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers;

import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import org.springframework.stereotype.Component;

@Component
public class PEExtendedWarrantyServiceRequestHandler extends BasePEServiceTypeHandler {

    public PEExtendedWarrantyServiceRequestHandler() {

        super(ServiceRequestType.PE_EW);
    }

}
