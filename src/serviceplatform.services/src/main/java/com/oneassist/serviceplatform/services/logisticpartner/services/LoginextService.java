package com.oneassist.serviceplatform.services.logisticpartner.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneassist.serviceplatform.commons.entities.ShipmentReassignEntity;
import com.oneassist.serviceplatform.commons.enums.LogisticResponseCodes;
import com.oneassist.serviceplatform.commons.proxies.OasysProxy;
import com.oneassist.serviceplatform.commons.repositories.LogisticShipmentReassignmentRepository;
import com.oneassist.serviceplatform.commons.validators.InputValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.PartnerEventDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceAddressDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentAssetDetailsDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentUpdateRequestDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.externalcontracts.ClaimLifecycleEvent;
import com.oneassist.serviceplatform.externalcontracts.HubMasterDto;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;
import com.oneassist.serviceplatform.externalcontracts.loginext.LoginextDeliveryRequest;
import com.oneassist.serviceplatform.externalcontracts.loginext.LoginextPickupRequest;
import com.oneassist.serviceplatform.externalcontracts.loginext.LoginextShipmentResponse;
import com.oneassist.serviceplatform.externalcontracts.loginext.ShipmentCrateMapping;
import com.oneassist.serviceplatform.externalcontracts.loginext.ShipmentLineItem;
import com.oneassist.serviceplatform.services.commons.ICommonService;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service("loginextService")
public class LoginextService implements ILogisticPartnerService {

    private final Logger log = Logger.getLogger(LoginextService.class);

    @Autowired
    @Qualifier("commonService")
    private ICommonService commonService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private InputValidator inputValidator;

    @Autowired
    private LogisticShipmentReassignmentRepository logisticShipmentRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private OasysProxy oasysProxy;

