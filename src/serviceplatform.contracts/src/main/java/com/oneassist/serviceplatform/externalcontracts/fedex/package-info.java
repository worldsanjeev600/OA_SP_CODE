/**
 * 
 */
/**
 * @author priya.prakash
 *
 */
@XmlSchema(namespace = "http://schemas.xmlsoap.org/soap/envelope/", elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED, xmlns = {
        @XmlNs(prefix = "SOAP-ENV", namespaceURI = "http://schemas.xmlsoap.org/soap/envelope/"), @XmlNs(prefix = "", namespaceURI = "http://fedex.com/ws/pickup/v13"),
        @XmlNs(prefix = "", namespaceURI = "http://fedex.com/ws/ship/v19"), @XmlNs(prefix = "v12", namespaceURI = "http://fedex.com/ws/track/v12") })
package com.oneassist.serviceplatform.externalcontracts.fedex;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;

