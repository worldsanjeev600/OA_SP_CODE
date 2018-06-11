package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRequestFeedbackDto {
	
	@ApiModelProperty(value="Feedback rating for the service request. It's a start rating between 0-5", required = true)
	private String feedbackRating;
	
	@ApiModelProperty(value="Feedback code mapped to a list of canned feedback items from server.", required = false)
	private String feedbackCode;
	
	@ApiModelProperty(value="Freetext feedback comments from the customer", required = false)
	private String feedbackComments;

	public String getFeedbackRating() {
		return feedbackRating;
	}

	public void setFeedbackRating(String feedbackRating) {
		this.feedbackRating = feedbackRating;
	}

	public String getFeedbackCode() {
		return feedbackCode;
	}

	public void setFeedbackCode(String feedbackCode) {
		this.feedbackCode = feedbackCode;
	}

	public String getFeedbackComments() {
		return feedbackComments;
	}

	public void setFeedbackComments(String feedbackComments) {
		this.feedbackComments = feedbackComments;
	}

	@Override
	public String toString() {
		return "ServiceRequestFeedbackDto [feedbackRating=" + feedbackRating + ", feedbackCode=" + feedbackCode
				+ ", feedbackComments=" + feedbackComments + "]";
	}
	

}
