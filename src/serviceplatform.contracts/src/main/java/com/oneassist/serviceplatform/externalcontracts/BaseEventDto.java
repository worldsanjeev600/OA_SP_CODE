package com.oneassist.serviceplatform.externalcontracts;

public class BaseEventDto {

    private ClaimLifecycleEvent eventName;
    private ServicePartner partnerName;
    private Long partnerCode;

    public ClaimLifecycleEvent getEventName() {
        return eventName;
    }

    public void setEventName(ClaimLifecycleEvent eventName) {
        this.eventName = eventName;
    }

    public Long getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(Long partnerCode) {
        this.partnerCode = partnerCode;
    }

    public ServicePartner getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(ServicePartner partnerName) {
        this.partnerName = partnerName;
    }

	@Override
	public String toString() {
		return "BaseEventDto [eventName=" + eventName + ", partnerName=" + partnerName + ", partnerCode=" + partnerCode
				+ "]";
	}
    
}
