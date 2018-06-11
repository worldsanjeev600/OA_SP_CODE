package com.oneassist.serviceplatform.contracts.dtos.activiti;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ActivitiHistoryTaskInstanceDto {
	
	private Long stageOrderId;
	
	private String processInstanceId;
	
	private String taskName;
	
	private String assignee;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss",timezone="IST")
	private Date startTime;
	
	private List<ActivitiHistoryCommentDto> comments;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss",timezone="IST")
	private Date endTime;
	
	private String status;
	
	private String liveStatus;	
	
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<ActivitiHistoryCommentDto> getComments() {
		return comments;
	}

	public void setComments(List<ActivitiHistoryCommentDto> list) {
		this.comments = list;
	}
	public String getLiveStatus() {
		return liveStatus;
	}
	public void setLiveStatus(String liveStatus) {
		this.liveStatus = liveStatus;
	}
	
	public Long getStageOrderId() {
		return stageOrderId;
	}
	public void setStageOrderId(Long stageOrderId) {
		this.stageOrderId = stageOrderId;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActivitiHistoryTaskInstanceDto [stageOrderId=" + stageOrderId);
		builder.append(", processInstanceId=" + processInstanceId);
		builder.append(", taskName=" + taskName);
		builder.append(", assignee=" + assignee);
		builder.append(", startTime=" + startTime);
		builder.append(", comments=" + comments);
		builder.append(", endTime=" + endTime);
		builder.append(", status=" + status);
		builder.append(", liveStatus=" + liveStatus);
		builder.append("]");
		return builder.toString();
	}
}
