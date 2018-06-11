package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.List;

import io.swagger.annotations.ApiModel;

@ApiModel(description="Count of Service Requests at each stages")
public class DashboardDto {
	
	private List<Dashboard> dashboardList;

	public List<Dashboard> getDashboardList() {
		return dashboardList;
	}

	public void setDashboardList(List<Dashboard> dashboardList) {
		this.dashboardList = dashboardList;
	}

	@Override
	public String toString() {
		return "DashboardDto [dashboardList=" + dashboardList + "]";
	}
}
