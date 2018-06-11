package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.math.BigInteger;
import java.util.Arrays;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShippingDocumentPart", namespace = "http://fedex.com/ws/ship/v19")
public class ShippingDocumentPart {

    @XmlElement(name = "DocumentPartSequenceNumber")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger documentPartSequenceNumber;
    @XmlElement(name = "Image")
    protected byte[] image;

    public BigInteger getDocumentPartSequenceNumber() {
        return this.documentPartSequenceNumber;
    }

    public void setDocumentPartSequenceNumber(BigInteger value) {
        this.documentPartSequenceNumber = value;
    }

    public byte[] getImage() {
        return this.image;
    }

    public void setImage(byte[] value) {
        this.image = value;
    }

    @Override
    public String toString() {
        return

        "ShippingDocumentPart [documentPartSequenceNumber=" + this.documentPartSequenceNumber + ", image=" + Arrays.toString(this.image) + "]";
    }
}
