package com.oneassist.serviceplatform.commons.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author srikanth
 */
@Entity
@Table(name = "OA_SHIPMENT_DTL")
public class ShipmentEntity extends BaseAuditEntity {

    private static final long serialVersionUID = -1486576349069944611L;

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_OA_SHIPMENT_DTL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    @Column(name = "SHIPMENT_ID")
    private Long shipmentId;

    @Column(name = "SHIPMENT_TYPE")
    private Integer shipmentType;

    @Column(name = "SENDER")
    private String sender;

    @Column(name = "RECIPIENT")
    private String recipient;

    @Column(name = "SENT_BY")
    private String sentBy;

    @Column(name = "RECEIVED_BY")
    private String receivedBy;

    @Column(name = "CURRENT_STAGE")
    private String currentStage;

    @Column(name = "LOGISTIC_PARTNER_CODE")
    private String logisticPartnerCode;

    @Column(name = "LOGISTIC_PARTNER_REF_TRACK_NO")
    private String logisticPartnerRefTrackingNumber;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ORIGIN_ADDREES_ID", referencedColumnName = "SERVICE_ADDRESS_ID", insertable = true, updatable = false)
    private ServiceAddressEntity originAddressDetails;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "DESTINATION_ADDREES_ID", referencedColumnName = "SERVICE_ADDRESS_ID", insertable = true, updatable = false)
    private ServiceAddressEntity destinationAddressDetails;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "SHIPMENT_ID", referencedColumnName = "SHIPMENT_ID")
    private List<ShipmentAssetEntity> shipmentAssetDetails;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "SERVICE_REQUEST_ID", referencedColumnName = "SERVICE_REQUEST_ID", insertable = true, updatable = false)
    private ServiceRequestEntity serviceRequestDetails;;
    
    @Column(name = "SCHEDULED_PICKUP_DATE")
    private Date scheduledPickupDate;

    @Column(name = "SHIPMENT_DECLARE_VALUE")
    private Double shipmentDeclareValue;

    @Column(name = "BOX_ACTUAL_LENGTH")
    private Double boxActualLength;

    @Column(name = "BOX_ACTUAL_WIDTH")
    private Double boxActualWidth;

    @Column(name = "BOX_ACTUAL_HEIGHT")
    private Double boxActualHeight;

    @Column(name = "BOX_ACTUAL_WEIGHT")
    private Double boxActualWeight;

    @Column(name = "HUB_ID")
    private String hubId;

    @Column(name = "BOX_COUNT")
    private Integer boxCount;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "BOX_LENGTH_UNIT")
    private String boxLengthUnit;

    @Column(name = "BOX_WIDTH_UNIT")
    private String boxWidthUnit;

    @Column(name = "BOX_HEIGHT_UNIT")
    private String boxHeightUnit;

    @Column(name = "BOX_WEIGHT_UNIT")
    private String boxWeightUnit;

    @Column(name = "FAILURE_REASON")
    private String reasonForFailure;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "SHIPMENT_ID", referencedColumnName = "SHIPMENT_ID", insertable = true, updatable = false)
    private List<ShipmentStageEntity> shipmentStages;

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Integer getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(Integer shipmentType) {
        this.shipmentType = shipmentType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(String currentStage) {
        this.currentStage = currentStage;
    }

    public String getLogisticPartnerCode() {
        return logisticPartnerCode;
    }

    public void setLogisticPartnerCode(String logisticPartnerCode) {
        this.logisticPartnerCode = logisticPartnerCode;
    }

    public String getLogisticPartnerRefTrackingNumber() {
        return logisticPartnerRefTrackingNumber;
    }

    public void setLogisticPartnerRefTrackingNumber(String logisticPartnerRefTrackingNumber) {
        this.logisticPartnerRefTrackingNumber = logisticPartnerRefTrackingNumber;
    }

    public ServiceAddressEntity getOriginAddressDetails() {
        return originAddressDetails;
    }

    public void setOriginAddressDetails(ServiceAddressEntity originAddressDetails) {
        this.originAddressDetails = originAddressDetails;
    }

    public ServiceAddressEntity getDestinationAddressDetails() {
        return destinationAddressDetails;
    }

    public void setDestinationAddressDetails(ServiceAddressEntity destinationAddressDetails) {
        this.destinationAddressDetails = destinationAddressDetails;
    }

    public List<ShipmentAssetEntity> getShipmentAssetDetails() {
        return shipmentAssetDetails;
    }

    public void setShipmentAssetDetails(List<ShipmentAssetEntity> shipmentAssetDetails) {
        this.shipmentAssetDetails = shipmentAssetDetails;
    }

    public ServiceRequestEntity getServiceRequestDetails() {
        return serviceRequestDetails;
    }

    public void setServiceRequestDetails(ServiceRequestEntity serviceRequestDetails) {
        this.serviceRequestDetails = serviceRequestDetails;
    }

    public Date getScheduledPickupDate() {
        return scheduledPickupDate;
    }

    public void setScheduledPickupDate(Date scheduledPickupDate) {
        this.scheduledPickupDate = scheduledPickupDate;
    }

    public Double getShipmentDeclareValue() {
        return shipmentDeclareValue;
    }

    public void setShipmentDeclareValue(Double shipmentDeclareValue) {
        this.shipmentDeclareValue = shipmentDeclareValue;
    }

    public Double getBoxActualLength() {
        return boxActualLength;
    }

    public void setBoxActualLength(Double boxActualLength) {
        this.boxActualLength = boxActualLength;
    }

    public Double getBoxActualWidth() {
        return boxActualWidth;
    }

    public void setBoxActualWidth(Double boxActualWidth) {
        this.boxActualWidth = boxActualWidth;
    }

    public Double getBoxActualHeight() {
        return boxActualHeight;
    }

    public void setBoxActualHeight(Double boxActualHeight) {
        this.boxActualHeight = boxActualHeight;
    }

    public Double getBoxActualWeight() {
        return boxActualWeight;
    }

    public void setBoxActualWeight(Double boxActualWeight) {
        this.boxActualWeight = boxActualWeight;
    }

    public String getHubId() {
        return hubId;
    }

    public void setHubId(String hubId) {
        this.hubId = hubId;
    }

    public Integer getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(Integer boxCount) {
        this.boxCount = boxCount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBoxLengthUnit() {
        return boxLengthUnit;
    }

    public void setBoxLengthUnit(String boxLengthUnit) {
        this.boxLengthUnit = boxLengthUnit;
    }

    public String getBoxWidthUnit() {
        return boxWidthUnit;
    }

    public void setBoxWidthUnit(String boxWidthUnit) {
        this.boxWidthUnit = boxWidthUnit;
    }

    public String getBoxHeightUnit() {
        return boxHeightUnit;
    }

    public void setBoxHeightUnit(String boxHeightUnit) {
        this.boxHeightUnit = boxHeightUnit;
    }

    public String getBoxWeightUnit() {
        return boxWeightUnit;
    }

    public void setBoxWeightUnit(String boxWeightUnit) {
        this.boxWeightUnit = boxWeightUnit;
    }

    public String getReasonForFailure() {
        return reasonForFailure;
    }

    public void setReasonForFailure(String reasonForFailure) {
        this.reasonForFailure = reasonForFailure;
    }

    public List<ShipmentStageEntity> getShipmentStages() {
        return shipmentStages;
    }

    public void setShipmentStages(List<ShipmentStageEntity> shipmentStages) {
        this.shipmentStages = shipmentStages;
    }
}
