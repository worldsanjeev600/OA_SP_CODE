package com.oneassist.serviceplatform.services.logisticpartner.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import com.amazonaws.util.StringInputStream;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.enums.EcomShipmentType;
import com.oneassist.serviceplatform.commons.enums.LogisticResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ShipmentType;
import com.oneassist.serviceplatform.commons.proxies.EcomExpressProxy;
import com.oneassist.serviceplatform.commons.utils.BarcodeGenerator;
import com.oneassist.serviceplatform.commons.utils.TemplateParser;
import com.oneassist.serviceplatform.commons.validators.InputValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.PartnerEventDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceAddressDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentRequestDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.externalcontracts.ClaimLifecycleEvent;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;
import com.oneassist.serviceplatform.externalcontracts.ecom.EcomExpressObjects;
import com.oneassist.serviceplatform.externalcontracts.ecom.ForwardShipmentRequest;
import com.oneassist.serviceplatform.externalcontracts.ecom.ForwardShipmentResponse;
import com.oneassist.serviceplatform.externalcontracts.ecom.ResponseObjects;
import com.oneassist.serviceplatform.externalcontracts.ecom.Shipment;
import com.oneassist.serviceplatform.externalcontracts.ecom.Shipments;
import com.oneassist.serviceplatform.externalcontracts.ecom.TrackShipmentResponse;
import com.oneassist.serviceplatform.services.commons.ICommonService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service("ecomService")
public class ECOMService extends BaseLogisticPartnerService implements ILogisticPartnerService {

    private final Logger log = Logger.getLogger(ECOMService.class);

    @Autowired
    @Qualifier("commonService")
    private ICommonService commonService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${SHIPMENT_DOCUMENT_FOLDER_PATH}")
    private String shipmentDocFolderPath;

    private String OUTBOUND_LABEL = "OUTBOUND_LABEL";

    @Autowired
    private InputValidator inputValidator;

    @Autowired
    private EcomExpressProxy ecomExpressProxy;

    @Autowired
    private BarcodeGenerator barcodeGenerator;

    @Autowired
    private TemplateParser templateParser;

    @Value("${VM_TEMPLATE_PATH_NAME}")
    private String TEMPLATE_PATH_NAME;

    @Value("${ECOM_REVERSE_PICKUP_ITEM_DESCRIPTION}")
    private String ECOM_REVERSE_PICKUP_ITEM_DESCRIPTION;

    @Value("${ECOM_LABEL_VM_FILE_NAME}")
    private String ECOM_LABEL_VM_FILE_NAME;

    @Value("${ECOM_REVERSE_PICKUP_ADDON_SERVICES}")
    private String ECOM_REVERSE_PICKUP_ADDON_SERVICES;

    @Autowired
    private MessageSource messageSource;

    private static final String TRUE = "true";

    private static final String ECOM_BARCODE_IMAGE_ID = "barcode";

    private static final String AWB_PARAM_NAME = "awb";

    private static final String REASON_CODE_NUMBER = "reason_code_number";

    private static final String STATUS_CODE_NUMBER = "status_update_number";

    @Override
    public Object createShipment(ShipmentRequestDto shipmentRequest) throws Exception {
        boolean status = false;

        if (shipmentRequest != null) {
            if (shipmentRequest.getShipmentType().intValue() == ShipmentType.REVERSE_PICKUP.getShipmentType()) {
                String awbNumber = ecomExpressProxy.generateAWB(EcomShipmentType.FORWARD_PICKUP);
                this.placeForwardPickupRequest(shipmentRequest, awbNumber);
                log.info("EcomEventHandler Awb number" + awbNumber);
                status = true;
            } else if (shipmentRequest.getShipmentType().intValue() == ShipmentType.PICKUP.getShipmentType()) {
                String awbNumber = ecomExpressProxy.generateAWB(EcomShipmentType.REVERSE_PICKUP);
                this.placeReversePickupRequest(shipmentRequest, awbNumber);
                status = true;
            } else {
                log.error("Invalid shipment type ::" + shipmentRequest.getShipmentType());
            }
        } else {
            log.error("Shipment Request details object is empty");
        }

        return status;
    }

