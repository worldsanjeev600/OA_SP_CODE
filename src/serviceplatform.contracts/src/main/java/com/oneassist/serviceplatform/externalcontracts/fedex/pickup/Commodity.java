package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Commodity")
public class Commodity {

    @XmlElement(name = "CommodityId")
    protected String commodityId;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "NumberOfPieces")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger numberOfPieces;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "CountryOfManufacture")
    protected String countryOfManufacture;
    @XmlElement(name = "HarmonizedCode")
    protected String harmonizedCode;
    @XmlElement(name = "Weight")
    protected Weight weight;
    @XmlElement(name = "Quantity")
    protected BigDecimal quantity;
    @XmlElement(name = "QuantityUnits")
    protected String quantityUnits;
    @XmlElement(name = "UnitPrice")
    protected Money unitPrice;
    @XmlElement(name = "CustomsValue")
    protected Money customsValue;
    @XmlElement(name = "ExportLicenseNumber")
    protected String exportLicenseNumber;
    @XmlElement(name = "ExportLicenseExpirationDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar exportLicenseExpirationDate;
    @XmlElement(name = "CIMarksAndNumbers")
    protected String ciMarksAndNumbers;
    @XmlElement(name = "PartNumber")
    protected String partNumber;

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getNumberOfPieces() {
        return numberOfPieces;
    }

    public void setNumberOfPieces(BigInteger numberOfPieces) {
        this.numberOfPieces = numberOfPieces;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountryOfManufacture() {
        return countryOfManufacture;
    }

    public void setCountryOfManufacture(String countryOfManufacture) {
        this.countryOfManufacture = countryOfManufacture;
    }

    public String getHarmonizedCode() {
        return harmonizedCode;
    }

    public void setHarmonizedCode(String harmonizedCode) {
        this.harmonizedCode = harmonizedCode;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getQuantityUnits() {
        return quantityUnits;
    }

    public void setQuantityUnits(String quantityUnits) {
        this.quantityUnits = quantityUnits;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Money unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Money getCustomsValue() {
        return customsValue;
    }

    public void setCustomsValue(Money customsValue) {
        this.customsValue = customsValue;
    }

    public String getExportLicenseNumber() {
        return exportLicenseNumber;
    }

    public void setExportLicenseNumber(String exportLicenseNumber) {
        this.exportLicenseNumber = exportLicenseNumber;
    }

    public XMLGregorianCalendar getExportLicenseExpirationDate() {
        return exportLicenseExpirationDate;
    }

    public void setExportLicenseExpirationDate(XMLGregorianCalendar exportLicenseExpirationDate) {
        this.exportLicenseExpirationDate = exportLicenseExpirationDate;
    }

    public String getCiMarksAndNumbers() {
        return ciMarksAndNumbers;
    }

    public void setCiMarksAndNumbers(String ciMarksAndNumbers) {
        this.ciMarksAndNumbers = ciMarksAndNumbers;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    @Override
    public String toString() {
        return "Commodity [commodityId=" + commodityId + ", name=" + name + ", numberOfPieces=" + numberOfPieces + ", description=" + description + ", countryOfManufacture=" + countryOfManufacture
                + ", harmonizedCode=" + harmonizedCode + ", weight=" + weight + ", quantity=" + quantity + ", quantityUnits=" + quantityUnits + ", unitPrice=" + unitPrice + ", customsValue="
                + customsValue + ", exportLicenseNumber=" + exportLicenseNumber + ", exportLicenseExpirationDate=" + exportLicenseExpirationDate + ", ciMarksAndNumbers=" + ciMarksAndNumbers
                + ", partNumber=" + partNumber + "]";
    }

}
