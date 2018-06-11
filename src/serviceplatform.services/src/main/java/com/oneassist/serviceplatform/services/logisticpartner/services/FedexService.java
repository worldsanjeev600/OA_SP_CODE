package com.oneassist.serviceplatform.services.logisticpartner.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.proxies.FedexProxy;
import com.oneassist.serviceplatform.contracts.dtos.PartnerEventDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceAddressDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentDetailsDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentRequestDto;
import com.oneassist.serviceplatform.externalcontracts.ClaimLifecycleEvent;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;
import com.oneassist.serviceplatform.externalcontracts.ServicePartner;
import com.oneassist.serviceplatform.externalcontracts.fedex.PickupAvailabilityReplyWrapper;
import com.oneassist.serviceplatform.externalcontracts.fedex.PickupAvailabilityRequestBodyWrapper;
import com.oneassist.serviceplatform.externalcontracts.fedex.PickupAvailabilityRequestWrapper;
import com.oneassist.serviceplatform.externalcontracts.fedex.ProcessShipmentReplyWrapper;
import com.oneassist.serviceplatform.externalcontracts.fedex.ProcessShipmentRequestBodyWrapper;
import com.oneassist.serviceplatform.externalcontracts.fedex.ProcessShipmentRequestWrapper;
import com.oneassist.serviceplatform.externalcontracts.fedex.TrackShipmentResponseWrapper;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.Address;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.ClientDetail;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.CommercialInvoice;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.Commodity;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.CompletedPackageDetail;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.Contact;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.ContactAndAddress;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.CountryRelationshipType;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.CreatePickupRequest;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.CustomerReference;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.CustomerReferenceType;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.CustomsClearanceDetail;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.LabelSpecification;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.Money;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.Notification;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.Party;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.Payment;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.Payor;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.PickupAvailabilityReply;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.PickupAvailabilityRequest;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.PickupOriginDetail;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.PickupScheduleOption;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.PickupShipmentAttributes;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.ProcessShipmentReply;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.ProcessShipmentRequest;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.RequestedPackageLineItem;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.RequestedShipment;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.ShippingDocument;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.ShippingDocumentPart;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.TaxpayerIdentification;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.TrackPackageIdentifier;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.TrackReply;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.TrackRequest;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.TrackSelectionDetail;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.TrackingId;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.TransactionDetail;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.VersionId;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.WebAuthenticationCredential;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.WebAuthenticationDetail;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.Weight;
import com.oneassist.serviceplatform.services.commons.ICommonService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service("fedexService")
public class FedexService implements ILogisticPartnerService {

    final static Logger logger = Logger.getLogger(FedexService.class);

    @Autowired
    @Qualifier("commonService")
    private ICommonService commonService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FedexProxy fedexProxy;

    @Value("${SHIPMENT_DOCUMENT_FOLDER_PATH}")
    private String shipmentDocFolderPath;

    public static final String OUTBOUND_LABEL = "OUTBOUND_LABEL";
    public static final String PARTNER_ERROR = "ERROR";
    public static final String NUBER_OF_BUSINESS_DAYS = "3";
    public static final String PARTNER_COUNTRY_CODE = "IN";
    public static final String PARTNER_CURRENCY_CODE = "INR";
    public static int FEDEX_ADDRESS_LINE_CONSTANT = 35;
    private static String REGEX_FOR_ADDRESS_FORMATING = "[ ,-;]";
    public static final String ONEASSIST = "ONEASSIST";
    public static final String QUANTITY_UNITS = "EA";
    private static final String CARRIER_CODE_TYPE = "FDXE";
    private static final String DROP_OFF_TYPE = "REGULAR_PICKUP";
    private static final String DOCUMENT_CONTENT_TYPE = "DOCUMENTS_ONLY";
    private static final String LABEL_FORMAT_TYPE = "COMMON2D";
    private static final String LABEL_ORIENTATION_TYPE = "TOP_EDGE_OF_TEXT_FIRST";
    private static final String LABEL_STOCK_TYPE = "PAPER_8.5X11_TOP_HALF_LABEL";
    private static final String PACKAGING_TYPE = "YOUR_PACKAGING";
    private static final String PAYMENT_TYPE = "SENDER";
    private static final String SERVICE_TYPE_STANDARD_OVERNIGHT = "STANDARD_OVERNIGHT";
    private static final String PICKUP_REQUEST_TYPE = "SAME_DAY";
    private static final String SERVICE_TYPE_PRIORITY_OVERNIGHT = "PRIORITY_OVERNIGHT";

