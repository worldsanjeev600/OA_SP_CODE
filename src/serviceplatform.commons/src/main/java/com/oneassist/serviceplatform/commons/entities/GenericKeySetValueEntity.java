package com.oneassist.serviceplatform.commons.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "OA_GENERIC_KEYSET_VALUE_DTL")
public class GenericKeySetValueEntity extends BaseAuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column (name = "VALUE_ID")
	//@SequenceGenerator(name="SEQ_GEN", sequenceName=Constants.SEQ_OA_PINCODE_SERVICE_MST, allocationSize=1)
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
	private Long valueId;
	
	@Column(name = "KEYSET_ID")
	private String keySetId;
	
	@Column(name = "KEY")
	private String key;

	@Column(name = "VALUE")
	private String value;
	
	public Long getValueId() {
		return valueId;
	}

	public void setValueId(Long valueId) {
		this.valueId = valueId;
	}

	public String getKeySetId() {
		return keySetId;
	}

	public void setKeySetId(String keySetId) {
		this.keySetId = keySetId;
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
}
