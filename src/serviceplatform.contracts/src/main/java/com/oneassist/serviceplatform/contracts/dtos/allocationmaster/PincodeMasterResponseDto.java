/**
 * 
 */

package com.oneassist.serviceplatform.contracts.dtos.allocationmaster;

import java.io.Serializable;
import java.util.List;

public class PincodeMasterResponseDto implements Serializable {

	private List<PincodeMasterDataDto> data;

	public List<PincodeMasterDataDto> getData() {
		return data;
	}

	public void setData(List<PincodeMasterDataDto> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "PincodeMasterResponseDto [data=" + data + "]";
	}
	
}