    private static final String PURPOSE_OF_SHIPMENT = "SOLD";
    private static final String IMAGE_TYPE = "PDF";

    private static final String TIN_TYPE = "BUSINESS_NATIONAL";

    private static final String TRACK_TYPE = "TRACKING_NUMBER_OR_DOORTAG";

    private static final String WEIGHT_UNITS = "KG";

    private static final int PROCESS_SHIPMENT_SCHEMA_VERSION = 19;

    private static final String PROCESS_SHIPMENT_SERVICE_NAME = "ship";

    @Override
    public Object createShipment(ShipmentRequestDto shipmentRequest) throws Exception {
        boolean status = false;
        logger.info("Inside fedex handle request start");
        boolean shipmentUpdated = false, labelCreated = false;
        try {
            PickupAvailabilityRequest pickupAvailabilityRequest = preaprePickupAvailabilityPayload(shipmentRequest);
            PickupAvailabilityReply pickupAvailabilityReply = pickupAvailabilityProcess(pickupAvailabilityRequest, shipmentRequest);
            if (pickupAvailabilityReply != null) {
                CreatePickupRequest pickupRequest = preaprePickupPayload(shipmentRequest, pickupAvailabilityReply);
                ProcessShipmentRequest shipProcessrequest = preapreShipProcessPayload(shipmentRequest, pickupRequest);
                ProcessShipmentReply shipProcessreply = processShipment(shipProcessrequest, shipmentRequest);
                String awbNum = getAWBNumber(shipProcessreply.getCompletedShipmentDetail().getCompletedPackageDetails());
                if (!StringUtils.isEmpty(awbNum)) {
                    commonService.updateShipmentAWB(shipmentRequest.getShipmentId(), awbNum, shipmentRequest.getLogisticPartnerCode(), shipmentRequest.getCreatedBy());
                    ShippingDocument shippingDocument = shipProcessreply.getCompletedShipmentDetail().getCompletedPackageDetails().get(0).getLabel();
                    labelCreated = getAssociatedShipmentLabelToFile(shippingDocument, awbNum, OUTBOUND_LABEL, shipmentRequest);
                    logger.info("Inside fedex handle request storeMongoRefInDoc");
                    status = true;
                } else {
                    throw new Exception("Null Response from Fedex");
                }
            } else {
                throw new Exception("Null Pickup availability response from Fedex");
            }

        } catch (Exception ex) {
            logger.error("Fedex handler exception", ex);
            throw ex;
        } finally {
            if (shipmentUpdated && labelCreated) {
                commonService.updateShipmentFailReason(shipmentRequest.getShipmentId(), null, null, shipmentRequest.getModifiedBy());
            }
        }
        logger.info("Inside fedex handle request End");
        return status;
    }

    private String getAWBNumber(List<CompletedPackageDetail> completedPackageDetails) {
        String awbNumber = null;
        if (null != completedPackageDetails) {
            for (CompletedPackageDetail trackid : completedPackageDetails) {
                List<TrackingId> track = trackid.getTrackingIds();
                for (TrackingId s : track) {
                    awbNumber = s.getTrackingNumber();
                }
            }
        }
        return awbNumber;
    }

