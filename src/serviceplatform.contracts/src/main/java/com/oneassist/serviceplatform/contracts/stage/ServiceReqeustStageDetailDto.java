package com.oneassist.serviceplatform.contracts.stage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oneassist.serviceplatform.contracts.dtos.BaseAuditDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceReqeustStageDetailDto extends BaseAuditDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String serviceReqeustType;
	private String stageCode;
	private String stageName;
	private Long stageOrder;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getServiceReqeustType() {
		return serviceReqeustType;
	}
	public void setServiceReqeustType(String serviceReqeustType) {
		this.serviceReqeustType = serviceReqeustType;
	}
	public String getStageCode() {
		return stageCode;
	}
	public void setStageCode(String stageCode) {
		this.stageCode = stageCode;
	}
	public String getStageName() {
		return stageName;
	}
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	public Long getStageOrder() {
		return stageOrder;
	}
	public void setStageOrder(Long stageOrder) {
		this.stageOrder = stageOrder;
	}
	@Override
	public String toString() {
		return "StageDetailDto [id=" + id + ", serviceReqeustType=" + serviceReqeustType + ", stageCode=" + stageCode
				+ ", stageName=" + stageName + ", stageOrder=" + stageOrder + "]";
	}
	
	
	
}
