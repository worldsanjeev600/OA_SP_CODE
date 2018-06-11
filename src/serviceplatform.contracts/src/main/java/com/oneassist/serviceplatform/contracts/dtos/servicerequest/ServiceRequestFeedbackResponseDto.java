package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRequestFeedbackResponseDto {

	@ApiModelProperty(value = "Feedback rating for the service request. It's a start rating between 0-5", required = true)
	private String feedbackRating;

	@ApiModelProperty(value = "Feedback code mapped to a list of canned feedback items from server.", required = false)
	private List<String> feedbackCode;

	@ApiModelProperty(value = "Freetext feedback comments from the customer", required = false)
	private String feedbackComments;

	public String getFeedbackRating() {
		return feedbackRating;
	}

	public void setFeedbackRating(String feedbackRating) {
		this.feedbackRating = feedbackRating;
	}

	public List<String> getFeedbackCode() {
		return feedbackCode;
	}

	public void setFeedbackCode(List<String> feedbackCode) {
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
		return "ServiceRequestFeedbackResponseDto [feedbackRating=" + feedbackRating + ", feedbackCode=" + feedbackCode
				+ ", feedbackComments=" + feedbackComments + "]";
	}

}