    public ProcessShipmentRequest preapreShipProcessPayload(ShipmentRequestDto shipmentRequestDto, CreatePickupRequest createPickup) throws Exception {
        logger.info("Inside preapreShipProcessPayload- start");
        ProcessShipmentRequest request = new ProcessShipmentRequest();

        PartnerEventDetailDto eventMst = commonService.getPartnerEventMst(ClaimLifecycleEvent.FEDEX_PROCESS_SHIPMENT.toString());
        try {
            RequestedShipment requestShipment = new RequestedShipment();
            Weight weight = new Weight();
            request.setWebAuthenticationDetail(getWebAuthDetail(eventMst));
            request.setClientDetail(getClientDetail(eventMst));
            request.setTransactionDetail(getTransactionDetail(eventMst.getEventName()));
            if (null != createPickup.getOriginDetail())
                requestShipment.setShipTimestamp(createPickup.getOriginDetail().getReadyTimestamp());
            VersionId version = new VersionId();
            version.setMajor(PROCESS_SHIPMENT_SCHEMA_VERSION);
            version.setServiceId(PROCESS_SHIPMENT_SERVICE_NAME);
            request.setVersion(version);
            requestShipment.setDropoffType(DROP_OFF_TYPE);
            requestShipment.setServiceType(SERVICE_TYPE_STANDARD_OVERNIGHT);
            requestShipment.setPackagingType(PACKAGING_TYPE);

            requestShipment.setShipper(getPartyDetails(shipmentRequestDto.getOriginAddressDetails(), eventMst));
            requestShipment.setRecipient(getPartyDetails(shipmentRequestDto.getDestinationAddressDetails(), eventMst));

            weight.setUnits(WEIGHT_UNITS);
            weight.setValue(new BigDecimal(shipmentRequestDto.getBoxActualWeight()));
            Money money = getMoneyDetail(commonService.getInvoiceValue(shipmentRequestDto.getShipmentDeclareValue()));

            requestShipment.setShippingChargesPayment(getPaymentDetails(eventMst));
            requestShipment.setCustomsClearanceDetail(getCustomerClearanceDetail(shipmentRequestDto, money, weight));

            requestShipment.setPackageCount(new BigInteger("1"));
            requestShipment.getRequestedPackageLineItems().add(getPackageLineItem(shipmentRequestDto, money, weight));
            requestShipment.setLabelSpecification(getLabelSpecification());
            request.setRequestedShipment(requestShipment);

        } catch (Exception ex) {
            logger.info(" Exception inside preapreShipProcessPayload exception", ex);
        }

        return request;
    }

    private Payment getPaymentDetails(PartnerEventDetailDto eventMst) {
        Payment payment = new Payment();
        Payor payor = new Payor();
        Party partyPayor = new Party();
        partyPayor.setAccountNumber(eventMst.getParnerAttributes().getAccountNum());
        payment.setPaymentType(PAYMENT_TYPE);
        payor.setResponsibleParty(partyPayor);
        payment.setPayor(payor);
        return payment;
    }

    private Party getPartyDetails(ServiceAddressDetailDto originAddress, PartnerEventDetailDto eventMst) throws Exception {
        Party partyShipper = new Party();
        partyShipper.getTins().add(getTaxPayerIdentification(eventMst));
        partyShipper.setAccountNumber(eventMst.getParnerAttributes().getAccountNum());
        partyShipper.setContact(getContact(originAddress));
        partyShipper.setAddress(getAddress(originAddress));
        return partyShipper;
    }

    private LabelSpecification getLabelSpecification() {
        LabelSpecification labelSpecification = new LabelSpecification();
        labelSpecification.setImageType(IMAGE_TYPE);
        labelSpecification.setLabelFormatType(LABEL_FORMAT_TYPE);
        labelSpecification.setLabelStockType(LABEL_STOCK_TYPE);
        labelSpecification.setLabelPrintingOrientation(LABEL_ORIENTATION_TYPE);
        return labelSpecification;
    }

    private RequestedPackageLineItem getPackageLineItem(ShipmentRequestDto shipmentRequestDto, Money money, Weight weight) {
        RequestedPackageLineItem requestedPackageLineItem = new RequestedPackageLineItem();
        CustomerReference cust = new CustomerReference();
        cust.setCustomerReferenceType(CustomerReferenceType.CUSTOMER_REFERENCE);
        cust.setValue(String.valueOf(shipmentRequestDto.getShipmentId()));
        CustomerReference cust2 = new CustomerReference();
        cust2.setCustomerReferenceType(CustomerReferenceType.P_O_NUMBER);
        cust2.setValue("B2C");
        CustomerReference cust3 = new CustomerReference();
        cust3.setCustomerReferenceType(CustomerReferenceType.DEPARTMENT_NUMBER);
        cust3.setValue("BILL D/T: SENDER");

        requestedPackageLineItem.setSequenceNumber(BigInteger.ONE);
        requestedPackageLineItem.setInsuredValue(money);
        requestedPackageLineItem.setWeight(weight);
        requestedPackageLineItem.getCustomerReferences().add(cust);
        requestedPackageLineItem.getCustomerReferences().add(cust2);
        requestedPackageLineItem.getCustomerReferences().add(cust3);
        return requestedPackageLineItem;
    }

    private CustomsClearanceDetail getCustomerClearanceDetail(ShipmentRequestDto shipmentRequestDto, Money money, Weight weight) {
        CustomsClearanceDetail customsClearanceDetail = new CustomsClearanceDetail();
        CommercialInvoice commercialinvoice = new CommercialInvoice();
        customsClearanceDetail.setCustomsValue(money);
        customsClearanceDetail.getCommodities().add(getCommodityDetail(shipmentRequestDto, money, weight));

        customsClearanceDetail.setDocumentContent(DOCUMENT_CONTENT_TYPE);
        customsClearanceDetail.setCustomsValue(money);
        commercialinvoice.setPurpose(PURPOSE_OF_SHIPMENT);
        customsClearanceDetail.setCommercialInvoice(commercialinvoice);
        return customsClearanceDetail;
    }

