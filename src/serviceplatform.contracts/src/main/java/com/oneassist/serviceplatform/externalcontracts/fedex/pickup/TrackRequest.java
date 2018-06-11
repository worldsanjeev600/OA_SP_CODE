package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.math.BigInteger;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "TrackRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrackRequest")
public class TrackRequest {

    @XmlElement(name = "WebAuthenticationDetail", required = true)
    protected WebAuthenticationDetail webAuthenticationDetail;
    @XmlElement(name = "ClientDetail", required = true)
    protected ClientDetail clientDetail;
    @XmlElement(name = "TransactionDetail")
    protected TransactionDetail transactionDetail;
    @XmlElement(name = "Version", required = true)
    protected VersionId version;
    @XmlElement(name = "SelectionDetails")
    protected List<TrackSelectionDetail> selectionDetails;
    @XmlElement(name = "TransactionTimeOutValueInMilliseconds")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger transactionTimeOutValueInMilliseconds;

    public WebAuthenticationDetail getWebAuthenticationDetail() {
        return webAuthenticationDetail;
    }

    public void setWebAuthenticationDetail(WebAuthenticationDetail webAuthenticationDetail) {
        this.webAuthenticationDetail = webAuthenticationDetail;
    }

    public ClientDetail getClientDetail() {
        return clientDetail;
    }

    public void setClientDetail(ClientDetail clientDetail) {
        this.clientDetail = clientDetail;
    }

    public TransactionDetail getTransactionDetail() {
        return transactionDetail;
    }

    public void setTransactionDetail(TransactionDetail transactionDetail) {
        this.transactionDetail = transactionDetail;
    }

    public VersionId getVersion() {
        return version;
    }

    public void setVersion(VersionId version) {
        this.version = version;
    }

    public List<TrackSelectionDetail> getSelectionDetails() {
        return selectionDetails;
    }

    public void setSelectionDetails(List<TrackSelectionDetail> selectionDetails) {
        this.selectionDetails = selectionDetails;
    }

    public BigInteger getTransactionTimeOutValueInMilliseconds() {
        return transactionTimeOutValueInMilliseconds;
    }

    public void setTransactionTimeOutValueInMilliseconds(BigInteger transactionTimeOutValueInMilliseconds) {
        this.transactionTimeOutValueInMilliseconds = transactionTimeOutValueInMilliseconds;
    }

    @Override
    public String toString() {
        return "TrackRequest [webAuthenticationDetail=" + webAuthenticationDetail + ", clientDetail=" + clientDetail + ", transactionDetail=" + transactionDetail + ", version=" + version
                + ", selectionDetails=" + selectionDetails + ", transactionTimeOutValueInMilliseconds=" + transactionTimeOutValueInMilliseconds + "]";
    }

}
