
package com.oneassist.serviceplatform.contracts.dtos.shipment;

import java.io.Serializable;
import java.util.List;

import com.oneassist.serviceplatform.contracts.dtos.DashBoardShipmentDetailDto;

/**
 * @author srikanth
 */
public class DashBoardInfoDto implements Serializable {

	private static final long serialVersionUID = -7632893845059525401L;

	private Long	totalShipments;

	private List<DashBoardShipmentDetailDto> dashboard;
	
	public Long getTotalShipments() {	
		return totalShipments;
	}
	
	public void setTotalShipments(Long totalShipments) {	
		this.totalShipments = totalShipments;
	}
	
	public List<DashBoardShipmentDetailDto> getDashboard() {	
		return dashboard;
	}
	
	public void setDashboard(List<DashBoardShipmentDetailDto> dashboard) {	
		this.dashboard = dashboard;
	}

	@Override
	public String toString() {
		return "DashBoardInfoDto [totalShipments=" + totalShipments
				+ ", dashboard=" + dashboard + "]";
	}
}
