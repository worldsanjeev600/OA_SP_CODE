package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LabelSpecification")
public class LabelSpecification {

    @XmlElement(name = "LabelFormatType", required = true)
    protected String labelFormatType;
    @XmlElement(name = "ImageType")
    protected String imageType;
    @XmlElement(name = "LabelStockType")
    protected String labelStockType;
    @XmlElement(name = "LabelPrintingOrientation")
    protected String labelPrintingOrientation;
    @XmlElement(name = "PrintedLabelOrigin")
    protected ContactAndAddress printedLabelOrigin;

    public String getLabelFormatType() {
        return this.labelFormatType;
    }

    public void setLabelFormatType(String value) {
        this.labelFormatType = value;
    }

    public String getImageType() {
        return this.imageType;
    }

    public void setImageType(String value) {
        this.imageType = value;
    }

    public String getLabelStockType() {
        return this.labelStockType;
    }

    public void setLabelStockType(String value) {
        this.labelStockType = value;
    }

    public String getLabelPrintingOrientation() {
        return this.labelPrintingOrientation;
    }

    public void setLabelPrintingOrientation(String value) {
        this.labelPrintingOrientation = value;
    }

    public ContactAndAddress getPrintedLabelOrigin() {
        return this.printedLabelOrigin;
    }

    public void setPrintedLabelOrigin(ContactAndAddress value) {
        this.printedLabelOrigin = value;
    }

    @Override
    public String toString() {
        return "LabelSpecification [ labelFormatType=" + labelFormatType + ", imageType=" + imageType + ", labelStockType=" + labelStockType + ", labelPrintingOrientation=" + labelPrintingOrientation
                + ", printedLabelOrigin=" + printedLabelOrigin + "]";
    }

}