    private Commodity getCommodityDetail(ShipmentRequestDto shipmentRequestDto, Money money, Weight weight) {
        Commodity commodity = new Commodity();
        commodity.setNumberOfPieces(new BigInteger(String.valueOf(shipmentRequestDto.getShipmentAssetDetails().size())));
        commodity.setDescription(shipmentRequestDto.getShipmentAssetDetails().get(0).getAssetCategoryName());

        commodity.setCountryOfManufacture(PARTNER_COUNTRY_CODE);
        commodity.setQuantity(new BigDecimal(String.valueOf(shipmentRequestDto.getShipmentAssetDetails().size())));
        commodity.setQuantityUnits(QUANTITY_UNITS);
        commodity.setUnitPrice(money);
        commodity.setWeight(weight);
        return commodity;
    }

    private Money getMoneyDetail(String shipmentValue) {
        Money money = new Money();
        money.setCurrency(PARTNER_CURRENCY_CODE);
        money.setAmount(new BigDecimal(shipmentValue));
        return money;
    }

    private TaxpayerIdentification getTaxPayerIdentification(PartnerEventDetailDto eventMst) {
        TaxpayerIdentification tax = new TaxpayerIdentification();
        tax.setTinType(TIN_TYPE);
        tax.setNumber(eventMst.getParnerAttributes().getOneassitsVatNum());
        return tax;
    }

    private boolean getAssociatedShipmentLabelToFile(ShippingDocument shippingDocument, String trackingNumber, String labelName, ShipmentRequestDto shipmentRequestDto) throws Exception {
        logger.info("Inside getAssociatedShipmentLabelToFile - start ");
        boolean labelCreated = false;
        File associatedShipmentLabelFile = null;
        try {
            List<ShippingDocumentPart> sdparts = shippingDocument.getParts();
            ShippingDocumentPart sdpart = null;
            if (!sdparts.isEmpty()) {
                for (ShippingDocumentPart docLabel : sdparts) {
                    sdpart = docLabel;
                }

                String associatedShipmentLabelFileName = new String(shipmentDocFolderPath + labelName + "_" + trackingNumber + ".pdf");
                associatedShipmentLabelFile = new File(associatedShipmentLabelFileName);
                FileOutputStream fos = new FileOutputStream(associatedShipmentLabelFile);
                fos.write(sdpart.getImage());
                fos.close();
                commonService.storeMongoRefInDoc(shipmentRequestDto.getServiceRequestDetails().getServiceRequestId(), trackingNumber, associatedShipmentLabelFile);
                labelCreated = true;
                logger.info("Associated shipment label file name " + associatedShipmentLabelFile.getAbsolutePath());
            }
        } catch (Exception e) {
            commonService.updateShipmentFailReason(shipmentRequestDto.getShipmentId(), "Failed to read Fedex shipment label.", e, shipmentRequestDto.getModifiedBy());
            logger.error("Exception while getting label.", e);
            throw e;
        }
        logger.info("Inside getAssociatedShipmentLabelToFile - end ");
        return labelCreated;
    }

    public PickupAvailabilityRequest preaprePickupAvailabilityPayload(ShipmentRequestDto shipmentRequest) throws Exception {
        PickupAvailabilityRequest pickupAvailabilityRequest = new PickupAvailabilityRequest();
        GregorianCalendar c = new GregorianCalendar();
        logger.info("Inside preaprePickupAvailabilityPayload start");
        try {
            PartnerEventDetailDto eventMst = commonService.getPartnerEventMst(ClaimLifecycleEvent.FEDEX_PICKUP.toString());
            pickupAvailabilityRequest.setClientDetail(getClientDetail(eventMst));
            pickupAvailabilityRequest.setVersion(new VersionId());
            pickupAvailabilityRequest.setWebAuthenticationDetail(getWebAuthDetail(eventMst));
            TransactionDetail transactionDetail = new TransactionDetail();
            transactionDetail.setCustomerTransactionId(String.valueOf(shipmentRequest.getServiceRequestDetails().getRefPrimaryTrackingNo()));
            pickupAvailabilityRequest.setTransactionDetail(transactionDetail);

            pickupAvailabilityRequest.setPickupAddress(getAddress(shipmentRequest.getOriginAddressDetails()));
            pickupAvailabilityRequest.setNumberOfBusinessDays(new BigInteger(NUBER_OF_BUSINESS_DAYS));
            pickupAvailabilityRequest.getPickupRequestType().add(PICKUP_REQUEST_TYPE);
            pickupAvailabilityRequest.getCarriers().add(CARRIER_CODE_TYPE);
            pickupAvailabilityRequest.setShipmentAttributes(getPickupShipmentAttributes());

            pickupAvailabilityRequest.setDispatchDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
        } catch (Exception ex) {
            logger.info("Inside Pickup availability method" + ex);
        }
        return pickupAvailabilityRequest;

    }

