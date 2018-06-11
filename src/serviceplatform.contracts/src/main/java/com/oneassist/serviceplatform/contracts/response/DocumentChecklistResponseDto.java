package com.oneassist.serviceplatform.contracts.response;

import java.util.List;

public class DocumentChecklistResponseDto {

	private List<DocumentChecklistDataDto> data;

	public List<DocumentChecklistDataDto> getData() {
		return data;
	}

	public void setData(List<DocumentChecklistDataDto> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "DocumentChecklistResponseDto [data=" + data + "]";
	}	
	
}