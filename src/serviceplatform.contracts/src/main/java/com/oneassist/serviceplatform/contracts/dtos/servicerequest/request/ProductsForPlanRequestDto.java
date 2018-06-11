package com.oneassist.serviceplatform.contracts.dtos.servicerequest.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class ProductsForPlanRequestDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="Unique Identifier for a Products")
	private String planCode;
	/**
	 * @return the planCode
	 */
	public String getPlanCode() {
		return planCode;
	}
	/**
	 * @param planCode the planCode to set
	 */
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	@Override
	public String toString() {
		return "ProductsForPlanRequestDto [planCode=" + planCode + "]";
	}
	
	
}
