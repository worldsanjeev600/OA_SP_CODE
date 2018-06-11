package com.oneassist.serviceplatform.contracts.response;

import com.oneassist.serviceplatform.contracts.dtos.PartnerEventDetailDto;

public class PartnerEventDetailDataDto {

	private String eventName;
	private PartnerEventDetailDto partnerEvent;
	
	public String getEventName() {	
		return eventName;
	}

	public void setEventName(String eventName) {	
		this.eventName = eventName;
	}

	public PartnerEventDetailDto getPartnerEvent() {	
		return partnerEvent;
	}
	
	public void setPartnerEvent(PartnerEventDetailDto partnerEvent) {	
		this.partnerEvent = partnerEvent;
	}

	@Override
	public String toString() {
		return "PartnerEventDetailDataDto [eventName=" + eventName + ", partnerEvent=" + partnerEvent + "]";
	}	
	
}
