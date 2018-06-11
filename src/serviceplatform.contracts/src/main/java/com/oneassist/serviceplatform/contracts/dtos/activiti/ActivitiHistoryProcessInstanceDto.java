package com.oneassist.serviceplatform.contracts.dtos.activiti;
import java.util.Date;

public class ActivitiHistoryProcessInstanceDto {
	private String processInstanceId;
	private String taskName;
	private String description;
	private String assignee;
	private Date startTime;
	private Date claimTime;
	private Date endTime;
	private Long duration;
	private Long priority;
	private Date dueDate;
	
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
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getAssignee() {
		return this.assignee;
	}
	
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	
	public Date getStartTime() {
		return this.startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getClaimTime() {
		return this.claimTime;
	}
	
	public void setClaimTime(Date claimTime) {
		this.claimTime = claimTime;
	}
	
	public Date getEndTime() {
		return this.endTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public Long getDuration() {
		return this.duration;
	}
	
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	
	public Long getPriority() {
		return this.priority;
	}
	
	public void setPriority(Long priority) {
		this.priority = priority;
	}
	
	public Date getDueDate() {
		return this.dueDate;
	}
	
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActivitiHistoryProcessInstanceDto [processInstanceId=");
		builder.append(processInstanceId);
		builder.append(", taskName=");
		builder.append(taskName);
		builder.append(", description=");
		builder.append(description);
		builder.append(", assignee=");
		builder.append(assignee);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", claimTime=");
		builder.append(claimTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", duration=");
		builder.append(duration);
		builder.append(", priority=");
		builder.append(priority);
		builder.append(", dueDate=");
		builder.append(dueDate);
		builder.append("]");
		return builder.toString();
	}
	
}