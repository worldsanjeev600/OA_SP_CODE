package com.oneassist.serviceplatform.externalcontracts.loginext;

import java.io.Serializable;
import java.util.List;

public class LoginextShipmentResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -24568823475807853L;
    private Integer status;
    private String message;
    private List<String> referenceId;
    private Object data;
    private Object error;
    private Boolean hasError;

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getReferenceId() {
        return this.referenceId;
    }

    public void setReferenceId(List<String> referenceId) {
        this.referenceId = referenceId;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getHasError() {
        return this.hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "LoginextShipmentResponse [status=" + status + ", message=" + message + ", referenceId=" + referenceId + ", data=" + data + ", hasError=" + hasError + "]";
    }
}
