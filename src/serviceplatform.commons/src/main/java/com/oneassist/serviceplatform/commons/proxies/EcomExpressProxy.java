package com.oneassist.serviceplatform.commons.proxies;

import com.oneassist.serviceplatform.commons.enums.EcomShipmentType;
import com.oneassist.serviceplatform.commons.proxies.base.BaseProxy;
import com.oneassist.serviceplatform.contracts.dtos.PartnerEventDetailDto;
import com.oneassist.serviceplatform.externalcontracts.ClaimLifecycleEvent;
import com.oneassist.serviceplatform.externalcontracts.ecom.AwbGenerationResponse;
import com.oneassist.serviceplatform.externalcontracts.ecom.ForwardShipmentResponse;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class EcomExpressProxy extends BaseProxy {

    private final Logger log = Logger.getLogger(EcomExpressProxy.class);

    @Override
    protected HttpHeaders getHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return requestHeaders;
    }

    @Override
    protected String getBaseUrl() {
        return null;
    }

    public String generateAWB(EcomShipmentType shipmentType) throws Exception {
        String awbNumber = null;
        PartnerEventDetailDto eventMst = getPartnerEventMst(ClaimLifecycleEvent.ECOM_AWB.toString());
        MultiValueMap<String, Object> awbPaylod = addAuthenticationParam(eventMst);
        log.info("awbGenerationRequest type" + shipmentType.getShipmentType());
        awbPaylod.add("count", "1");
        awbPaylod.add("type", shipmentType.getShipmentType());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(awbPaylod, getHeaders());
        ResponseEntity<AwbGenerationResponse> responseOject = callUrl(AwbGenerationResponse.class, eventMst.getConnectionDetail(), HttpMethod.POST, requestEntity);
        if (responseOject != null && responseOject.getBody() != null) {
            AwbGenerationResponse awbResponse = responseOject.getBody();
            if (awbResponse != null && !CollectionUtils.isEmpty(awbResponse.getAwb())) {
                awbNumber = awbResponse.getAwb().get(0);
            } else {
                throw new Exception("AWB generation response is null");
            }
        } else {
            throw new Exception("Null Response from ecom awb generation request");
        }
        return awbNumber;
    }

    public ResponseEntity<ForwardShipmentResponse> placeForwardPickupRequest(MultiValueMap<String, Object> ecomForwardPayload) throws Exception {
        PartnerEventDetailDto eventMst = getPartnerEventMst(ClaimLifecycleEvent.ECOM_FORWARD_SHIPMENT.toString());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(ecomForwardPayload, getHeaders());
        ResponseEntity<ForwardShipmentResponse> responseOject = callUrl(ForwardShipmentResponse.class, eventMst.getConnectionDetail(), HttpMethod.POST, requestEntity);
        return responseOject;
    }

    public ResponseEntity<String> placeReversePickupRequest(MultiValueMap<String, Object> ecomReversePickupRequest) throws Exception {
        PartnerEventDetailDto eventMst = getPartnerEventMst(ClaimLifecycleEvent.ECOM_REVERSE_PICKUP.toString());
        log.info("Ecom Reverse shipment request::" + ecomReversePickupRequest);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(ecomReversePickupRequest, getHeaders());
        ResponseEntity<String> responseOject = callUrl(String.class, eventMst.getConnectionDetail(), HttpMethod.POST, requestEntity);
        return responseOject;
    }

    public MultiValueMap<String, Object> addAuthenticationParam(PartnerEventDetailDto eventMst) {
        MultiValueMap<String, Object> requestPayload = new LinkedMultiValueMap<String, Object>();

        requestPayload.add("username", eventMst.getUserName());
        requestPayload.add("password", eventMst.getPassword());
        return requestPayload;
    }
}
