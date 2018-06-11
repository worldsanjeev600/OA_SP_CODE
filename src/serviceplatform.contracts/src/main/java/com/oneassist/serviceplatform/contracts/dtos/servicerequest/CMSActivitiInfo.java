package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.Date;

public class CMSActivitiInfo {

    private String id;
    private String name;
    private String deleteReason;
    private Date startTime;
    private Date endTime;
    private String assignee;
    private String userName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
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

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "CMSActivitiInfo [id=" + id + ", name=" + name + ", deleteReason=" + deleteReason + ", startTime=" + startTime + ", endTime=" + endTime + ", assignee=" + assignee + ", userName="
                + userName + "]";
    }

}
