package com.oneassist.serviceplatform.contracts.dtos.allocationmaster;

public class PincodeServiceMasterDataDto {

	private int pincodeServiceId;
	private PincodeServiceDto pincodeServiceDto;
	
	public int getPincodeServiceId() {
		return pincodeServiceId;
	}
	
	public void setPincodeServiceId(int pincodeServiceId) {
		this.pincodeServiceId = pincodeServiceId;
	}
	
	public PincodeServiceDto getPincodeServiceDto() {
		return pincodeServiceDto;
	}
	
	public void setPincodeServiceDto(PincodeServiceDto pincodeServiceDto) {
		this.pincodeServiceDto = pincodeServiceDto;
	}

	@Override
	public String toString() {
		return "PincodeServiceMasterDataDto [pincodeServiceId=" + pincodeServiceId + ", pincodeServiceDto="
				+ pincodeServiceDto + "]";
	}
	
}