    @Override
    public Object createShipment(ShipmentRequestDto shipmentRequest) throws Exception {
        boolean status = false;
        if (shipmentRequest != null) {
            if (shipmentRequest.getShipmentType().intValue() == 2) {
                LoginextDeliveryRequest deliveryRequest = populateDeliveryRequest(shipmentRequest);
                List<LoginextDeliveryRequest> requestArray = new ArrayList<LoginextDeliveryRequest>();
                requestArray.add(deliveryRequest);
                placeLoginextRequest(new ObjectMapper().writeValueAsString(requestArray), shipmentRequest);
                status = true;
            } else if (shipmentRequest.getShipmentType().intValue() == 1) {
                LoginextPickupRequest pickupRequest = populatePickupRequest(shipmentRequest);
                List<LoginextPickupRequest> requestArray = new ArrayList<LoginextPickupRequest>();
                requestArray.add(pickupRequest);
                placeLoginextRequest(new ObjectMapper().writeValueAsString(requestArray), shipmentRequest);
                status = true;
            } else {
                log.error("Invlid shipment type ::" + shipmentRequest.getShipmentType());
            }
        } else {
            log.error("Shipment Request details object is empty");
        }
        return status;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object trackShipment(HashMap trackingPayload) {
        ResponseDto<String> response = new ResponseDto<String>();
        List<ErrorInfoDto> errorInfoList = doValidateShipmentTrackingRequest(trackingPayload);
        try {
            if (null != errorInfoList && errorInfoList.size() > 0) {
                response.setInvalidData(errorInfoList);
                response.setStatus(ResponseConstant.FAILED);
                response.setMessage("Failed");
            } else {
                String orderNo = (String) trackingPayload.get("orderNo");
                String notificationType = (String) trackingPayload.get("notificationType");
                String clientShipmentId = (String) trackingPayload.get("clientShipmentId");
                String shipmentId = !StringUtils.isEmpty(orderNo) ? orderNo : clientShipmentId;
                log.info("Shipment id for tracking is ::" + shipmentId);
                ShipmentReassignEntity shipment = logisticShipmentRepository.findOne(Long.parseLong(shipmentId));
                if (shipment != null) {
                    commonService.updateShipmentStage(shipment, notificationType, null);
                    response.setStatus(ResponseConstant.SUCCESS);
                    response.setMessage(messageSource.getMessage((String.valueOf(LogisticResponseCodes.SHIPMENT_TRACKINGHISTORY_SUCCESS.getErrorCode())), new Object[] { "" }, null));
                } else {
                    errorInfoList = new ArrayList<ErrorInfoDto>();
                    ErrorInfoDto errorInfo = new ErrorInfoDto();
                    errorInfo.setErrorCode(LogisticResponseCodes.SHIPMENT_DOESNOT_EXIST.getErrorCode());
                    errorInfo.setErrorMessage(messageSource.getMessage((String.valueOf(LogisticResponseCodes.SHIPMENT_DOESNOT_EXIST.getErrorCode())), new Object[] { "" }, null));
                    errorInfoList.add(errorInfo);
                    response.setInvalidData(errorInfoList);
                    response.setStatus(ResponseConstant.FAILED);
                    response.setStatusCode(String.valueOf(LogisticResponseCodes.REASSIGN_SHIPMENT_REQUEST_FAILED.getErrorCode()));
                }
            }
        } catch (Exception e) {
            response.setStatus(ResponseConstant.FAILED);
            response.setMessage(messageSource.getMessage((String.valueOf(LogisticResponseCodes.SHIPMENT_TRACKINGHISTORY_FAILED.getErrorCode())), new Object[] { "" }, null));
            log.error("Exception occurred while getting Shipment Tracking History::" + trackingPayload, e);
        }
        return response;
    }

    @Override
    public Object cancelShipment(String logistincPartnerTrackingNumber) throws Exception {
        boolean status = false;
        PartnerEventDetailDto partnerEventMst = commonService.getPartnerEventMst(ClaimLifecycleEvent.LOGINEXT_SHIPMENT_CANCELLATION.toString());
        if (partnerEventMst != null) {
            log.info("--------- partnerEventMst ------------------" + partnerEventMst);
            List<String> referenceNumbers = Arrays.asList(logistincPartnerTrackingNumber);
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(referenceNumbers.toString(), getHeader(partnerEventMst));
            ResponseEntity<LoginextShipmentResponse> responseOject = restTemplate.exchange(partnerEventMst.getConnectionDetail(), HttpMethod.PUT, requestEntity, LoginextShipmentResponse.class);
            if (responseOject != null && responseOject.getBody() != null) {
                LoginextShipmentResponse loginextResponse = responseOject.getBody();
                if (loginextResponse.getHasError() || loginextResponse.getStatus().intValue() != LOGINEXT_SUCCESS_CODE) {
                    log.error("Exception while cancelling order with reference id ::" + logistincPartnerTrackingNumber + loginextResponse.getData() + "");
                }
            } else {
                throw new Exception("Null Response from loginext cancellation request" + logistincPartnerTrackingNumber);
            }
        } else {
            throw new Exception("Partner event master is null");
        }
        return status;
    }

    private LoginextPickupRequest populatePickupRequest(ShipmentRequestDto shipmentRequest) throws Exception {
        HubMasterDto hubMst = commonService.getHubDetails(shipmentRequest.getHubId());
        Calendar cal = Calendar.getInstance();
        LoginextPickupRequest pickupRequest = new LoginextPickupRequest();
        cal.add(Calendar.HOUR, -5);
        cal.add(Calendar.MINUTE, -30);
        pickupRequest.setShipmentOrderDt(shipmentOrderDateFormat.format(cal.getTime()).replaceAll("'", ""));
        pickupRequest.setAwbNumber(String.valueOf(shipmentRequest.getServiceRequestDetails().getRefPrimaryTrackingNo()));
        pickupRequest.setCancellationAllowedFl(YES);
        pickupRequest.setOrderState(ORDER_STATE);
        pickupRequest.setDistributionCenter(hubMst.getOchmHubName());
        pickupRequest.setShipmentOrderTypeCd(PICKUP_ORDER_TYPE_CD);
        pickupRequest.setOrderNo(String.valueOf(shipmentRequest.getShipmentId()));
        String invoice = commonService.getInvoiceValue(shipmentRequest.getShipmentDeclareValue());
        pickupRequest.setPackageValue(invoice);
        pickupRequest.setPackageWeight(String.valueOf(shipmentRequest.getBoxActualWeight()));
        pickupRequest.setPaymentType(PAYMENT_TYPE);
        ServiceAddressDetailDto originAddress = shipmentRequest.getOriginAddressDetails();
        pickupRequest.setPickupAccountName(originAddress.getAddresseeFullName());
        pickupRequest.setPickupAccountCode(originAddress.getAddresseeFullName());
        pickupRequest.setPickupApartment(originAddress.getAddressLine1());
        pickupRequest.setPickupStreetName(originAddress.getAddressLine1());
        pickupRequest.setPickupStartTimeWindow(shipmentOrderDateFormat.format(cal.getTime()).replaceAll("'", ""));
        cal.add(Calendar.DATE, 2);
        pickupRequest.setPickupEndTimeWindow(shipmentOrderDateFormat.format(cal.getTime()).replaceAll("'", ""));
        pickupRequest.setPickupEmail(originAddress.getEmail());
        pickupRequest.setPickupPhoneNumber(String.valueOf(originAddress.getMobileNo()));
        pickupRequest.setPickupPinCode(originAddress.getPincode());
        pickupRequest.setPickupBranch(hubMst.getOchmHubName());
        PincodeMasterDto stateCityResponseDto = commonService.getStateAndCity(originAddress.getPincode());
        if (stateCityResponseDto == null) {
            throw new Exception("Invalid pincode :" + originAddress.getPincode());
        }
        pickupRequest.setPickupCity(stateCityResponseDto.getCityName());
        pickupRequest.setPickupLocality(stateCityResponseDto.getCityName());
        pickupRequest.setPickupState(commonService.getStateCode(stateCityResponseDto.getStateCode()));
        pickupRequest.setPickupCountry(COUNTRY_CODE);
        pickupRequest.setShipmentCrateMappings(getCreateMapping(shipmentRequest, invoice));
        pickupRequest.setNumberOfItems(String.valueOf(pickupRequest.getShipmentCrateMappings().size()));
        pickupRequest.setDeliveryType(DELIVERY_TYPE);
        return pickupRequest;
    }

    private LoginextDeliveryRequest populateDeliveryRequest(ShipmentRequestDto shipmentRequest) throws Exception {
        Calendar cal = Calendar.getInstance();
        HubMasterDto hubMst = commonService.getHubDetails(shipmentRequest.getHubId());
        LoginextDeliveryRequest deliveryRequest = new LoginextDeliveryRequest();
        cal.add(Calendar.HOUR, -5);
        cal.add(Calendar.MINUTE, -30);
        deliveryRequest.setShipmentOrderDt(shipmentOrderDateFormat.format(cal.getTime()).replaceAll("'", ""));
        deliveryRequest.setAwbNumber(String.valueOf(shipmentRequest.getServiceRequestDetails().getRefPrimaryTrackingNo()));
        deliveryRequest.setCancellationAllowedFl(YES);
        deliveryRequest.setDistributionCenter(hubMst.getOchmHubName());
        deliveryRequest.setShipmentOrderTypeCd(DELIVERY_ORDER_TYPE_CD);
        deliveryRequest.setOrderState(ORDER_STATE);
        deliveryRequest.setOrderNo(String.valueOf(shipmentRequest.getShipmentId()));
        String invoice = commonService.getInvoiceValue(shipmentRequest.getShipmentDeclareValue());
        deliveryRequest.setPackageValue(invoice);
        deliveryRequest.setPackageWeight(String.valueOf(shipmentRequest.getBoxActualWeight()));
        deliveryRequest.setPaymentType(PAYMENT_TYPE);
        ServiceAddressDetailDto destinationAddress = shipmentRequest.getDestinationAddressDetails();
        deliveryRequest.setDeliverAccountCode(destinationAddress.getAddresseeFullName());
        deliveryRequest.setDeliverAccountName(destinationAddress.getAddresseeFullName());
        deliveryRequest.setDeliverApartment(destinationAddress.getAddressLine1());
        deliveryRequest.setDeliverStreetName(destinationAddress.getAddressLine1());
        deliveryRequest.setDeliverStartTimeWindow(shipmentOrderDateFormat.format(cal.getTime()).replaceAll("'", ""));
        cal.add(Calendar.DATE, 2);
        deliveryRequest.setDeliverEndTimeWindow(shipmentOrderDateFormat.format(cal.getTime()).replaceAll("'", ""));
        deliveryRequest.setDeliverEmail(destinationAddress.getEmail());
        deliveryRequest.setDeliverPhoneNumber(String.valueOf(destinationAddress.getMobileNo()));
        deliveryRequest.setDeliverPinCode(destinationAddress.getPincode());
        PincodeMasterDto stateCityResponseDto = commonService.getStateAndCity(destinationAddress.getPincode());
        if (stateCityResponseDto == null) {
            throw new Exception("Invalid pincode :" + destinationAddress.getPincode());
        }
        deliveryRequest.setDeliverCity(stateCityResponseDto.getCityName());
        deliveryRequest.setDeliverLocality(stateCityResponseDto.getCityName());
        deliveryRequest.setDeliverState(commonService.getStateCode(stateCityResponseDto.getStateCode()));
        deliveryRequest.setDeliverCountry(COUNTRY_CODE);
        deliveryRequest.setReturnBranch(hubMst.getOchmHubName());
        deliveryRequest.setDeliverBranch(hubMst.getOchmHubName());
        deliveryRequest.setShipmentCrateMappings(getCreateMapping(shipmentRequest, invoice));
        deliveryRequest.setNumberOfItems(String.valueOf(deliveryRequest.getShipmentCrateMappings().size()));
        deliveryRequest.setDeliveryType(DELIVERY_TYPE);
        return deliveryRequest;
    }

    public void placeLoginextRequest(String payload, ShipmentRequestDto shipmentDto) throws Exception {
        PartnerEventDetailDto partnerEventMst = commonService.getPartnerEventMst(ClaimLifecycleEvent.LOGINEXT_SHIPMENT.toString());
        if (partnerEventMst != null) {
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(payload, getHeader(partnerEventMst));
            log.error("Loginext request ::" + payload);
            ResponseEntity<String> responseOject = restTemplate.exchange(partnerEventMst.getConnectionDetail(), HttpMethod.POST, requestEntity, String.class);
            if (responseOject != null && responseOject.getBody() != null) {
                log.error("Loginext Response is : " + responseOject.getBody());
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                commonService.storeThirdPartyInteractionDetailsInMongo(Long.valueOf(shipmentDto.getServiceRequestDetails().getRefPrimaryTrackingNo()), ClaimLifecycleEvent.LOGINEXT_SHIPMENT, payload,
                        responseOject.getBody(), null);
                LoginextShipmentResponse loginextResponse = objectMapper.readValue(responseOject.getBody(), LoginextShipmentResponse.class);
                if (loginextResponse.getStatus() == 200 && !loginextResponse.getHasError()) {
                    if (!CollectionUtils.isEmpty(loginextResponse.getReferenceId())) {
                        ShipmentUpdateRequestDto updateRequest = new ShipmentUpdateRequestDto();
                        updateRequest.setShipmentId(String.valueOf(shipmentDto.getShipmentId()));
                        updateRequest.setModifiedBy(shipmentDto.getCreatedBy());
                        updateRequest.setLogisticPartnerRefTrackingNumber(loginextResponse.getReferenceId().get(0));
                        updateRequest.setLogisticPartnerCode(shipmentDto.getLogisticPartnerCode());
                        commonService.updateShipmentAWB(shipmentDto.getShipmentId(), loginextResponse.getReferenceId().get(0), shipmentDto.getLogisticPartnerCode(), shipmentDto.getCreatedBy());
                        ShipmentUpdateRequestDto updateDto = new ShipmentUpdateRequestDto();
                        updateDto.setShipmentId(String.valueOf(shipmentDto.getShipmentId()));
                        commonService.updateShipmentFailReason(shipmentDto.getShipmentId(), null, null, shipmentDto.getModifiedBy());
                    } else {
                        throw new Exception("Got empty reference id from loginext");
                    }
                } else {
                    throw new Exception("Got invalid response from loginext " + loginextResponse.getError() != null ? objectMapper.writeValueAsString(loginextResponse.getError()) : "");
                }
            } else {
                throw new Exception("Null Response from loginext cancellation request" + shipmentDto.getLogisticPartnerRefTrackingNumber());
            }
        } else {
            throw new Exception("No partner event has been configured for " + ClaimLifecycleEvent.LOGINEXT_SHIPMENT.toString());
        }
    }

    private List<ShipmentCrateMapping> getCreateMapping(ShipmentRequestDto shipment, String invoice) {
        List<ShipmentCrateMapping> crateMappings = new ArrayList<ShipmentCrateMapping>();
        ShipmentCrateMapping crateMapping = new ShipmentCrateMapping();
        if (!CollectionUtils.isEmpty(shipment.getShipmentAssetDetails())) {
            List<ShipmentLineItem> lineItems = new ArrayList<ShipmentLineItem>();
            Double crateAmount = new Double("0");
            for (ShipmentAssetDetailsDto asset : shipment.getShipmentAssetDetails()) {
                ShipmentLineItem lineItem = new ShipmentLineItem();
                lineItem.setItemCd(asset.getAssetReferenceNo());
                lineItem.setItemName(asset.getAssetMakeName() + " " + asset.getAssetModelName());
                lineItem.setItemPrice(Double.valueOf(invoice));
                lineItem.setItemQuantity(asset.getAssetPieceCount());
                lineItem.setItemType(ITEM_TYPE);
                crateAmount = crateAmount + new Double(invoice);
                lineItem.setItemWeight(asset.getAssetActualWeight());
                lineItems.add(lineItem);
            }
            crateMapping.setCrateAmount(crateAmount);
            crateMapping.setCrateCd(CRATE_CD + shipment.getShipmentId());
            crateMapping.setNoOfUnits(lineItems.size());
            crateMapping.setShipmentlineitems(lineItems);
            crateMapping.setCrateType(ITEM_TYPE);
        }
        crateMappings.add(crateMapping);
        return crateMappings;
    }

    private HttpHeaders getHeader(PartnerEventDetailDto partnerEventMst) {

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(LOGINEXT_AUTHENTICATION_HEADER_NAME, partnerEventMst.getUserName());
        requestHeaders.add(LOGINEXT_SECRETE_HEADER_NAME, partnerEventMst.getPassword());
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        return requestHeaders;
    }

    @SuppressWarnings("rawtypes")
    public List<ErrorInfoDto> doValidateShipmentTrackingRequest(HashMap trackingRequest) {
        log.info(">>> Validation for Mandatory Data Track Shipment :" + trackingRequest);
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();
        if (!CollectionUtils.isEmpty(trackingRequest)) {
            String notificationType = (String) trackingRequest.get("notificationType");
            String clientShipmentId = (String) trackingRequest.get("clientShipmentId");
            String orderNo = (String) trackingRequest.get("orderNo");
            if (StringUtils.isEmpty(notificationType)) {
                inputValidator.populateMandatoryFieldError("notificationType", errorInfoDtoList);
            }
            if (StringUtils.isEmpty(clientShipmentId) && StringUtils.isEmpty(orderNo)) {
                inputValidator.populateMandatoryFieldError("clientShipmentId/orderNo", errorInfoDtoList);
            }
        } else {
            inputValidator.populateMandatoryFieldError("trackingRequest", errorInfoDtoList);
        }

        return errorInfoDtoList;
    }

    private static String YES = "Y";
    private static String PAYMENT_TYPE = "Prepaid";
    private static String COUNTRY_CODE = "IND";
    private static String DELIVERY_ORDER_TYPE_CD = "DELIVER";
    private static String PICKUP_ORDER_TYPE_CD = "PICKUP";
    private static String ORDER_STATE = "FORWARD";
    private static String ITEM_TYPE = "Mobile";
    private static String CRATE_CD = "CRATE_";
    private static SimpleDateFormat shipmentOrderDateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static final String OA_SERVICE_PARTNER_MAPPING = "OA_SERVICE_PARTNER_MAPPING";
    public static final String LOGINEXT_SHIPMENT_CANCELLATION_EVENT = "LoginextShipmentCancellation";
    public static final String LOGINEXT_AUTHENTICATION_HEADER_NAME = "WWW-Authenticate";
    public static final String LOGINEXT_SECRETE_HEADER_NAME = "CLIENT_SECRET_KEY";
    public static final int LOGINEXT_SUCCESS_CODE = 200;

    public static final String DELIVERY_TYPE = "DLBOY";
}
