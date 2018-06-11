package com.oneassist.serviceplatform.contracts.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class PartnerEventDetailDto extends BaseAuditDto {

    /**
     * 
     */
    private static final long serialVersionUID = -1124651434482983786L;

    private Long eventId;

    private Long partnerCode;

    private String eventName;

    private String eventDescription;

    private String protocolName;

    private String connectionDetail;

    private Integer eventRetryCount;

    private Integer eventRetryTimeout;

    private String userName;

    private String password;

    private FedexAttributeDeatils parnerAttributes;

    private FedexAttributeDeatils fedexAttributeDeatils;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(Long partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getConnectionDetail() {
        return connectionDetail;
    }

    public void setConnectionDetail(String connectionDetail) {
        this.connectionDetail = connectionDetail;
    }

    public Integer getEventRetryCount() {
        return eventRetryCount;
    }

    public void setEventRetryCount(Integer eventRetryCount) {
        this.eventRetryCount = eventRetryCount;
    }

    public Integer getEventRetryTimeout() {
        return eventRetryTimeout;
    }

    public void setEventRetryTimeout(Integer eventRetryTimeout) {
        this.eventRetryTimeout = eventRetryTimeout;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FedexAttributeDeatils getParnerAttributes() {
        return parnerAttributes;
    }

    public void setParnerAttributes(FedexAttributeDeatils parnerAttributes) {
        this.parnerAttributes = parnerAttributes;
    }

    public FedexAttributeDeatils getFedexAttributeDeatils() {
        return fedexAttributeDeatils;
    }

    public void setFedexAttributeDeatils(FedexAttributeDeatils fedexAttributeDeatils) {
        this.fedexAttributeDeatils = fedexAttributeDeatils;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PartnerEventDetailDto [eventId=");
		builder.append(eventId);
		builder.append(", partnerCode=");
		builder.append(partnerCode);
		builder.append(", eventName=");
		builder.append(eventName);
		builder.append(", eventDescription=");
		builder.append(eventDescription);
		builder.append(", protocolName=");
		builder.append(protocolName);
		builder.append(", connectionDetail=");
		builder.append(connectionDetail);
		builder.append(", eventRetryCount=");
		builder.append(eventRetryCount);
		builder.append(", eventRetryTimeout=");
		builder.append(eventRetryTimeout);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", password=");
		builder.append(password);
		builder.append(", parnerAttributes=");
		builder.append(parnerAttributes);
		builder.append(", fedexAttributeDeatils=");
		builder.append(fedexAttributeDeatils);
		builder.append("]");
		return builder.toString();
	}
    
}