    private void placeForwardPickupRequest(ShipmentRequestDto shipmentRequest, String awbNumber) throws Exception {
        if (null != awbNumber) {
            PartnerEventDetailDto eventMst = commonService.getPartnerEventMst(ClaimLifecycleEvent.ECOM_FORWARD_SHIPMENT.toString());
            MultiValueMap<String, Object> ecomForwardPayload = prepareForwadShipment(shipmentRequest, awbNumber, eventMst);
            log.info("Fwd shipment request ::" + ecomForwardPayload);
            ResponseEntity<ForwardShipmentResponse> responseOject = ecomExpressProxy.placeForwardPickupRequest(ecomForwardPayload);
            log.info("Fwd shipment response ::" + responseOject);
            commonService.storeThirdPartyInteractionDetailsInMongo(Long.parseLong(shipmentRequest.getServiceRequestDetails().getRefPrimaryTrackingNo()), ClaimLifecycleEvent.ECOM_FORWARD_SHIPMENT,
                    objectMapper.writeValueAsString(ecomForwardPayload), responseOject != null ? objectMapper.writeValueAsString(responseOject) : "", null);
            if (responseOject != null && responseOject.getBody() != null) {
                Exception e = null;
                String failReason = null;
                ForwardShipmentResponse shipments = responseOject.getBody();
                if (shipments != null && shipments.getShipments() != null && shipments.getShipments().length > 0) {
                    for (Shipments ship : shipments.getShipments()) {
                        if (ship.getSuccess().equalsIgnoreCase(TRUE)) {
                            commonService.updateShipmentAWB(shipmentRequest.getShipmentId(), awbNumber, shipmentRequest.getLogisticPartnerCode(), shipmentRequest.getCreatedBy());
                            failReason = storeEcomLabel(shipmentRequest, awbNumber);
                        } else {
                            e = ship.getReason() != null ? new Exception(ship.getReason()) : null;
                            failReason = messageSource.getMessage(String.valueOf(LogisticResponseCodes.FAILED_TO_PLACE_ECOM_REQUEST.getErrorCode()), new Object[] { "" }, null);
                        }
                    }
                } else {
                    failReason = messageSource.getMessage(String.valueOf(LogisticResponseCodes.NULL_RESPONSE_FROM_ECOM.getErrorCode()), new Object[] { "" }, null);
                }
                commonService.updateShipmentFailReason(shipmentRequest.getShipmentId(), failReason, e, shipmentRequest.getCreatedBy());
            } else {
                throw new Exception(messageSource.getMessage(String.valueOf(LogisticResponseCodes.NULL_AWB_GENERATION_RESPONSE_FROM_ECOM.getErrorCode()), new Object[] { "" }, null));
            }
        }
    }

