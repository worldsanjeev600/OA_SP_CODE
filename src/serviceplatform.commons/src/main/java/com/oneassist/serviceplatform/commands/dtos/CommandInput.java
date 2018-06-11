package com.oneassist.serviceplatform.commands.dtos;

import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;

public class CommandInput<T> {

    private T data;
    private ServiceRequestEventCode commandEventCode;
    private ServiceRequestEntity serviceRequestEntity;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ServiceRequestEventCode getCommandEventCode() {
        return commandEventCode;
    }

    public void setCommandEventCode(ServiceRequestEventCode commandEventCode) {
        this.commandEventCode = commandEventCode;
    }

    public ServiceRequestEntity getServiceRequestEntity() {
        return serviceRequestEntity;
    }

    public void setServiceRequestEntity(ServiceRequestEntity serviceRequestEntity) {
        this.serviceRequestEntity = serviceRequestEntity;
    }

}
