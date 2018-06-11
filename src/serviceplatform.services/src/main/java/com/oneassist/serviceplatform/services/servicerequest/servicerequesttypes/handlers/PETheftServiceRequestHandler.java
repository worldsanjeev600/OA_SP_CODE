package com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers;

import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import org.springframework.stereotype.Component;

@Component
public class PETheftServiceRequestHandler extends BasePEServiceTypeHandler {

    public PETheftServiceRequestHandler() {

        super(ServiceRequestType.PE_THEFT);
    }

}
