package com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers;

import com.oneassist.serviceplatform.commons.cache.PinCodeMasterCache;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PEAccidentalDamageServiceRequestHandler extends BasePEServiceTypeHandler {

    @Autowired
    private PinCodeMasterCache pinCodeMasterCache;

    public PEAccidentalDamageServiceRequestHandler() {

        super(ServiceRequestType.PE_ADLD);
    }
}
