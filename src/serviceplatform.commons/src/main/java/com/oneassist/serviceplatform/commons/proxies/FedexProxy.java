package com.oneassist.serviceplatform.commons.proxies;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import com.oneassist.serviceplatform.commons.proxies.base.BaseProxy;
import com.oneassist.serviceplatform.contracts.dtos.PartnerEventDetailDto;
import com.oneassist.serviceplatform.externalcontracts.ClaimLifecycleEvent;
import com.oneassist.serviceplatform.externalcontracts.fedex.PickupAvailabilityRequestWrapper;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.TrackRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FedexProxy extends BaseProxy {

    private final Logger logger = Logger.getLogger(FedexProxy.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected HttpHeaders getHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.TEXT_XML);
        return requestHeaders;
    }

    @Override
    protected String getBaseUrl() {
        return null;
    }

    public String checkPickupAvailability(PickupAvailabilityRequestWrapper payload) throws Exception {
        String response = null;
        try {

            PartnerEventDetailDto eventMst = getPartnerEventMst(ClaimLifecycleEvent.FEDEX_PICKUP.toString());
            logger.info("Fedex pickup request ::" + payload);
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(payload, getHeaders());
            ResponseEntity<String> responseOject = restTemplate.exchange(eventMst.getConnectionDetail(), HttpMethod.POST, requestEntity, String.class);
            logger.info("Fedex pickup request ::" + responseOject.getBody());
            if (responseOject != null && responseOject.getBody() != null) {
                response = removeSchemaPrefix(responseOject.getBody());
            }
        } catch (Exception ex) {
            logger.info("Exception in pickupAvailabilityProcess :", ex);
            throw ex;
        }
        return response;
    }

    public String processShipment(String requestWrapper) throws Exception {
        logger.info("Inside the processShipment  -Start" + requestWrapper);
        String response = null;
        try {
            PartnerEventDetailDto eventMst = getPartnerEventMst(ClaimLifecycleEvent.FEDEX_PROCESS_SHIPMENT.toString());
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestWrapper, getHeaders());
            ResponseEntity<String> responseOject = this.callUrl(String.class, eventMst.getConnectionDetail(), HttpMethod.POST, requestEntity);
            logger.error("Fedex response :" + responseOject);
            if (responseOject != null && responseOject.getBody() != null) {
                response = removeSchemaPrefix(responseOject.getBody());
            } else {
                throw new Exception("Null Response from fedex process shipment request");
            }
        } catch (Exception ex) {
            logger.error("Detail error", ex);
            throw ex;
        }
        logger.info("Inside the processShipment  -End");
        return response;

    }

    public String trackShipment(TrackRequest trackRequest) throws Exception {
        PartnerEventDetailDto eventMst = getPartnerEventMst(ClaimLifecycleEvent.FEDEX_TRACK.toString());
        String response = null;
        JAXBContext context = JAXBContext.newInstance(TrackRequest.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(trackRequest, System.out);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(trackRequest, getHeaders());
        ResponseEntity<String> responseOject = this.callUrl(String.class, eventMst.getConnectionDetail(), HttpMethod.POST, requestEntity);
        logger.error("shipment tracking response ::" + responseOject);
        if (responseOject != null && responseOject.getBody() != null) {
            response = removeSchemaPrefix(responseOject.getBody());
        } else {
            throw new Exception("Null Response from fedex process shipment request");
        }

        return response;
    }

    private String removeSchemaPrefix(String requestString) {
        return requestString.replaceAll("v19:", "").replaceAll(":v19", "").replaceAll("v13:", "").replaceAll(":v13", "").replaceAll("v12:", "").replaceAll(":v12", "");
    }
}
