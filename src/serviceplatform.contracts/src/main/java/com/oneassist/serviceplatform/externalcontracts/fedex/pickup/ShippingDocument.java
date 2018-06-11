package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShippingDocument", namespace = "http://fedex.com/ws/ship/v19")
public class ShippingDocument {

    @XmlElement(name = "AccessReference")
    protected String accessReference;
    @XmlElement(name = "ImageType")
    protected String imageType;
    @XmlElement(name = "Resolution")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger resolution;
    @XmlElement(name = "CopiesToPrint")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger copiesToPrint;
    @XmlElement(name = "Parts")
    protected List<ShippingDocumentPart> parts;

    public String getAccessReference() {
        return this.accessReference;
    }

    public void setAccessReference(String value) {
        this.accessReference = value;
    }

    public String getImageType() {
        return this.imageType;
    }

    public void setImageType(String value) {
        this.imageType = value;
    }

    public BigInteger getResolution() {
        return this.resolution;
    }

    public void setResolution(BigInteger value) {
        this.resolution = value;
    }

    public BigInteger getCopiesToPrint() {
        return this.copiesToPrint;
    }

    public void setCopiesToPrint(BigInteger value) {
        this.copiesToPrint = value;
    }

    public List<ShippingDocumentPart> getParts() {
        if (this.parts == null) {
            this.parts = new ArrayList();
        }
        return this.parts;
    }

    @Override
    public String toString() {
        return

        "ShippingDocument [  accessReference=" + this.accessReference + ", imageType=" + this.imageType + ", resolution=" + this.resolution + ", copiesToPrint=" + this.copiesToPrint + ", parts="
                + this.parts + "]";
    }
}
