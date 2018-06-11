/**
 * 
 */

package com.oneassist.serviceplatform.contracts.dtos.allocationmaster;

import java.io.Serializable;
import java.util.List;

public class PincodeServiceMasterResponseDto implements Serializable {

	private List<PincodeServiceMasterDataDto> data;

	public List<PincodeServiceMasterDataDto> getData() {
		return data;
	}

	public void setData(List<PincodeServiceMasterDataDto> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "PincodeServiceMasterResponseDto [data=" + data + "]";
	}
	
}
