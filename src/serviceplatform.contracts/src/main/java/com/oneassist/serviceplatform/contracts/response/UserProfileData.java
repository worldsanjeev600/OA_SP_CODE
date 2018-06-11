package com.oneassist.serviceplatform.contracts.response;

public class UserProfileData {
	
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobileNumber;
	private String emailId;
	private String userId;
	private String createdOn;
	private String profileMongoId;
	private String experience;
	private String imageByteArray;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getProfileMongoId() {
		return profileMongoId;
	}
	public void setProfileMongoId(String profileMongoId) {
		this.profileMongoId = profileMongoId;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getImageByteArray() {
		return imageByteArray;
	}
	public void setImageByteArray(String imageByteArray) {
		this.imageByteArray = imageByteArray;
	}
	
	@Override
	public String toString() {
		return "UserProfileData [firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName
				+ ", mobileNumber=" + mobileNumber + ", emailId=" + emailId + ", userId=" + userId + ", createdOn="
				+ createdOn + ", profileMongoId=" + profileMongoId + ", experience=" + experience + ", imageByteArray="
				+ imageByteArray + "]";
	}

	
}
