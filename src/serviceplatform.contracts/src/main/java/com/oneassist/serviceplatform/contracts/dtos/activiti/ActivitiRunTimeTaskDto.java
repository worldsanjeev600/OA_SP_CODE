package com.oneassist.serviceplatform.contracts.dtos.activiti;
import java.util.Date;

public class ActivitiRunTimeTaskDto {

	private String id;
	private Long rev;
	private String executionId;
	private String procInstId;
	private String procDefId;
	private String name;
	private String parentTaskId;
	private String description;
	private String taskDefKey;
	private String owner;
	private String assignee;
	private String delegation;
	private Long priority;
	private Date createTime;
	private Date dueDate;
	private String category;
	private Long susspensionState;
	private String tenantId;
	private String formKey;
	private Long count;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getRev() {
		return rev;
	}
	public void setRev(Long rev) {
		this.rev = rev;
	}
	public String getExecutionId() {
		return executionId;
	}
	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}
	public String getProcInstId() {
		return procInstId;
	}
	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}
	public String getProcDefId() {
		return procDefId;
	}
	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentTaskId() {
		return parentTaskId;
	}
	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTaskDefKey() {
		return taskDefKey;
	}
	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getDelegation() {
		return delegation;
	}
	public void setDelegation(String delegation) {
		this.delegation = delegation;
	}
	public Long getPriority() {
		return priority;
	}
	public void setPriority(Long priority) {
		this.priority = priority;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Long getSusspensionState() {
		return susspensionState;
	}
	public void setSusspensionState(Long susspensionState) {
		this.susspensionState = susspensionState;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getFormKey() {
		return formKey;
	}
	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActivitiRunTimeTaskDto [id=");
		builder.append(id);
		builder.append(", rev=");
		builder.append(rev);
		builder.append(", executionId=");
		builder.append(executionId);
		builder.append(", procInstId=");
		builder.append(procInstId);
		builder.append(", procDefId=");
		builder.append(procDefId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", parentTaskId=");
		builder.append(parentTaskId);
		builder.append(", description=");
		builder.append(description);
		builder.append(", taskDefKey=");
		builder.append(taskDefKey);
		builder.append(", owner=");
		builder.append(owner);
		builder.append(", assignee=");
		builder.append(assignee);
		builder.append(", delegation=");
		builder.append(delegation);
		builder.append(", priority=");
		builder.append(priority);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", dueDate=");
		builder.append(dueDate);
		builder.append(", category=");
		builder.append(category);
		builder.append(", susspensionState=");
		builder.append(susspensionState);
		builder.append(", tenantId=");
		builder.append(tenantId);
		builder.append(", formKey=");
		builder.append(formKey);
		builder.append(", count=");
		builder.append(count);
		builder.append("]");
		return builder.toString();
	}
	
}