    private Address getAddress(ServiceAddressDetailDto addressDetails) throws Exception {
        Address address = new Address();
        address.getStreetLines().addAll(formatAddress(addressDetails.getAddressLine1(), addressDetails.getAddressLine2()));
        address.setPostalCode(addressDetails.getPincode());
        address.setCountryCode(PARTNER_COUNTRY_CODE);

        PincodeMasterDto stateCityResponse = commonService.getStateAndCity(addressDetails.getPincode());
        if (stateCityResponse != null) {
            address.setCity(stateCityResponse.getCityName());
            address.setStateOrProvinceCode(stateCityResponse.getStateName());
        }
        return address;
    }

    private PickupShipmentAttributes getPickupShipmentAttributes() {

        PickupShipmentAttributes pickupShipmentAttributes = new PickupShipmentAttributes();
        pickupShipmentAttributes.setServiceType(SERVICE_TYPE_PRIORITY_OVERNIGHT);
        pickupShipmentAttributes.setPackagingType(PACKAGING_TYPE);
        return pickupShipmentAttributes;
    }

    private WebAuthenticationDetail getWebAuthDetail(PartnerEventDetailDto eventMst) {
        WebAuthenticationDetail authenticationDetails = new WebAuthenticationDetail();
        WebAuthenticationCredential credentials = new WebAuthenticationCredential();
        credentials.setKey(eventMst.getUserName());
        credentials.setPassword(eventMst.getPassword());
        authenticationDetails.setUserCredential(credentials);
        return authenticationDetails;
    }

    private ClientDetail getClientDetail(PartnerEventDetailDto eventMst) {

        ClientDetail clientDetail = new ClientDetail();
        clientDetail.setAccountNumber(eventMst.getParnerAttributes().getAccountNum());
        clientDetail.setMeterNumber(eventMst.getParnerAttributes().getMeterNum());
        return clientDetail;
    }

    public PickupAvailabilityReply pickupAvailabilityProcess(PickupAvailabilityRequest createPickupAvailabilityRequest, ShipmentRequestDto shipmentRequestDto) throws Exception {
        logger.info("Inside the pickupAvailabilityProcess -Start" + createPickupAvailabilityRequest);
        PickupAvailabilityReply reply = null;
        try {
            PickupAvailabilityRequestWrapper payload = constructPickupAvailabilityPayload(createPickupAvailabilityRequest);
            String response = fedexProxy.checkPickupAvailability(payload);
            System.out.println(response);
            PickupAvailabilityReplyWrapper responseOject = unmarshalResponse(response, PickupAvailabilityReplyWrapper.class);
            storeRequestInMongo(shipmentRequestDto.getServiceRequestDetails().getRefPrimaryTrackingNo(), ClaimLifecycleEvent.FEDEX_PICKUP, payload, responseOject);
            if (responseOject != null) {
                reply = responseOject.getPickupAvailabilityReplyBodyWrapper().getPickupAvailabilityReply();
                if (String.valueOf(reply.getHighestSeverity()).equals(PARTNER_ERROR)) {
                    for (Notification notification : reply.getNotifications()) {
                        logger.info("pickupProcess" + notification.getMessage());
                        throw new Exception("Pickup Shipment exception" + notification.getMessage());
                    }
                }
            } else {
                throw new Exception("Null Response from fedex process shipment request");
            }
        } catch (Exception ex) {
            logger.info("Exception in pickupAvailabilityProcess :", ex);
            throw ex;

        }
        logger.info("Inside the pickupAvailabilityProcess -End");
        return reply;
    }