    private void placeReversePickupRequest(ShipmentRequestDto shipmentRequest, String awbNumber) throws Exception {
        PartnerEventDetailDto eventMst = commonService.getPartnerEventMst(ClaimLifecycleEvent.ECOM_REVERSE_PICKUP.toString());
        MultiValueMap<String, Object> ecomReversePickupRequest = ecomReversePickupPayload(shipmentRequest, awbNumber, eventMst);
        log.info("Ecom Reverse shipment request::" + ecomReversePickupRequest);
        ResponseEntity<String> responseOject = ecomExpressProxy.placeReversePickupRequest(ecomReversePickupRequest);
        log.info("Ecom Reverse shipment response::" + responseOject);
        commonService.storeThirdPartyInteractionDetailsInMongo(Long.parseLong(shipmentRequest.getServiceRequestDetails().getRefPrimaryTrackingNo()), ClaimLifecycleEvent.ECOM_FORWARD_SHIPMENT,
                objectMapper.writeValueAsString(ecomReversePickupRequest), responseOject.getBody(), null);
        if (responseOject != null && responseOject.getBody() != null) {
            Exception e = null;
            String failReason = null;
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            log.info("Ecom Reverse shipment request::" + responseOject.getBody());
            ResponseObjects reverePickupResponse = mapper.readValue(responseOject.getBody(), ResponseObjects.class);
            if (reverePickupResponse != null && reverePickupResponse.getAirwaybillObjects().getAirwaybill().getSuccess().equalsIgnoreCase(TRUE)) {
                commonService.updateShipmentAWB(shipmentRequest.getShipmentId(), reverePickupResponse.getAirwaybillObjects().getAirwaybill().getAirwaybillNumber(),
                        shipmentRequest.getLogisticPartnerCode(), shipmentRequest.getCreatedBy());
                failReason = storeEcomLabel(shipmentRequest, awbNumber);
            } else {
                e = reverePickupResponse.getAirwaybillObjects().getAirwaybill().getErrorList() != null ? new Exception(objectMapper.writeValueAsString(reverePickupResponse.getAirwaybillObjects()
                        .getAirwaybill().getErrorList())) : null;
                failReason = messageSource.getMessage(String.valueOf(LogisticResponseCodes.FAILED_TO_PLACE_ECOM_REQUEST.getErrorCode()), new Object[] { "" }, null);
            }
            commonService.updateShipmentFailReason(shipmentRequest.getShipmentId(), failReason, e, shipmentRequest.getCreatedBy());
        } else {
            throw new Exception(messageSource.getMessage(String.valueOf(LogisticResponseCodes.NULL_AWB_GENERATION_RESPONSE_FROM_ECOM.getErrorCode()), new Object[] { "" }, null));
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Object trackShipment(HashMap trackingPayload) {
        TrackShipmentResponse response = new TrackShipmentResponse();
        response.setAwb((String) trackingPayload.get(AWB_PARAM_NAME));
        response.setStatus_update_number((String) trackingPayload.get(STATUS_CODE_NUMBER));
        try {
            ResponseDto<Object> baseResponse = (ResponseDto<Object>) super.trackShipment(trackingPayload);
            if (Constants.SUCCESS.equalsIgnoreCase(baseResponse.getStatus())) {
                response.setStatus(true);
            } else {
                if (!CollectionUtils.isEmpty(baseResponse.getInvalidData())) {
                    throw new Exception(baseResponse.getInvalidData().toString());
                } else {
                    throw new Exception(baseResponse.getMessage());
                }
            }
        } catch (Exception e) {
            response.setReason(e.getMessage());
            log.error("Exception occurred while getting Shipment Tracking History::" + trackingPayload, e);
        }
        return response;
    }

    @Override
    public Object cancelShipment(String logistincPartnerTrackingNumber) {
        return null;
    }

    public MultiValueMap<String, Object> ecomReversePickupPayload(ShipmentRequestDto shipmentRequestDto, String awbNumber, PartnerEventDetailDto eventMst) throws Exception {
        MultiValueMap<String, Object> reverseRequest = ecomExpressProxy.addAuthenticationParam(eventMst);
        EcomExpressObjects ecomExpressObjects = new EcomExpressObjects();
        Shipment shipment = prepareShipmentPayload(shipmentRequestDto, awbNumber);
        ecomExpressObjects.setShipment(shipment);
        HashMap<String, Object> expObje = new HashMap<String, Object>();
        expObje.put("ECOMEXPRESS-OBJECTS", ecomExpressObjects);
        reverseRequest.add("json_input", objectMapper.writeValueAsString(expObje));
        log.info("paload ::::" + new ObjectMapper().writeValueAsString(expObje));
        log.info("ecomReversePickupPayload payload::" + reverseRequest);

        return reverseRequest;
    }

    public Shipment prepareShipmentPayload(ShipmentRequestDto shipmentRequestDto, String awbNumber) throws Exception {
        Shipment ship = new Shipment();
        ship.setAWBNumber(awbNumber);
        ship.setOrderNumber(shipmentRequestDto.getServiceRequestDetails().getRefPrimaryTrackingNo());
        ship.setProduct(EcomShipmentType.REVERSE_PICKUP.getShipmentType());

        ServiceAddressDetailDto originAddress = shipmentRequestDto.getOriginAddressDetails();
        PincodeMasterDto stateCityResponse = commonService.getStateAndCity(originAddress.getPincode());
        ship.populateRevPickupAdress(originAddress, stateCityResponse);
        ship.setItemDescription(ECOM_REVERSE_PICKUP_ITEM_DESCRIPTION);

        ship.setAddOnService(new ArrayList<String>(Arrays.asList(ECOM_REVERSE_PICKUP_ADDON_SERVICES.split(","))));
        ship.populateAssetDetails(shipmentRequestDto, commonService.getInvoiceValue(shipmentRequestDto.getShipmentDeclareValue()));
        ServiceAddressDetailDto destinationAddress = shipmentRequestDto.getDestinationAddressDetails();
        ship.populateDropAddress(destinationAddress);

        return ship;
    }

    public MultiValueMap<String, Object> prepareForwadShipment(ShipmentRequestDto shipmentDto, String awb, PartnerEventDetailDto eventMst) throws Exception {
        MultiValueMap<String, Object> reverseRequest = ecomExpressProxy.addAuthenticationParam(eventMst);
        List<ForwardShipmentRequest> josnArray = prepareJsonDataForwardShipment(shipmentDto, awb);
        reverseRequest.add("json_input", objectMapper.writeValueAsString(josnArray));
        return reverseRequest;
    }

    public List<ForwardShipmentRequest> prepareJsonDataForwardShipment(ShipmentRequestDto shipment, String awb) throws Exception {
        ForwardShipmentRequest forward = new ForwardShipmentRequest();
        List<ForwardShipmentRequest> fwdShipments = new ArrayList<ForwardShipmentRequest>();
        log.info("prepareJsonDataForwardShipment :" + awb);
        forward.setAWBNmuber(awb);
        forward.setOrderNumber(shipment.getServiceRequestDetails().getRefPrimaryTrackingNo());
        forward.populateAssetDetails(shipment, commonService.getInvoiceValue(shipment.getShipmentDeclareValue()));
        forward.setProduct(EcomShipmentType.FORWARD_PICKUP.getShipmentType());
        ServiceAddressDetailDto destinationAddress = shipment.getDestinationAddressDetails();
        PincodeMasterDto stateCityResponse = commonService.getStateAndCity(destinationAddress.getPincode());

        forward.populateConsigneeAddress(destinationAddress, stateCityResponse);

        ServiceAddressDetailDto originAddress = shipment.getOriginAddressDetails();
        forward.setPickupName(originAddress.getAddresseeFullName());

        forward.populatePickupAndReturnAddress(originAddress);
        log.info("PrepareForwardShipment forward request " + forward);
        fwdShipments.add(forward);

        return fwdShipments;
    }

    private String storeEcomLabel(ShipmentRequestDto serviceRequestdto, String awbNumber) {
        String labelCreationStatus = null;
        log.info("storeEcomLabel Awb number" + awbNumber);
        try {

            String ecomHtmlDoc = populateLabelValues(serviceRequestdto, awbNumber);
            CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            String labelFile = shipmentDocFolderPath + OUTBOUND_LABEL + "_" + awbNumber + ".pdf";
            log.info("Ecom label name for awb " + awbNumber + " is " + labelFile);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(labelFile)));
            document.open();

            // these configurations are required to pdf writer to apply css and to render image.
            HtmlPipelineContext htmlContext = new HtmlPipelineContext();
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
            htmlContext.setImageProvider(new Base64ImageProvider());

            PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
            HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
            CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

            XMLWorker worker = new XMLWorker(css, true);
            XMLParser p = new XMLParser(worker);
            p.parse(new StringInputStream(ecomHtmlDoc.toString()));
            document.close();
            log.info("end generateEcomLabel awb number " + awbNumber);
            File label = new File(labelFile);
            commonService.storeMongoRefInDoc(serviceRequestdto.getServiceRequestDetails().getServiceRequestId(), labelFile, label);
        } catch (Exception e) {
            labelCreationStatus = messageSource.getMessage(String.valueOf(LogisticResponseCodes.FAILED_TO_GENERATE_LABEL.getErrorCode()), new Object[] { "" }, null) + ":" + e.getMessage();
            log.error("Exception while storing EcomLabel", e);
        }
        log.info("end storeEcomLabel Awb number" + awbNumber);
        return labelCreationStatus;
    }

