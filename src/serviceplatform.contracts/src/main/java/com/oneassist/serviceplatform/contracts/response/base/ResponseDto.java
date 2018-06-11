package com.oneassist.serviceplatform.contracts.response.base;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;

/**
 * @author sanjeev.gupta
 * @param <DtoType>
 */
@JsonInclude(Include.NON_NULL)
public class ResponseDto<DtoType> extends BaseResponse {

    private DtoType data;
    private List<ErrorInfoDto> invalidData;

    public DtoType getData() {
		return data;
	}
	
    public void setData(DtoType data) {
		this.data = data;
	}
	
	public List<ErrorInfoDto> getInvalidData() {
		return invalidData;
	}

	public void setInvalidData(List<ErrorInfoDto> invalidData) {
		this.invalidData = invalidData;
	}

	@Override
	public String toString() {
		return "ResponseDto [data=" + data + ", invalidData=" + invalidData + "]";
	}
	
}