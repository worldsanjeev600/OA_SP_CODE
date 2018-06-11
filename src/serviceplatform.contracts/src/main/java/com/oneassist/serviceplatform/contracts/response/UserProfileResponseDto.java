package com.oneassist.serviceplatform.contracts.response;

public class UserProfileResponseDto {
	
	private UserProfileData data;

	public UserProfileData getData() {
		return data;
	}

	public void setData(UserProfileData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "UserProfileResponseDto [data=" + data + "]";
	}
	
}
