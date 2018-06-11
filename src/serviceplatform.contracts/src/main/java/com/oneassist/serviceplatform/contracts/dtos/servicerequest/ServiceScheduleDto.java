
package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Satish Kumar
 */

public class ServiceScheduleDto implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	private Date				serviceRequestDate;

	private String				serviceRequestType;

	private List<ServiceSlot>	serviceSlots;

	public Date getServiceRequestDate() {

		return serviceRequestDate;
	}

	public void setServiceRequestDate(Date serviceRequestDate) {

		this.serviceRequestDate = serviceRequestDate;
	}

	public List<ServiceSlot> getServiceSlots() {

		return serviceSlots;
	}

	public void setServiceSlots(List<ServiceSlot> serviceSlots) {

		this.serviceSlots = serviceSlots;
	}

	public String getServiceRequestType() {

		return serviceRequestType;
	}

	public void setServiceRequestType(String serviceRequestType) {

		this.serviceRequestType = serviceRequestType;
	}

	@Override
	public String toString() {
		return "ServiceScheduleDto [serviceRequestDate=" + serviceRequestDate + ", serviceRequestType="
				+ serviceRequestType + ", serviceSlots=" + serviceSlots + "]";
	}
	
}