    class Base64ImageProvider extends AbstractImageProvider {

        @Override
        public Image retrieve(String src) {
            int pos = src.indexOf("base64,");
            try {
                if (src.startsWith("data") && pos > 0) {
                    byte[] img = Base64.decode(src.substring(pos + 7));
                    return Image.getInstance(img);
                } else {
                    return Image.getInstance(src);
                }
            } catch (BadElementException ex) {
                return null;
            } catch (IOException ex) {
                return null;
            }
        }

        @Override
        public String getImageRootPath() {
            return null;
        }
    }

    public String populateLabelValues(ShipmentRequestDto shipmentSearchDto, String awbNumber) throws Exception {
        HashMap<String, Object> model = new HashMap<String, Object>();
        String htmlString = null;
        try {
            shipmentSearchDto.setLogisticPartnerRefTrackingNumber(awbNumber);
            model.put("shipmentDetails", shipmentSearchDto);
            /** Rendering barcode image **/
            model.put(ECOM_BARCODE_IMAGE_ID, barcodeGenerator.generateBarcodeInBase64String(awbNumber));
            htmlString = templateParser.parseTemplate(TEMPLATE_PATH_NAME, ECOM_LABEL_VM_FILE_NAME, model);
        } catch (Exception e) {
            log.error("Exception while parsing template ::" + TEMPLATE_PATH_NAME + ECOM_LABEL_VM_FILE_NAME, e);
            throw new Exception(e);
        }
        log.debug("End parseTemplate::" + htmlString);
        return htmlString;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List<ErrorInfoDto> doValidateShipmentTrackingRequest(HashMap trackingRequest) {
        log.info(">>> Validation for Mandatory Data Track Shipment :" + trackingRequest);
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();
        if (!CollectionUtils.isEmpty(trackingRequest)) {
            String awb = (String) trackingRequest.get(AWB_PARAM_NAME);
            String reasonCode = (String) trackingRequest.get(REASON_CODE_NUMBER);
            if (StringUtils.isEmpty(awb)) {
                inputValidator.populateMandatoryFieldError("awb", errorInfoDtoList);
            }
            if (StringUtils.isEmpty(reasonCode)) {
                inputValidator.populateMandatoryFieldError("reasonCode", errorInfoDtoList);
            }
        } else {
            inputValidator.populateMandatoryFieldError("trackingRequest", errorInfoDtoList);
        }

        return errorInfoDtoList;
    }
}
