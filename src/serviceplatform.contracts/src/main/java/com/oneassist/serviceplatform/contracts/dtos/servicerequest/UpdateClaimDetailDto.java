package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.oneassist.serviceplatform.externalcontracts.PartnerBusinessUnit;

public class UpdateClaimDetailDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8843149974161264839L;

    private Long accountNo;

    private Long memId;

    private String prodCode;

    private Long planCode;

    private String mobMake;

    private String mobModel;

    private String mobPurchageDate;

    private Double invoiceValue;

    private String imei;

    private Long custId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String dob;

    private String emailid;

    private Long mobile1;

    private Long mobile2;

    private Long addressId;

    private Long customerId;

    private String addressType;

    private String addressLine1;

    private String addressLine2;

    private String landmark;

    private String city;

    private String district;

    private String state;

    private String countryCode;

    private String pincode;

    private String stdCode;

    private Long telNo;

    private Long faxNo;

    private String commAddr;
    private String pickUpAddr;

    private String createdBy;

    private String createdDate;

    private String modifiedBy;

    private String modifiedDate;

    private String isMailingAddress;

    private String applDate;

    private Long mobileNo;

    private String status;

    private Long ocdClaimIdTmp;

    private Long ocdClaimId;

    private Long ocdSvcId; // claimType

    private Long ocdCustId;

    private Long ocdAccountNo;

    private Long ocdHubId;

    private Long ocdServiceCntrId;

    private Long ocdActivitiProcId;

    private String ocdClaimIntDate;

    private String ocdAssetInvoiceNo;

    private String ocdIncidentDescription;

    private String ocdIincidentDescVerifyStatus;

    private String ocdDamageLossDateTime;

    private String ocdDamageLossDateTimeVerStat;

    private String ocdVerDocAllStatus;

    private String ocdHandsetPickupDate;

    private String ocdPickupDate;

    private String ocdPickupTime;

    private Double ocdEstimateAmt;

    private String ocdExcessAmtApproved;

    private String ocdExcessAmtReceived;

    private String ocdRepairDueDate;

    private String ocdDelvDueDate;

    private String ocdActRepairDate;

    private String ocdActDelvDate;

    private String ocdIcDecision;

    private Double ocdAmtToCust;

    private Double ocdIcExcessAmt;

    private Double ocdIcSalvageAmt;

    private Double ocdIcMarketValue;

    private Double ocdIcDepriciation;

    private Double ocdIcReinstallPrem;

    private Double ocdIcUnderInsur;

    private String ocdAllDocSentIc;

    private String ocdIcPaymentConfm;

    private Double ocdIcPaymentAmt;

    private String ocdIcPaymentDate;

    private Map<String, String> ocdClaimAssetAttributes;

    private String ocdClaimStatus;

    private String ocdCreatedDate;

    private String ocdCreatedBy;

    private String ocdModifiedBy;

    private String ocdSpoorsPickupStatus;

    private Long serviceId;

    private Long partnerCode;

    private String planServiceName;

    private Long claimCount;
    private String repairable;

    private Map<String, String> icRejectReasonCodesMap;

    private String icRejectReason;

    private Map<String, String> reasonCodeMap;

    private List<CMSDocumentDetail> claimDocumentDetails;

    private Map<String, Object> claimMobileDamageDetails;

    private Map<String, Object> claimDeviceBreakDownDetails;

    private Map<String, Object> claimMobileLossDetails;

    private List<CMSActivitiInfo> taskHistory;

    private String commCity;

    private String commState;

    private String commPincode;

    private String claimType;

    private String damageLossTime;

    private String damageLossDate;

    private String hardCopyDocSentToICDate;

    private String activitiProcessDefinitionKey;

    private Double sumAssured;

    private List<String> damageTypeList;

    private String ocdDamageLossDateTimeVerRemarks;
    private String ocdIincidentDescVerRemarks;
    private String icIntimationFlag;
    private String startDate;
    private Double ocdIcEstimateAmount;
    private Long pickupAddressId;
    private String closeClaimReasonCode;
    private Double amtPaidByOneassistToASC;
    private Long ocdFulfilmentHubId;
    private Long ocdPickUpPartnerId;
    private Long ocdDeliveryPartnerId;

    private String assignee;
    private Long businessPartnerCode;
    private String partnerBUCode;
    private String requirementTriggered;
    private String docsRecievedViaEmail;
    private String docsNeverUploaded;
    private String verificationNewCases;
    private String assigneeDefault;

    private String deliveryICApproved;
    private String deliveryICBerApproved;
    private String deliveryICRejected;
    private Map<String, Object> claimPartnerAttributesdetails;
    private String cloudStorageArchiveId;
    private String cloudStorageJobStatus;

    private String courtesyRequired;
    private Map<String, String> qcPickup;
    private Map<String, String> qcRepair;
    private CMSPickupQcDetail claimPickupQcDetails;
    private CMSRepairQcDetail claimRepairQcDetails;
    private String ocdIncidentDisposition;
    private String ocddDocUploadStatus;
    private Long mobileId;
    private String deviceWarranty;
    private String stageName;
    private String rowId;
    private List<PartnerBusinessUnit> partnerBusinessUnitList;
    private List<String> srHistoryList;
    private String deliveryToAscStatus;
    private String pickupFromAscStatus;
    private String tat;
    private Integer tatValue;
    private String ascHub;
    private String estimationStatus;
    private String estimationCompletedDate;
    private Double mu;
    private Double sigma;
    private String expectedDeliveryDateForm;
    private String expectedDeliveryDateTo;
    private String modelVersion;
    private String muSigmaUpdatedDate;
    private String verificationSubStatus;
    private String admissibilityDecision;
    private Long icApprovalAssignee;
    private String icApprovalSubStatus;
    private String admissibilityStartedDate;
    private String admissibilityCompletedDate;
    private Long admissibilityAssignee;
    private String icApprovalCompletedDate;
    private String icApprovalDecision;

    public Long getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(Long accountNo) {
        this.accountNo = accountNo;
    }

    public Long getMemId() {
        return memId;
    }

    public void setMemId(Long memId) {
        this.memId = memId;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public Long getPlanCode() {
        return planCode;
    }

    public void setPlanCode(Long planCode) {
        this.planCode = planCode;
    }

    public String getMobMake() {
        return mobMake;
    }

    public void setMobMake(String mobMake) {
        this.mobMake = mobMake;
    }

    public String getMobModel() {
        return mobModel;
    }

    public void setMobModel(String mobModel) {
        this.mobModel = mobModel;
    }

    public String getMobPurchageDate() {
        return mobPurchageDate;
    }

    public void setMobPurchageDate(String mobPurchageDate) {
        this.mobPurchageDate = mobPurchageDate;
    }

    public Double getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(Double invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public Long getMobile1() {
        return mobile1;
    }

    public void setMobile1(Long mobile1) {
        this.mobile1 = mobile1;
    }

    public Long getMobile2() {
        return mobile2;
    }

    public void setMobile2(Long mobile2) {
        this.mobile2 = mobile2;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getStdCode() {
        return stdCode;
    }

    public void setStdCode(String stdCode) {
        this.stdCode = stdCode;
    }

    public Long getTelNo() {
        return telNo;
    }

    public void setTelNo(Long telNo) {
        this.telNo = telNo;
    }

    public Long getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(Long faxNo) {
        this.faxNo = faxNo;
    }

    public String getCommAddr() {
        return commAddr;
    }

    public void setCommAddr(String commAddr) {
        this.commAddr = commAddr;
    }

    public String getPickUpAddr() {
        return pickUpAddr;
    }

    public void setPickUpAddr(String pickUpAddr) {
        this.pickUpAddr = pickUpAddr;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getIsMailingAddress() {
        return isMailingAddress;
    }

    public void setIsMailingAddress(String isMailingAddress) {
        this.isMailingAddress = isMailingAddress;
    }

    public String getApplDate() {
        return applDate;
    }

    public void setApplDate(String applDate) {
        this.applDate = applDate;
    }

    public Long getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(Long mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getOcdClaimIdTmp() {
        return ocdClaimIdTmp;
    }

    public void setOcdClaimIdTmp(Long ocdClaimIdTmp) {
        this.ocdClaimIdTmp = ocdClaimIdTmp;
    }

    public Long getOcdClaimId() {
        return ocdClaimId;
    }

    public void setOcdClaimId(Long ocdClaimId) {
        this.ocdClaimId = ocdClaimId;
    }

    public Long getOcdSvcId() {
        return ocdSvcId;
    }

    public void setOcdSvcId(Long ocdSvcId) {
        this.ocdSvcId = ocdSvcId;
    }

    public Long getOcdCustId() {
        return ocdCustId;
    }

    public void setOcdCustId(Long ocdCustId) {
        this.ocdCustId = ocdCustId;
    }

    public Long getOcdAccountNo() {
        return ocdAccountNo;
    }

    public void setOcdAccountNo(Long ocdAccountNo) {
        this.ocdAccountNo = ocdAccountNo;
    }

    public Long getOcdHubId() {
        return ocdHubId;
    }

    public void setOcdHubId(Long ocdHubId) {
        this.ocdHubId = ocdHubId;
    }

    public Long getOcdServiceCntrId() {
        return ocdServiceCntrId;
    }

    public void setOcdServiceCntrId(Long ocdServiceCntrId) {
        this.ocdServiceCntrId = ocdServiceCntrId;
    }

    public Long getOcdActivitiProcId() {
        return ocdActivitiProcId;
    }

    public void setOcdActivitiProcId(Long ocdActivitiProcId) {
        this.ocdActivitiProcId = ocdActivitiProcId;
    }

    public String getOcdClaimIntDate() {
        return ocdClaimIntDate;
    }

    public void setOcdClaimIntDate(String ocdClaimIntDate) {
        this.ocdClaimIntDate = ocdClaimIntDate;
    }

    public String getOcdAssetInvoiceNo() {
        return ocdAssetInvoiceNo;
    }

    public void setOcdAssetInvoiceNo(String ocdAssetInvoiceNo) {
        this.ocdAssetInvoiceNo = ocdAssetInvoiceNo;
    }

    public String getOcdIncidentDescription() {
        return ocdIncidentDescription;
    }

    public void setOcdIncidentDescription(String ocdIncidentDescription) {
        this.ocdIncidentDescription = ocdIncidentDescription;
    }

    public String getOcdIincidentDescVerifyStatus() {
        return ocdIincidentDescVerifyStatus;
    }

    public void setOcdIincidentDescVerifyStatus(String ocdIincidentDescVerifyStatus) {
        this.ocdIincidentDescVerifyStatus = ocdIincidentDescVerifyStatus;
    }

    public String getOcdDamageLossDateTime() {
        return ocdDamageLossDateTime;
    }

    public void setOcdDamageLossDateTime(String ocdDamageLossDateTime) {
        this.ocdDamageLossDateTime = ocdDamageLossDateTime;
    }

    public String getOcdDamageLossDateTimeVerStat() {
        return ocdDamageLossDateTimeVerStat;
    }

    public void setOcdDamageLossDateTimeVerStat(String ocdDamageLossDateTimeVerStat) {
        this.ocdDamageLossDateTimeVerStat = ocdDamageLossDateTimeVerStat;
    }

    public String getOcdVerDocAllStatus() {
        return ocdVerDocAllStatus;
    }

    public void setOcdVerDocAllStatus(String ocdVerDocAllStatus) {
        this.ocdVerDocAllStatus = ocdVerDocAllStatus;
    }

    public String getOcdHandsetPickupDate() {
        return ocdHandsetPickupDate;
    }

    public void setOcdHandsetPickupDate(String ocdHandsetPickupDate) {
        this.ocdHandsetPickupDate = ocdHandsetPickupDate;
    }

    public String getOcdPickupDate() {
        return ocdPickupDate;
    }

    public void setOcdPickupDate(String ocdPickupDate) {
        this.ocdPickupDate = ocdPickupDate;
    }

    public String getOcdPickupTime() {
        return ocdPickupTime;
    }

    public void setOcdPickupTime(String ocdPickupTime) {
        this.ocdPickupTime = ocdPickupTime;
    }

    public Double getOcdEstimateAmt() {
        return ocdEstimateAmt;
    }

    public void setOcdEstimateAmt(Double ocdEstimateAmt) {
        this.ocdEstimateAmt = ocdEstimateAmt;
    }

    public String getOcdExcessAmtApproved() {
        return ocdExcessAmtApproved;
    }

    public void setOcdExcessAmtApproved(String ocdExcessAmtApproved) {
        this.ocdExcessAmtApproved = ocdExcessAmtApproved;
    }

    public String getOcdExcessAmtReceived() {
        return ocdExcessAmtReceived;
    }

    public void setOcdExcessAmtReceived(String ocdExcessAmtReceived) {
        this.ocdExcessAmtReceived = ocdExcessAmtReceived;
    }

    public String getOcdRepairDueDate() {
        return ocdRepairDueDate;
    }

    public void setOcdRepairDueDate(String ocdRepairDueDate) {
        this.ocdRepairDueDate = ocdRepairDueDate;
    }

    public String getOcdDelvDueDate() {
        return ocdDelvDueDate;
    }

    public void setOcdDelvDueDate(String ocdDelvDueDate) {
        this.ocdDelvDueDate = ocdDelvDueDate;
    }

    public String getOcdActRepairDate() {
        return ocdActRepairDate;
    }

    public void setOcdActRepairDate(String ocdActRepairDate) {
        this.ocdActRepairDate = ocdActRepairDate;
    }

    public String getOcdActDelvDate() {
        return ocdActDelvDate;
    }

    public void setOcdActDelvDate(String ocdActDelvDate) {
        this.ocdActDelvDate = ocdActDelvDate;
    }

    public String getOcdIcDecision() {
        return ocdIcDecision;
    }

    public void setOcdIcDecision(String ocdIcDecision) {
        this.ocdIcDecision = ocdIcDecision;
    }

    public Double getOcdAmtToCust() {
        return ocdAmtToCust;
    }

    public void setOcdAmtToCust(Double ocdAmtToCust) {
        this.ocdAmtToCust = ocdAmtToCust;
    }

    public Double getOcdIcExcessAmt() {
        return ocdIcExcessAmt;
    }

    public void setOcdIcExcessAmt(Double ocdIcExcessAmt) {
        this.ocdIcExcessAmt = ocdIcExcessAmt;
    }

    public Double getOcdIcSalvageAmt() {
        return ocdIcSalvageAmt;
    }

    public void setOcdIcSalvageAmt(Double ocdIcSalvageAmt) {
        this.ocdIcSalvageAmt = ocdIcSalvageAmt;
    }

    public Double getOcdIcMarketValue() {
        return ocdIcMarketValue;
    }

    public void setOcdIcMarketValue(Double ocdIcMarketValue) {
        this.ocdIcMarketValue = ocdIcMarketValue;
    }

    public Double getOcdIcDepriciation() {
        return ocdIcDepriciation;
    }

    public void setOcdIcDepriciation(Double ocdIcDepriciation) {
        this.ocdIcDepriciation = ocdIcDepriciation;
    }

    public Double getOcdIcReinstallPrem() {
        return ocdIcReinstallPrem;
    }

    public void setOcdIcReinstallPrem(Double ocdIcReinstallPrem) {
        this.ocdIcReinstallPrem = ocdIcReinstallPrem;
    }

    public Double getOcdIcUnderInsur() {
        return ocdIcUnderInsur;
    }

    public void setOcdIcUnderInsur(Double ocdIcUnderInsur) {
        this.ocdIcUnderInsur = ocdIcUnderInsur;
    }

    public String getOcdAllDocSentIc() {
        return ocdAllDocSentIc;
    }

    public void setOcdAllDocSentIc(String ocdAllDocSentIc) {
        this.ocdAllDocSentIc = ocdAllDocSentIc;
    }

    public String getOcdIcPaymentConfm() {
        return ocdIcPaymentConfm;
    }

    public void setOcdIcPaymentConfm(String ocdIcPaymentConfm) {
        this.ocdIcPaymentConfm = ocdIcPaymentConfm;
    }

    public Double getOcdIcPaymentAmt() {
        return ocdIcPaymentAmt;
    }

    public void setOcdIcPaymentAmt(Double ocdIcPaymentAmt) {
        this.ocdIcPaymentAmt = ocdIcPaymentAmt;
    }

    public String getOcdIcPaymentDate() {
        return ocdIcPaymentDate;
    }

    public void setOcdIcPaymentDate(String ocdIcPaymentDate) {
        this.ocdIcPaymentDate = ocdIcPaymentDate;
    }

    public Map<String, String> getOcdClaimAssetAttributes() {
        return ocdClaimAssetAttributes;
    }

    public void setOcdClaimAssetAttributes(Map<String, String> ocdClaimAssetAttributes) {
        this.ocdClaimAssetAttributes = ocdClaimAssetAttributes;
    }

    public String getOcdClaimStatus() {
        return ocdClaimStatus;
    }

    public void setOcdClaimStatus(String ocdClaimStatus) {
        this.ocdClaimStatus = ocdClaimStatus;
    }

    public String getOcdCreatedDate() {
        return ocdCreatedDate;
    }

    public void setOcdCreatedDate(String ocdCreatedDate) {
        this.ocdCreatedDate = ocdCreatedDate;
    }

    public String getOcdCreatedBy() {
        return ocdCreatedBy;
    }

    public void setOcdCreatedBy(String ocdCreatedBy) {
        this.ocdCreatedBy = ocdCreatedBy;
    }

    public String getOcdModifiedBy() {
        return ocdModifiedBy;
    }

    public void setOcdModifiedBy(String ocdModifiedBy) {
        this.ocdModifiedBy = ocdModifiedBy;
    }

    public String getOcdSpoorsPickupStatus() {
        return ocdSpoorsPickupStatus;
    }

    public void setOcdSpoorsPickupStatus(String ocdSpoorsPickupStatus) {
        this.ocdSpoorsPickupStatus = ocdSpoorsPickupStatus;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(Long partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getPlanServiceName() {
        return planServiceName;
    }

    public void setPlanServiceName(String planServiceName) {
        this.planServiceName = planServiceName;
    }

    public Long getClaimCount() {
        return claimCount;
    }

    public void setClaimCount(Long claimCount) {
        this.claimCount = claimCount;
    }

    public String getRepairable() {
        return repairable;
    }

    public void setRepairable(String repairable) {
        this.repairable = repairable;
    }

    public Map<String, String> getIcRejectReasonCodesMap() {
        return icRejectReasonCodesMap;
    }

    public void setIcRejectReasonCodesMap(Map<String, String> icRejectReasonCodesMap) {
        this.icRejectReasonCodesMap = icRejectReasonCodesMap;
    }

    public String getIcRejectReason() {
        return icRejectReason;
    }

    public void setIcRejectReason(String icRejectReason) {
        this.icRejectReason = icRejectReason;
    }

    public Map<String, String> getReasonCodeMap() {
        return reasonCodeMap;
    }

    public void setReasonCodeMap(Map<String, String> reasonCodeMap) {
        this.reasonCodeMap = reasonCodeMap;
    }

    public List<CMSDocumentDetail> getClaimDocumentDetails() {
        return claimDocumentDetails;
    }

    public void setClaimDocumentDetails(List<CMSDocumentDetail> claimDocumentDetails) {
        this.claimDocumentDetails = claimDocumentDetails;
    }

    public Map<String, Object> getClaimMobileDamageDetails() {
        return claimMobileDamageDetails;
    }

    public void setClaimMobileDamageDetails(Map<String, Object> claimMobileDamageDetails) {
        this.claimMobileDamageDetails = claimMobileDamageDetails;
    }

    public Map<String, Object> getClaimDeviceBreakDownDetails() {
        return claimDeviceBreakDownDetails;
    }

    public void setClaimDeviceBreakDownDetails(Map<String, Object> claimDeviceBreakDownDetails) {
        this.claimDeviceBreakDownDetails = claimDeviceBreakDownDetails;
    }

    public Map<String, Object> getClaimMobileLossDetails() {
        return claimMobileLossDetails;
    }

    public void setClaimMobileLossDetails(Map<String, Object> claimMobileLossDetails) {
        this.claimMobileLossDetails = claimMobileLossDetails;
    }

    public List<CMSActivitiInfo> getTaskHistory() {
        return taskHistory;
    }

    public void setTaskHistory(List<CMSActivitiInfo> taskHistory) {
        this.taskHistory = taskHistory;
    }

    public String getCommCity() {
        return commCity;
    }

    public void setCommCity(String commCity) {
        this.commCity = commCity;
    }

    public String getCommState() {
        return commState;
    }

    public void setCommState(String commState) {
        this.commState = commState;
    }

    public String getCommPincode() {
        return commPincode;
    }

    public void setCommPincode(String commPincode) {
        this.commPincode = commPincode;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getDamageLossTime() {
        return damageLossTime;
    }

    public void setDamageLossTime(String damageLossTime) {
        this.damageLossTime = damageLossTime;
    }

    public String getDamageLossDate() {
        return damageLossDate;
    }

    public void setDamageLossDate(String damageLossDate) {
        this.damageLossDate = damageLossDate;
    }

    public String getHardCopyDocSentToICDate() {
        return hardCopyDocSentToICDate;
    }

    public void setHardCopyDocSentToICDate(String hardCopyDocSentToICDate) {
        this.hardCopyDocSentToICDate = hardCopyDocSentToICDate;
    }

    public String getActivitiProcessDefinitionKey() {
        return activitiProcessDefinitionKey;
    }

    public void setActivitiProcessDefinitionKey(String activitiProcessDefinitionKey) {
        this.activitiProcessDefinitionKey = activitiProcessDefinitionKey;
    }

    public Double getSumAssured() {
        return sumAssured;
    }

    public void setSumAssured(Double sumAssured) {
        this.sumAssured = sumAssured;
    }

    public List<String> getDamageTypeList() {
        return damageTypeList;
    }

    public void setDamageTypeList(List<String> damageTypeList) {
        this.damageTypeList = damageTypeList;
    }

    public String getOcdDamageLossDateTimeVerRemarks() {
        return ocdDamageLossDateTimeVerRemarks;
    }

    public void setOcdDamageLossDateTimeVerRemarks(String ocdDamageLossDateTimeVerRemarks) {
        this.ocdDamageLossDateTimeVerRemarks = ocdDamageLossDateTimeVerRemarks;
    }

    public String getOcdIincidentDescVerRemarks() {
        return ocdIincidentDescVerRemarks;
    }

    public void setOcdIincidentDescVerRemarks(String ocdIincidentDescVerRemarks) {
        this.ocdIincidentDescVerRemarks = ocdIincidentDescVerRemarks;
    }

    public String getIcIntimationFlag() {
        return icIntimationFlag;
    }

    public void setIcIntimationFlag(String icIntimationFlag) {
        this.icIntimationFlag = icIntimationFlag;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Double getOcdIcEstimateAmount() {
        return ocdIcEstimateAmount;
    }

    public void setOcdIcEstimateAmount(Double ocdIcEstimateAmount) {
        this.ocdIcEstimateAmount = ocdIcEstimateAmount;
    }

    public Long getPickupAddressId() {
        return pickupAddressId;
    }

    public void setPickupAddressId(Long pickupAddressId) {
        this.pickupAddressId = pickupAddressId;
    }

    public String getCloseClaimReasonCode() {
        return closeClaimReasonCode;
    }

    public void setCloseClaimReasonCode(String closeClaimReasonCode) {
        this.closeClaimReasonCode = closeClaimReasonCode;
    }

    public Double getAmtPaidByOneassistToASC() {
        return amtPaidByOneassistToASC;
    }

    public void setAmtPaidByOneassistToASC(Double amtPaidByOneassistToASC) {
        this.amtPaidByOneassistToASC = amtPaidByOneassistToASC;
    }

    public Long getOcdFulfilmentHubId() {
        return ocdFulfilmentHubId;
    }

    public void setOcdFulfilmentHubId(Long ocdFulfilmentHubId) {
        this.ocdFulfilmentHubId = ocdFulfilmentHubId;
    }

    public Long getOcdPickUpPartnerId() {
        return ocdPickUpPartnerId;
    }

    public void setOcdPickUpPartnerId(Long ocdPickUpPartnerId) {
        this.ocdPickUpPartnerId = ocdPickUpPartnerId;
    }

    public Long getOcdDeliveryPartnerId() {
        return ocdDeliveryPartnerId;
    }

    public void setOcdDeliveryPartnerId(Long ocdDeliveryPartnerId) {
        this.ocdDeliveryPartnerId = ocdDeliveryPartnerId;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Long getBusinessPartnerCode() {
        return businessPartnerCode;
    }

    public void setBusinessPartnerCode(Long businessPartnerCode) {
        this.businessPartnerCode = businessPartnerCode;
    }

    public String getPartnerBUCode() {
        return partnerBUCode;
    }

    public void setPartnerBUCode(String partnerBUCode) {
        this.partnerBUCode = partnerBUCode;
    }

    public String getRequirementTriggered() {
        return requirementTriggered;
    }

    public void setRequirementTriggered(String requirementTriggered) {
        this.requirementTriggered = requirementTriggered;
    }

    public String getDocsRecievedViaEmail() {
        return docsRecievedViaEmail;
    }

    public void setDocsRecievedViaEmail(String docsRecievedViaEmail) {
        this.docsRecievedViaEmail = docsRecievedViaEmail;
    }

    public String getDocsNeverUploaded() {
        return docsNeverUploaded;
    }

    public void setDocsNeverUploaded(String docsNeverUploaded) {
        this.docsNeverUploaded = docsNeverUploaded;
    }

    public String getVerificationNewCases() {
        return verificationNewCases;
    }

    public void setVerificationNewCases(String verificationNewCases) {
        this.verificationNewCases = verificationNewCases;
    }

    public String getAssigneeDefault() {
        return assigneeDefault;
    }

    public void setAssigneeDefault(String assigneeDefault) {
        this.assigneeDefault = assigneeDefault;
    }

    public String getDeliveryICApproved() {
        return deliveryICApproved;
    }

    public void setDeliveryICApproved(String deliveryICApproved) {
        this.deliveryICApproved = deliveryICApproved;
    }

    public String getDeliveryICBerApproved() {
        return deliveryICBerApproved;
    }

    public void setDeliveryICBerApproved(String deliveryICBerApproved) {
        this.deliveryICBerApproved = deliveryICBerApproved;
    }

    public String getDeliveryICRejected() {
        return deliveryICRejected;
    }

    public void setDeliveryICRejected(String deliveryICRejected) {
        this.deliveryICRejected = deliveryICRejected;
    }

    public Map<String, Object> getClaimPartnerAttributesdetails() {
        return claimPartnerAttributesdetails;
    }

    public void setClaimPartnerAttributesdetails(Map<String, Object> claimPartnerAttributesdetails) {
        this.claimPartnerAttributesdetails = claimPartnerAttributesdetails;
    }

    public String getCloudStorageArchiveId() {
        return cloudStorageArchiveId;
    }

    public void setCloudStorageArchiveId(String cloudStorageArchiveId) {
        this.cloudStorageArchiveId = cloudStorageArchiveId;
    }

    public String getCloudStorageJobStatus() {
        return cloudStorageJobStatus;
    }

    public void setCloudStorageJobStatus(String cloudStorageJobStatus) {
        this.cloudStorageJobStatus = cloudStorageJobStatus;
    }

    public String getCourtesyRequired() {
        return courtesyRequired;
    }

    public void setCourtesyRequired(String courtesyRequired) {
        this.courtesyRequired = courtesyRequired;
    }

    public Map<String, String> getQcPickup() {
        return qcPickup;
    }

    public void setQcPickup(Map<String, String> qcPickup) {
        this.qcPickup = qcPickup;
    }

    public Map<String, String> getQcRepair() {
        return qcRepair;
    }

    public void setQcRepair(Map<String, String> qcRepair) {
        this.qcRepair = qcRepair;
    }

    public CMSPickupQcDetail getClaimPickupQcDetails() {
        return claimPickupQcDetails;
    }

    public void setClaimPickupQcDetails(CMSPickupQcDetail claimPickupQcDetails) {
        this.claimPickupQcDetails = claimPickupQcDetails;
    }

    public CMSRepairQcDetail getClaimRepairQcDetails() {
        return claimRepairQcDetails;
    }

    public void setClaimRepairQcDetails(CMSRepairQcDetail claimRepairQcDetails) {
        this.claimRepairQcDetails = claimRepairQcDetails;
    }

    public String getOcdIncidentDisposition() {
        return ocdIncidentDisposition;
    }

    public void setOcdIncidentDisposition(String ocdIncidentDisposition) {
        this.ocdIncidentDisposition = ocdIncidentDisposition;
    }

    public String getOcddDocUploadStatus() {
        return ocddDocUploadStatus;
    }

    public void setOcddDocUploadStatus(String ocddDocUploadStatus) {
        this.ocddDocUploadStatus = ocddDocUploadStatus;
    }

    public Long getMobileId() {
        return mobileId;
    }

    public void setMobileId(Long mobileId) {
        this.mobileId = mobileId;
    }

    public String getDeviceWarranty() {
        return deviceWarranty;
    }

    public void setDeviceWarranty(String deviceWarranty) {
        this.deviceWarranty = deviceWarranty;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public List<PartnerBusinessUnit> getPartnerBusinessUnitList() {
        return partnerBusinessUnitList;
    }

    public void setPartnerBusinessUnitList(List<PartnerBusinessUnit> partnerBusinessUnitList) {
        this.partnerBusinessUnitList = partnerBusinessUnitList;
    }

    public List<String> getSrHistoryList() {
        return srHistoryList;
    }

    public void setSrHistoryList(List<String> srHistoryList) {
        this.srHistoryList = srHistoryList;
    }

    public String getDeliveryToAscStatus() {
        return deliveryToAscStatus;
    }

    public void setDeliveryToAscStatus(String deliveryToAscStatus) {
        this.deliveryToAscStatus = deliveryToAscStatus;
    }

    public String getPickupFromAscStatus() {
        return pickupFromAscStatus;
    }

    public void setPickupFromAscStatus(String pickupFromAscStatus) {
        this.pickupFromAscStatus = pickupFromAscStatus;
    }

    public String getTat() {
        return tat;
    }

    public void setTat(String tat) {
        this.tat = tat;
    }

    public Integer getTatValue() {
        return tatValue;
    }

    public void setTatValue(Integer tatValue) {
        this.tatValue = tatValue;
    }

    public String getAscHub() {
        return ascHub;
    }

    public void setAscHub(String ascHub) {
        this.ascHub = ascHub;
    }

    public String getEstimationStatus() {
        return estimationStatus;
    }

    public void setEstimationStatus(String estimationStatus) {
        this.estimationStatus = estimationStatus;
    }

    public String getEstimationCompletedDate() {
        return estimationCompletedDate;
    }

    public void setEstimationCompletedDate(String estimationCompletedDate) {
        this.estimationCompletedDate = estimationCompletedDate;
    }

    public Double getMu() {
        return mu;
    }

    public void setMu(Double mu) {
        this.mu = mu;
    }

    public Double getSigma() {
        return sigma;
    }

    public void setSigma(Double sigma) {
        this.sigma = sigma;
    }

    public String getExpectedDeliveryDateForm() {
        return expectedDeliveryDateForm;
    }

    public void setExpectedDeliveryDateForm(String expectedDeliveryDateForm) {
        this.expectedDeliveryDateForm = expectedDeliveryDateForm;
    }

    public String getExpectedDeliveryDateTo() {
        return expectedDeliveryDateTo;
    }

    public void setExpectedDeliveryDateTo(String expectedDeliveryDateTo) {
        this.expectedDeliveryDateTo = expectedDeliveryDateTo;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getMuSigmaUpdatedDate() {
        return muSigmaUpdatedDate;
    }

    public void setMuSigmaUpdatedDate(String muSigmaUpdatedDate) {
        this.muSigmaUpdatedDate = muSigmaUpdatedDate;
    }

    public String getVerificationSubStatus() {
        return verificationSubStatus;
    }

    public void setVerificationSubStatus(String verificationSubStatus) {
        this.verificationSubStatus = verificationSubStatus;
    }

    public String getAdmissibilityDecision() {
        return admissibilityDecision;
    }

    public void setAdmissibilityDecision(String admissibilityDecision) {
        this.admissibilityDecision = admissibilityDecision;
    }

    public Long getIcApprovalAssignee() {
        return icApprovalAssignee;
    }

    public void setIcApprovalAssignee(Long icApprovalAssignee) {
        this.icApprovalAssignee = icApprovalAssignee;
    }

    public String getIcApprovalSubStatus() {
        return icApprovalSubStatus;
    }

    public void setIcApprovalSubStatus(String icApprovalSubStatus) {
        this.icApprovalSubStatus = icApprovalSubStatus;
    }

    public String getAdmissibilityStartedDate() {
        return admissibilityStartedDate;
    }

    public void setAdmissibilityStartedDate(String admissibilityStartedDate) {
        this.admissibilityStartedDate = admissibilityStartedDate;
    }

    public String getAdmissibilityCompletedDate() {
        return admissibilityCompletedDate;
    }

    public void setAdmissibilityCompletedDate(String admissibilityCompletedDate) {
        this.admissibilityCompletedDate = admissibilityCompletedDate;
    }

    public Long getAdmissibilityAssignee() {
        return admissibilityAssignee;
    }

    public void setAdmissibilityAssignee(Long admissibilityAssignee) {
        this.admissibilityAssignee = admissibilityAssignee;
    }

    public String getIcApprovalCompletedDate() {
        return icApprovalCompletedDate;
    }

    public void setIcApprovalCompletedDate(String icApprovalCompletedDate) {
        this.icApprovalCompletedDate = icApprovalCompletedDate;
    }

    public String getIcApprovalDecision() {
        return icApprovalDecision;
    }

    public void setIcApprovalDecision(String icApprovalDecision) {
        this.icApprovalDecision = icApprovalDecision;
    }

    @Override
    public String toString() {
        return "CMSClaimDetail [accountNo=" + accountNo + ", memId=" + memId + ", prodCode=" + prodCode + ", planCode=" + planCode + ", mobMake=" + mobMake + ", mobModel=" + mobModel
                + ", mobPurchageDate=" + mobPurchageDate + ", invoiceValue=" + invoiceValue + ", imei=" + imei + ", custId=" + custId + ", firstName=" + firstName + ", middleName=" + middleName
                + ", lastName=" + lastName + ", dob=" + dob + ", emailid=" + emailid + ", mobile1=" + mobile1 + ", mobile2=" + mobile2 + ", addressId=" + addressId + ", customerId=" + customerId
                + ", addressType=" + addressType + ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2 + ", landmark=" + landmark + ", city=" + city + ", district=" + district
                + ", state=" + state + ", countryCode=" + countryCode + ", pincode=" + pincode + ", stdCode=" + stdCode + ", telNo=" + telNo + ", faxNo=" + faxNo + ", commAddr=" + commAddr
                + ", pickUpAddr=" + pickUpAddr + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate + ", isMailingAddress="
                + isMailingAddress + ", applDate=" + applDate + ", mobileNo=" + mobileNo + ", status=" + status + ", ocdClaimIdTmp=" + ocdClaimIdTmp + ", ocdClaimId=" + ocdClaimId + ", ocdSvcId="
                + ocdSvcId + ", ocdCustId=" + ocdCustId + ", ocdAccountNo=" + ocdAccountNo + ", ocdHubId=" + ocdHubId + ", ocdServiceCntrId=" + ocdServiceCntrId + ", ocdActivitiProcId="
                + ocdActivitiProcId + ", ocdClaimIntDate=" + ocdClaimIntDate + ", ocdAssetInvoiceNo=" + ocdAssetInvoiceNo + ", ocdIncidentDescription=" + ocdIncidentDescription
                + ", ocdIincidentDescVerifyStatus=" + ocdIincidentDescVerifyStatus + ", ocdDamageLossDateTime=" + ocdDamageLossDateTime + ", ocdDamageLossDateTimeVerStat="
                + ocdDamageLossDateTimeVerStat + ", ocdVerDocAllStatus=" + ocdVerDocAllStatus + ", ocdHandsetPickupDate=" + ocdHandsetPickupDate + ", ocdPickupDate=" + ocdPickupDate
                + ", ocdPickupTime=" + ocdPickupTime + ", ocdEstimateAmt=" + ocdEstimateAmt + ", ocdExcessAmtApproved=" + ocdExcessAmtApproved + ", ocdExcessAmtReceived=" + ocdExcessAmtReceived
                + ", ocdRepairDueDate=" + ocdRepairDueDate + ", ocdDelvDueDate=" + ocdDelvDueDate + ", ocdActRepairDate=" + ocdActRepairDate + ", ocdActDelvDate=" + ocdActDelvDate
                + ", ocdIcDecision=" + ocdIcDecision + ", ocdAmtToCust=" + ocdAmtToCust + ", ocdIcExcessAmt=" + ocdIcExcessAmt + ", ocdIcSalvageAmt=" + ocdIcSalvageAmt + ", ocdIcMarketValue="
                + ocdIcMarketValue + ", ocdIcDepriciation=" + ocdIcDepriciation + ", ocdIcReinstallPrem=" + ocdIcReinstallPrem + ", ocdIcUnderInsur=" + ocdIcUnderInsur + ", ocdAllDocSentIc="
                + ocdAllDocSentIc + ", ocdIcPaymentConfm=" + ocdIcPaymentConfm + ", ocdIcPaymentAmt=" + ocdIcPaymentAmt + ", ocdIcPaymentDate=" + ocdIcPaymentDate + ", ocdClaimAssetAttributes="
                + ocdClaimAssetAttributes + ", ocdClaimStatus=" + ocdClaimStatus + ", ocdCreatedDate=" + ocdCreatedDate + ", ocdCreatedBy=" + ocdCreatedBy + ", ocdModifiedBy=" + ocdModifiedBy
                + ", ocdSpoorsPickupStatus=" + ocdSpoorsPickupStatus + ", serviceId=" + serviceId + ", partnerCode=" + partnerCode + ", planServiceName=" + planServiceName + ", claimCount="
                + claimCount + ", repairable=" + repairable + ", icRejectReasonCodesMap=" + icRejectReasonCodesMap + ", icRejectReason=" + icRejectReason + ", reasonCodeMap=" + reasonCodeMap
                + ", claimDocumentDetails=" + claimDocumentDetails + ", claimMobileDamageDetails=" + claimMobileDamageDetails + ", claimDeviceBreakDownDetails=" + claimDeviceBreakDownDetails
                + ", claimMobileLossDetails=" + claimMobileLossDetails + ", taskHistory=" + taskHistory + ", commCity=" + commCity + ", commState=" + commState + ", commPincode=" + commPincode
                + ", claimType=" + claimType + ", damageLossTime=" + damageLossTime + ", damageLossDate=" + damageLossDate + ", hardCopyDocSentToICDate=" + hardCopyDocSentToICDate
                + ", activitiProcessDefinitionKey=" + activitiProcessDefinitionKey + ", sumAssured=" + sumAssured + ", damageTypeList=" + damageTypeList + ", ocdDamageLossDateTimeVerRemarks="
                + ocdDamageLossDateTimeVerRemarks + ", ocdIincidentDescVerRemarks=" + ocdIincidentDescVerRemarks + ", icIntimationFlag=" + icIntimationFlag + ", startDate=" + startDate
                + ", ocdIcEstimateAmount=" + ocdIcEstimateAmount + ", pickupAddressId=" + pickupAddressId + ", closeClaimReasonCode=" + closeClaimReasonCode + ", amtPaidByOneassistToASC="
                + amtPaidByOneassistToASC + ", ocdFulfilmentHubId=" + ocdFulfilmentHubId + ", ocdPickUpPartnerId=" + ocdPickUpPartnerId + ", ocdDeliveryPartnerId=" + ocdDeliveryPartnerId
                + ", assignee=" + assignee + ", businessPartnerCode=" + businessPartnerCode + ", partnerBUCode=" + partnerBUCode + ", requirementTriggered=" + requirementTriggered
                + ", docsRecievedViaEmail=" + docsRecievedViaEmail + ", docsNeverUploaded=" + docsNeverUploaded + ", verificationNewCases=" + verificationNewCases + ", assigneeDefault="
                + assigneeDefault + ", deliveryICApproved=" + deliveryICApproved + ", deliveryICBerApproved=" + deliveryICBerApproved + ", deliveryICRejected=" + deliveryICRejected
                + ", claimPartnerAttributesdetails=" + claimPartnerAttributesdetails + ", cloudStorageArchiveId=" + cloudStorageArchiveId + ", cloudStorageJobStatus=" + cloudStorageJobStatus
                + ", courtesyRequired=" + courtesyRequired + ", qcPickup=" + qcPickup + ", qcRepair=" + qcRepair + ", claimPickupQcDetails=" + claimPickupQcDetails + ", claimRepairQcDetails="
                + claimRepairQcDetails + ", ocdIncidentDisposition=" + ocdIncidentDisposition + ", ocddDocUploadStatus=" + ocddDocUploadStatus + ", mobileId=" + mobileId + ", deviceWarranty="
                + deviceWarranty + ", stageName=" + stageName + ", rowId=" + rowId + ", partnerBusinessUnitList=" + partnerBusinessUnitList + ", srHistoryList=" + srHistoryList
                + ", deliveryToAscStatus=" + deliveryToAscStatus + ", pickupFromAscStatus=" + pickupFromAscStatus + ", tat=" + tat + ", tatValue=" + tatValue + ", ascHub=" + ascHub
                + ", estimationStatus=" + estimationStatus + ", estimationCompletedDate=" + estimationCompletedDate + ", mu=" + mu + ", sigma=" + sigma + ", expectedDeliveryDateForm="
                + expectedDeliveryDateForm + ", expectedDeliveryDateTo=" + expectedDeliveryDateTo + ", modelVersion=" + modelVersion + ", muSigmaUpdatedDate=" + muSigmaUpdatedDate
                + ", verificationSubStatus=" + verificationSubStatus + ", admissibilityDecision=" + admissibilityDecision + ", icApprovalAssignee=" + icApprovalAssignee + ", icApprovalSubStatus="
                + icApprovalSubStatus + ", admissibilityStartedDate=" + admissibilityStartedDate + ", admissibilityCompletedDate=" + admissibilityCompletedDate + ", admissibilityAssignee="
                + admissibilityAssignee + ", icApprovalCompletedDate=" + icApprovalCompletedDate + ", icApprovalDecision=" + icApprovalDecision + "]";
    }

}
