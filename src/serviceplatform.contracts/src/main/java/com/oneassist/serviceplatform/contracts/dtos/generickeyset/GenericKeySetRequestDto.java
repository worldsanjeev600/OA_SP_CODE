package com.oneassist.serviceplatform.contracts.dtos.generickeyset;

public class GenericKeySetRequestDto {
	private String keySetName;
	
	public String getKeySetName() {
		return keySetName;
	}

	public void setKeySetName(String keySetName) {
		this.keySetName = keySetName;
	}

	@Override
	public String toString() {
		return "GenericKeySetRequestDto [keySetName=" + keySetName + "]";
	}
	
}