    private PickupAvailabilityRequestWrapper constructPickupAvailabilityPayload(PickupAvailabilityRequest createPickupAvailabilityRequest) throws JAXBException, JsonProcessingException {
        JAXBContext context = JAXBContext.newInstance(PickupAvailabilityRequestWrapper.class);
        PickupAvailabilityRequestWrapper payload = new PickupAvailabilityRequestWrapper();
        PickupAvailabilityRequestBodyWrapper pickupBodyWrapper = new PickupAvailabilityRequestBodyWrapper();
        pickupBodyWrapper.setPickupAvailabilityRequest(createPickupAvailabilityRequest);
        payload.setPickupAvailabilityRequestBodyWrapper(pickupBodyWrapper);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(payload, System.out);
        return payload;
    }

    public CreatePickupRequest preaprePickupPayload(ShipmentRequestDto shipmentRequestDto, PickupAvailabilityReply pickupAvailabilityReply) throws Exception {

        XMLGregorianCalendar cutOffTime = null;
        CreatePickupRequest pickupRequest = new CreatePickupRequest();
        PickupOriginDetail pickupOriginDetail = new PickupOriginDetail();
        try {
            PartnerEventDetailDto eventMst = commonService.getPartnerEventMst(ClaimLifecycleEvent.FEDEX_PICKUP.toString());
            pickupRequest.setVersion(new VersionId());
            pickupRequest.setClientDetail(getClientDetail(eventMst));
            pickupRequest.setWebAuthenticationDetail(getWebAuthDetail(eventMst));
            pickupRequest.setTransactionDetail(getTransactionDetail(shipmentRequestDto.getServiceRequestDetails().getRefPrimaryTrackingNo()));

            ServiceAddressDetailDto originAddress = shipmentRequestDto.getOriginAddressDetails();
            ContactAndAddress party = new ContactAndAddress();
            party.setContact(getContact(originAddress));
            party.setAddress(getAddress(originAddress));
            pickupOriginDetail.setPickupLocation(party);

            pickupOriginDetail.setReadyTimestamp(getPickupReadyTimestamp(pickupAvailabilityReply.getOptions(), cutOffTime));

            pickupOriginDetail.setCompanyCloseTime(cutOffTime);
            pickupRequest.setOriginDetail(pickupOriginDetail);
            pickupRequest.setPackageCount(new BigInteger(String.valueOf(shipmentRequestDto.getBoxCount())));
            pickupRequest.setCarrierCode(CARRIER_CODE_TYPE);
        } catch (Exception ex) {
            logger.error("Exception while constructing FEDEX pickup payload", ex);
        }

        return pickupRequest;

    }

