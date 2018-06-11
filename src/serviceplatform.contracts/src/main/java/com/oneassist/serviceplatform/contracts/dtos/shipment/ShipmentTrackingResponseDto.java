
package com.oneassist.serviceplatform.contracts.dtos.shipment;

import java.math.BigDecimal;
import java.util.List;

public class ShipmentTrackingResponseDto {

	private Long							shipmentId;

	private String							logisticPartnerRefNum;

	private BigDecimal							originAddressId;

	private String							originAddressLine1;

	private String							originAddressLine2;

	private String							originLandmark;

	private String							originDistrict;

	private BigDecimal							originMobileNo;

	private String							originPincode;

	private String							originCountryCode;

	private BigDecimal							destAddressId;

	private String							destAddressLine1;

	private String							destAddressLine2;

	private String							destLandmark;

	private String							destDistrict;

	private BigDecimal							destMobileNo;

	private String							destPincode;

	private String							destCountryCode;

	private List<ShipmentStatusHistoryDto>	history;
	
	private String							origAddressFullName;

	private String							destAddressFullName;

	
	public Long getShipmentId() {
		return shipmentId;
	}
	
	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}
	
	public String getLogisticPartnerRefNum() {
		return logisticPartnerRefNum;
	}
	
	public void setLogisticPartnerRefNum(String logisticPartnerRefNum) {
		this.logisticPartnerRefNum = logisticPartnerRefNum;
	}

	public BigDecimal getOriginAddressId() {
		return originAddressId;
	}
	
	public void setOriginAddressId(BigDecimal originAddressId) {
		this.originAddressId = originAddressId;
	}
	
	public String getOriginAddressLine1() {
		return originAddressLine1;
	}

	public void setOriginAddressLine1(String originAddressLine1) {
		this.originAddressLine1 = originAddressLine1;
	}
	
	public String getOriginAddressLine2() {
		return originAddressLine2;
	}
	
	public void setOriginAddressLine2(String originAddressLine2) {
		this.originAddressLine2 = originAddressLine2;
	}
	
	public String getOriginLandmark() {
		return originLandmark;
	}
	
	public void setOriginLandmark(String originLandmark) {
		this.originLandmark = originLandmark;
	}
	
	public String getOriginDistrict() {
		return originDistrict;
	}
	
	public void setOriginDistrict(String originDistrict) {
		this.originDistrict = originDistrict;
	}
	
	public BigDecimal getOriginMobileNo() {
	
		return originMobileNo;
	}

	
	public void setOriginMobileNo(BigDecimal originMobileNo) {
	
		this.originMobileNo = originMobileNo;
	}

	
	public String getOriginPincode() {
	
		return originPincode;
	}

	
	public void setOriginPincode(String originPincode) {
	
		this.originPincode = originPincode;
	}

	
	public String getOriginCountryCode() {
	
		return originCountryCode;
	}

	
	public void setOriginCountryCode(String originCountryCode) {
	
		this.originCountryCode = originCountryCode;
	}

	
	public BigDecimal getDestAddressId() {
	
		return destAddressId;
	}

	
	public void setDestAddressId(BigDecimal destAddressId) {
	
		this.destAddressId = destAddressId;
	}

	
	public String getDestAddressLine1() {
	
		return destAddressLine1;
	}

	
	public void setDestAddressLine1(String destAddressLine1) {
	
		this.destAddressLine1 = destAddressLine1;
	}

	
	public String getDestAddressLine2() {
	
		return destAddressLine2;
	}

	
	public void setDestAddressLine2(String destAddressLine2) {
	
		this.destAddressLine2 = destAddressLine2;
	}

	
	public String getDestLandmark() {
	
		return destLandmark;
	}

	
	public void setDestLandmark(String destLandmark) {
	
		this.destLandmark = destLandmark;
	}
	
	public String getDestDistrict() {
		return destDistrict;
	}
	
	public void setDestDistrict(String destDistrict) {
		this.destDistrict = destDistrict;
	}
	
	public BigDecimal getDestMobileNo() {
		return destMobileNo;
	}

	public void setDestMobileNo(BigDecimal destMobileNo) {
		this.destMobileNo = destMobileNo;
	}
	
	public String getDestPincode() {
		return destPincode;
	}
	
	public void setDestPincode(String destPincode) {
		this.destPincode = destPincode;
	}
	
	public String getDestCountryCode() {
		return destCountryCode;
	}
	
	public void setDestCountryCode(String destCountryCode) {
		this.destCountryCode = destCountryCode;
	}
	
	public List<ShipmentStatusHistoryDto> getHistory() {
		return history;
	}
	
	public void setHistory(List<ShipmentStatusHistoryDto> history) {
		this.history = history;
	}

	public String getOrigAddressFullName() {
		return origAddressFullName;
	}

	public void setOrigAddressFullName(String origAddressFullName) {
		this.origAddressFullName = origAddressFullName;
	}
	
	public String getDestAddressFullName() {
		return destAddressFullName;
	}
	
	public void setDestAddressFullName(String destAddressFullName) {
		this.destAddressFullName = destAddressFullName;
	}

	@Override
	public String toString() {
		return "ShipmentTrackingResponseDto [shipmentId=" + shipmentId + ", logisticPartnerRefNum="
				+ logisticPartnerRefNum + ", originAddressId=" + originAddressId + ", originAddressLine1="
				+ originAddressLine1 + ", originAddressLine2=" + originAddressLine2 + ", originLandmark="
				+ originLandmark + ", originDistrict=" + originDistrict + ", originMobileNo=" + originMobileNo
				+ ", originPincode=" + originPincode + ", originCountryCode=" + originCountryCode + ", destAddressId="
				+ destAddressId + ", destAddressLine1=" + destAddressLine1 + ", destAddressLine2=" + destAddressLine2
				+ ", destLandmark=" + destLandmark + ", destDistrict=" + destDistrict + ", destMobileNo=" + destMobileNo
				+ ", destPincode=" + destPincode + ", destCountryCode=" + destCountryCode + ", history=" + history
				+ ", origAddressFullName=" + origAddressFullName + ", destAddressFullName=" + destAddressFullName + "]";
	}	
	
}
