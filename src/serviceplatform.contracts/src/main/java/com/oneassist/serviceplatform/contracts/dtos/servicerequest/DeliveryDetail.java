package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.io.Serializable;

public class DeliveryDetail implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7712075270878537247L;
    private String delvDueDate;
    private String actDelvDate;
    private String status;
    private String statusCode;
    private String description;

    public String getDelvDueDate() {
        return delvDueDate;
    }

    public void setDelvDueDate(String delvDueDate) {
        this.delvDueDate = delvDueDate;
    }

    public String getActDelvDate() {
        return actDelvDate;
    }

    public void setActDelvDate(String actDelvDate) {
        this.actDelvDate = actDelvDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
    
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "DeliveryDetail [delvDueDate=" + delvDueDate + ", actDelvDate=" + actDelvDate + ", status=" + status
				+ ", statusCode=" + statusCode + ", description=" + description + "]";
	}

}
