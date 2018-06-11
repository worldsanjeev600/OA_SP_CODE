package com.oneassist.serviceplatform.contracts.response.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author sanjeev.gupta
 * @param <DtoType>
 */
@JsonInclude(Include.NON_NULL)
public class PageResponseDto<DtoType> extends ResponseDto<DtoType> {

    private Long totalElements;

    private Integer totalPages;
	
	public Long getTotalElements() {
		return totalElements;
	}
	
	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}
	
	public Integer getTotalPages() {
		return totalPages;
	}
	
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	@Override
	public String toString() {
		return "PageResponseDto [totalElements=" + totalElements + ", totalPages=" + totalPages + "]";
	}
	
}