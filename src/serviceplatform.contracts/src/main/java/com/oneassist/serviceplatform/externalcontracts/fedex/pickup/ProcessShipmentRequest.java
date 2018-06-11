package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ProcessShipmentRequest", namespace = "http://fedex.com/ws/ship/v19")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcessShipmentRequest")
public class ProcessShipmentRequest {

    @XmlElement(name = "WebAuthenticationDetail", required = true)
    protected WebAuthenticationDetail webAuthenticationDetail;
    @XmlElement(name = "ClientDetail", required = true)
    protected ClientDetail clientDetail;
    @XmlElement(name = "TransactionDetail")
    protected TransactionDetail transactionDetail;
    @XmlElement(name = "Version", required = true)
    protected VersionId version;
    @XmlElement(name = "RequestedShipment", required = true)
    protected RequestedShipment requestedShipment;

    public WebAuthenticationDetail getWebAuthenticationDetail() {
        return this.webAuthenticationDetail;
    }

    public void setWebAuthenticationDetail(WebAuthenticationDetail value) {
        this.webAuthenticationDetail = value;
    }

    public ClientDetail getClientDetail() {
        return this.clientDetail;
    }

    public void setClientDetail(ClientDetail value) {
        this.clientDetail = value;
    }

    public TransactionDetail getTransactionDetail() {
        return this.transactionDetail;
    }

    public void setTransactionDetail(TransactionDetail value) {
        this.transactionDetail = value;
    }

    public VersionId getVersion() {
        return this.version;
    }

    public void setVersion(VersionId value) {
        this.version = value;
    }

    public RequestedShipment getRequestedShipment() {
        return this.requestedShipment;
    }

    public void setRequestedShipment(RequestedShipment value) {
        this.requestedShipment = value;
    }

    @Override
    public String toString() {
        return

        "ProcessShipmentRequest [webAuthenticationDetail=" + this.webAuthenticationDetail + ", clientDetail=" + this.clientDetail + ", transactionDetail=" + this.transactionDetail + ", version="
                + this.version + ", requestedShipment=" + this.requestedShipment + "]";
    }
}
