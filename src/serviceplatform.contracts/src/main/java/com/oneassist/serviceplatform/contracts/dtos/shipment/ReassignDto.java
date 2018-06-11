package com.oneassist.serviceplatform.contracts.dtos.shipment;

import java.util.List;

public class ReassignDto {
	private int recordFailed;

	private List<ShipmentReassignFailDto> failedList;

	public int getRecordFailed() {
		return recordFailed;
	}

	public void setRecordFailed(int recordFailed) {
		this.recordFailed = recordFailed;
	}

	public List<ShipmentReassignFailDto> getFailedList() {
		return failedList;
	}

	public void setFailedList(List<ShipmentReassignFailDto> failedList) {
		this.failedList = failedList;
	}

	@Override
	public String toString() {
		return "ReassignDto [recordFailed=" + recordFailed + ", failedList=" + failedList + "]";
	}
	
}