    @SuppressWarnings("deprecation")
    private XMLGregorianCalendar getPickupReadyTimestamp(List<PickupScheduleOption> listOfPickupScheduleOptions, XMLGregorianCalendar cutOffTime) throws DatatypeConfigurationException, ParseException {

        XMLGregorianCalendar pickUpDateVal = null;
        Date dateTimeVal = null;
        long time = 0;
        XMLGregorianCalendar xmlDate = null;

        XMLGregorianCalendar pickUpTimeVal = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (PickupScheduleOption option : listOfPickupScheduleOptions) {
            CountryRelationshipType countryName = option.getCountryRelationship();
            if (countryName.toString().equals("DOMESTIC")) {
                if (option.isAvailable()) {
                    pickUpDateVal = option.getPickupDate();
                    cutOffTime = option.getCutOffTime();
                    long cutOffTime1 = cutOffTime.toGregorianCalendar().getTimeInMillis();
                    Duration duration = option.getAccessTime();
                    long accessTime = duration.getTimeInMillis(Calendar.getInstance());
                    time = cutOffTime1 - accessTime;
                    break;
                }
            }
        }
        String timeFormat = new SimpleDateFormat("HH:mm:ss").format(new Date(time));
        pickUpTimeVal = DatatypeFactory.newInstance().newXMLGregorianCalendar(timeFormat);
        dateTimeVal = df.parse(pickUpDateVal.toString() + " " + pickUpTimeVal.toString());
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dateTimeVal);
        xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), dateTimeVal.getHours(),
                dateTimeVal.getMinutes(), dateTimeVal.getSeconds(), DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED);
        return xmlDate;
    }

    private Contact getContact(ServiceAddressDetailDto address) {
        Contact contact = new Contact();
        contact.setPersonName(address.getAddresseeFullName());
        contact.setEMailAddress(address.getEmail());
        contact.setCompanyName("Oneassist");
        contact.setPhoneNumber(String.valueOf(address.getMobileNo()));
        return contact;
    }

    public ProcessShipmentReply processShipment(ProcessShipmentRequest processShipmentRequest, ShipmentRequestDto shipmentRequest) throws Exception {
        logger.info("Inside the processShipment  -Start" + processShipmentRequest);
        ProcessShipmentReply reply = null;
        try {
            String requestWrapper = getProcessShipmentPayload(processShipmentRequest, shipmentRequest);
            String responseOject = fedexProxy.processShipment(requestWrapper);
            logger.error("Fedex response :" + responseOject);
            storeRequestInMongo(shipmentRequest.getServiceRequestDetails().getRefPrimaryTrackingNo(), ClaimLifecycleEvent.FEDEX_PROCESS_SHIPMENT, requestWrapper, responseOject);
            ProcessShipmentReplyWrapper replyWrapper = unmarshalResponse(responseOject, ProcessShipmentReplyWrapper.class);
            reply = replyWrapper.getProcessShipmentReplyBodyWrapper().getProcessShipmentReply();
            if (String.valueOf(reply.getHighestSeverity()).equals(PARTNER_ERROR)) {
                for (Notification notification : reply.getNotifications()) {
                    logger.info("processShipment" + notification.getMessage());
                    throw new Exception("Process Shipment exception" + notification.getMessage());
                }
            }
        } catch (Exception ex) {
            commonService.updateShipmentFailReason(shipmentRequest.getShipmentId(), "Fedex shipment process failed.", ex, shipmentRequest.getModifiedBy());
            logger.error("Detail error", ex);
            commonService.storeThirdPartyInteractionDetailsInMongo(Long.valueOf(shipmentRequest.getServiceRequestDetails().getRefPrimaryTrackingNo()), ClaimLifecycleEvent.FEDEX_PROCESS_SHIPMENT,
                    processShipmentRequest, reply, ex.getMessage());
            throw ex;
        }
        logger.info("Inside the processShipment  -End");
        return reply;

    }

    private String getProcessShipmentPayload(ProcessShipmentRequest processShipmentRequest, ShipmentRequestDto shipmentRequest) throws JAXBException, JsonProcessingException {
        ProcessShipmentRequestWrapper requestWrapper = new ProcessShipmentRequestWrapper();
        ProcessShipmentRequestBodyWrapper bodyWrapper = new ProcessShipmentRequestBodyWrapper();
        bodyWrapper.setProcessShipmentRequest(processShipmentRequest);
        requestWrapper.setProcessShipmentRequestBodyWrapper(bodyWrapper);

        JAXBContext context = JAXBContext.newInstance(ProcessShipmentRequestWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        final StringWriter stringWriter = new StringWriter();
        marshaller.marshal(requestWrapper, stringWriter);
        String paylaod = stringWriter.toString();
        paylaod = paylaod.replaceAll("ProcessShipmentRequest>", "ns3:ProcessShipmentRequest>").replaceAll("xmlns:ns3=\"http://fedex.com/ws/pickup/v13\"", "")
                .replaceAll("xmlns=\"http://fedex.com/ws/ship/v19\"", "xmlns:ns3=\"http://fedex.com/ws/ship/v19\"");
        logger.error("Process shipment payload is :" + paylaod);
        return paylaod;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object trackShipment(HashMap trackingPayload) throws Exception {
        PartnerEventDetailDto eventMst = commonService.getPartnerEventMst(ClaimLifecycleEvent.FEDEX_TRACK.toString());
        if (eventMst != null) {
            Long parterCode = commonService.getPartnerCodeByName(ServicePartner.FEDEX);
            List<ShipmentDetailsDto> shipmenTrackings = commonService.getShipmentsForTracking(parterCode);
            if (!CollectionUtils.isEmpty(shipmenTrackings)) {
                for (ShipmentDetailsDto shipment : shipmenTrackings) {
                    TrackReply reply = null;
                    try {
                        TrackRequest trackRequest = preapreTrackPayload(shipment.getLogisticPartnerRefTrackingNumber(), eventMst);
                        String response = fedexProxy.trackShipment(trackRequest);
                        TrackShipmentResponseWrapper replyWrapper = unmarshalResponse(response, TrackShipmentResponseWrapper.class);
                        reply = replyWrapper.getTrackShipmentResponseBodyWrapper().getTrackReply();
                        if (reply != null) {
                            XMLGregorianCalendar datetime = reply.getCompletedTrackDetails().get(0).getTrackDetails().get(0).getStatusDetail().getCreationTime();
                            Date updatedOn = new Date();
                            if (datetime != null) {
                                updatedOn = datetime.toGregorianCalendar().getTime();
                            }
                            String statuCode = reply.getCompletedTrackDetails().get(0).getTrackDetails().get(0).getStatusDetail().getCode();
                            String oneAssistStage = commonService.getOneassistShipmentStage(statuCode, shipment.getLogisticPartnerCode(), shipment.getShipmentType());
                            if (oneAssistStage != null) {
                                commonService.updateShipmentStage(shipment.getShipmentId(), oneAssistStage, updatedOn, Constants.MODIFIED_BY_BATCH);
                            } else {
                                logger.error("Not updating the stage of shipment::" + shipment.getShipmentId() + " because couldn't find oneassist status for code ::" + statuCode);
                            }
                        }
                    } catch (Exception ex) {
                        logger.error("Exception in trackProcess(): ", ex);
                    }
                }
            } else {
                logger.info("No FEDEXshipments are present for tracking ");
            }
        } else {
            logger.error("No events has been configured for fedex tracking");
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T unmarshalResponse(String response, Class<T> classType) throws JAXBException {

        StringReader sr = new StringReader(response);
        JAXBContext jaxbContext = JAXBContext.newInstance(classType);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (T) unmarshaller.unmarshal(sr);
    }

    @Override
    public Object cancelShipment(String logistincPartnerTrackingNumber) throws Exception {
        return null;
    }

    public TrackRequest preapreTrackPayload(String trakingNum, PartnerEventDetailDto eventMst) {

        TrackRequest trackRequest = new TrackRequest();
        trackRequest.setClientDetail(getClientDetail(eventMst));
        trackRequest.setWebAuthenticationDetail(getWebAuthDetail(eventMst));
        trackRequest.setTransactionDetail(getTransactionDetail(trakingNum));
        TrackSelectionDetail trackSelectionDetail = new TrackSelectionDetail();
        trackSelectionDetail.setCarrierCode(CARRIER_CODE_TYPE);
        TrackPackageIdentifier trackPackageIdentifier = new TrackPackageIdentifier();
        trackPackageIdentifier.setType(TRACK_TYPE);
        trackPackageIdentifier.setValue(trakingNum);
        trackSelectionDetail.setPackageIdentifier(trackPackageIdentifier);

        trackRequest.getSelectionDetails().add(trackSelectionDetail);
        return trackRequest;

    }

    private TransactionDetail getTransactionDetail(String trakingNum) {

        TransactionDetail transactionDetail = new TransactionDetail();
        transactionDetail.setCustomerTransactionId(trakingNum);
        return transactionDetail;
    }

    private void storeRequestInMongo(String refPrimaryTrackingNo, ClaimLifecycleEvent event, Object payload, Object responseOject) {
        try {
            commonService.storeThirdPartyInteractionDetailsInMongo(Long.parseLong(refPrimaryTrackingNo), event, payload != null ? objectMapper.writeValueAsString(payload) : null,
                    responseOject != null ? objectMapper.writeValueAsString(responseOject) : null, null);
        } catch (Exception e) {
            logger.error("Exception while storing interaction in mongo", e);
        }

    }

    private List<String> formatAddress(String addressLine1, String addressLine2) {
        List<String> addressList = new ArrayList<String>();
        try {
            if (!StringUtils.isEmpty(addressLine1) && addressLine1.length() > FEDEX_ADDRESS_LINE_CONSTANT) {
                String tempAddress1 = addressLine1.substring(0, FEDEX_ADDRESS_LINE_CONSTANT);
                Matcher matcher = Pattern.compile(REGEX_FOR_ADDRESS_FORMATING).matcher(tempAddress1);
                int splitIndex = FEDEX_ADDRESS_LINE_CONSTANT;
                while (matcher.find()) {
                    splitIndex = matcher.start();
                }

                String preparedAddressLine1 = addressLine1.substring(0, splitIndex);
                String preparedAddressLine2 = addressLine1.substring(splitIndex) + (StringUtils.isEmpty(addressLine2) ? "" : (" " + addressLine2));
                logger.info("Formatted address is :: address line1 " + preparedAddressLine1 + " address line2 " + preparedAddressLine2);
                addressList.add(preparedAddressLine1);
                addressList.add(preparedAddressLine2);
            } else {
                addressList.add(addressLine1);
                addressList.add(addressLine2);
            }
        } catch (Exception e) {
            logger.error("Exception while formatting address for fedex request", e);
        }
        return addressList;
    }
}
