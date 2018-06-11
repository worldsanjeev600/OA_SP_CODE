package com.oneassist.serviceplatform.commons.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "OA_GENERIC_KEYSET_MST")
public class GenericKeySetEntity extends BaseAuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column (name = "KEYSET_ID")
	//@SequenceGenerator(name="SEQ_GEN", sequenceName=Constants.SEQ_OA_PINCODE_SERVICE_MST, allocationSize=1)
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
	private Long keySetId;
	
	@Column(name = "KEYSET_NAME")
	private String keySetName;
	
	@Column(name = "KEYSET_DESCRIPTION")
	private String keySetDescription;

	@OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "KEYSET_ID", referencedColumnName = "KEYSET_ID", insertable = true, updatable = false)
    List<GenericKeySetValueEntity> genericKeySetValueDetails;
	
	public Long getKeySetId() {
		return keySetId;
	}

	public void setKeySetId(Long keySetId) {
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

	public List<GenericKeySetValueEntity> getGenericKeySetValueDetails() {
		return genericKeySetValueDetails;
	}

	public void setGenericKeySetValueDetails(List<GenericKeySetValueEntity> genericKeySetValueDetails) {
		this.genericKeySetValueDetails = genericKeySetValueDetails;
	}

	
	
	
}
