package com.oneassist.serviceplatform.contracts.dtos.generickeyset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oneassist.serviceplatform.contracts.dtos.BaseAuditDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericKeySetDetailDto extends BaseAuditDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long valueId;
	private long keySetId;
	private String keySetName;
	private String keySetDescription;
	private String key;
	private String value;
	
	public long getValueId() {
		return valueId;
	}

	public void setValueId(long valueId) {
		this.valueId = valueId;
	}

	public long getKeySetId() {
		return keySetId;
	}

	public void setKeySetId(long keySetId) {
		this.keySetId = keySetId;
	}

	public String getKeySetName() {
		return keySetName;
	}

	public void setKeySetName(String keySetName) {
		this.keySetName = keySetName;
	}

	public String getKeySetDescription() {
		return keySetDescription;
	}

	public void setKeySetDescription(String keySetDescription) {
		this.keySetDescription = keySetDescription;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "GenericKeySetDetailDto [" 
				+ "valueId=" + valueId 
				+ ", keySetId=" + keySetId 
				+ ", keySetName=" + keySetName				
				+ ", keySetDescription=" + keySetDescription 
				+ ", key=" + key 
				+ ", value=" + value 
				+ "]";
	}
}
