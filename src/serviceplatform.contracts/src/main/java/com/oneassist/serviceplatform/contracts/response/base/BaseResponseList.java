package com.oneassist.serviceplatform.contracts.response.base;

import java.util.ArrayList;
import java.util.List;

// TODO: Need to rename this class
public class BaseResponseList<T> {

	public BaseResponseList() {
		this.genericKeySetValues = new ArrayList<>();
	}
	
	private List<T> genericKeySetValues;

	public List<T> getGenericKeySetValues() {
		return genericKeySetValues;
	}

	public void setGenericKeySetValues(List<T> genericKeySetValues) {
		this.genericKeySetValues = genericKeySetValues;
	}	
}