package com.oneassist.serviceplatform.contracts.dtos.activiti;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ActivitiHistoryCommentDto {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss",timezone="IST")
	private Date time;
	
	private String procInstanceId;
	
	private String fullMessage;
	
	public Date getTime() {
		return time;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}
	
	public String getProcInstanceId() {
		return procInstanceId;
	}
	
	public void setProcInstanceId(String procInstanceId) {
		this.procInstanceId = procInstanceId;
	}
	
	public String getFullMessage() {
		return fullMessage;
	}
	
	public void setFullMessage(String fullMessage) {
		this.fullMessage = fullMessage;
	}

	@Override
	public String toString() {
		return "ActivitiHistoryCommentDto [time=" + time + ", procInstanceId=" + procInstanceId + ", fullMessage="
				+ fullMessage + "]";
	}	
	
}
