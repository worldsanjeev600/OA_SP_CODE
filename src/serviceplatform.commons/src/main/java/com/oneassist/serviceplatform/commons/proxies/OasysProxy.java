package com.oneassist.serviceplatform.commons.proxies;

import com.google.gson.Gson;
import com.oneassist.serviceplatform.commons.cache.base.RestTemplateConfiguration;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestStatus;
import com.oneassist.serviceplatform.commons.proxies.base.BaseProxy;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.EmailDto;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.SmsDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CloseRepairSREventDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CreateClaimDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.OASYSCustMemDetails;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.OASYSCustMemDetailsResponse;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.PaymentDto;
import com.oneassist.serviceplatform.contracts.response.CustMemViewResponse;
import com.oneassist.serviceplatform.contracts.response.CustomerHomeApplianceAssetResponse;
import com.oneassist.serviceplatform.contracts.response.StateCityResponseDto;
import com.oneassist.serviceplatform.externalcontracts.ClaimLifecycleEvent;
import com.oneassist.serviceplatform.externalcontracts.CustMemView;
import com.oneassist.serviceplatform.externalcontracts.MembershipDetailsDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OasysProxy extends BaseProxy {

    private final Logger logger = Logger.getLogger(OasysProxy.class);

    public static final String DATA = "data";
    public static final String MEMBERSHIP = "membership";
    public static final String ASSETS = "assets";
    public static final String MEM_ID = "memId";
    public static final String ASSET_REQUIRED = "assetRequired";
    public static final String ASSET_ID = "assetId";
    public static final String CATEGORY = "category";
    public static final String ASSET_DOCUMENT_REQUIRED = "assetDocRequired";

    @Value("${OASYSUrl}")
    private String oasysUrl;

    @Value("${OASYSUrlPrivate}")
    private String oasysUrlPrivate;

    @Value("${OASYSClaimBaseUrl}")
    private String oasysClaimBaseUrl;

    @Value("${oasysUsername}")
    private String oasysUsername;

    @Value("${oasysPassword}")
    private String oasysPassword;

    @Value("${getStateAndCity}")
    private String getStateAndCity;

    @Value("${ShipmentDocumentDownload}")
    private String shipmentDocumentDownload;

    @Value("${PublishShipment}")
    private String publishShipment;

    @Value("${getCustomerMembershipDetail}")
    private String getCustomerMembershipDetail;

    @Value("${ServiceDocumentByteArrayDownload}")
    private String serviceDocumentByteArrayDownload;

    @Value("${ServiceDocumentUpload}")
    private String ServiceDocumentUpload;

    @Value("${ServiceDocumentZipFileDownload}")
    private String ServiceDocumentZipFileDownload;

    @Value("${ServiceDocumentImageFileDownload}")
    private String ServiceDocumentImageFileDownload;

    @Value("${ServiceDocumentByteArrayDownload}")
    private String ServiceDocumentByteArrayDownload;

    @Value("${ShipmentDocumentUpload}")
    private String ShipmentDocumentUpload;

    @Value("${SMSCommunication}")
    private String smsCommunication;

    @Value("${EMAILCommunication}")
    private String emailCommunication;

    @Value("${Authorization}")
    private String authorization;

    @Value("${saveClaim}")
    private String saveClaim;

    @Value("${advices}")
    private String advices;

    @Value("${getOasysMembershipDetails}")
    private String getMembershipDetails;

    @Autowired
    private RestTemplateConfiguration restTemplateConfiguration;

    @Override
    protected HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("userName", oasysUsername);
        headers.set("passWord", oasysPassword);
        headers.set("Authorization", authorization);

        return headers;
    }

    @Override
    protected String getBaseUrl() {
        return oasysUrl;
    }

    public StateCityResponseDto getStateAndCity(String pinCode) {

        StateCityResponseDto stateCityResponseDto = null;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.getBaseUrl() + getStateAndCity).path(pinCode);

        ResponseEntity<StateCityResponseDto> httpResponse = this.callUrl(StateCityResponseDto.class, builder.toUriString(), HttpMethod.GET);

        if (httpResponse != null) {
            stateCityResponseDto = httpResponse.getBody();
        }

        return stateCityResponseDto;
    }

    public MembershipDetailsDTO getCustomerHomeApplianceAssetDetail(String membershipId, String assetId) {

        MembershipDetailsDTO membershipDetails = null;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.getBaseUrl()).pathSegment("memberships", membershipId, "assets", assetId);

        ResponseEntity<CustomerHomeApplianceAssetResponse> httpResponse = this.callUrl(CustomerHomeApplianceAssetResponse.class, builder.toUriString(), HttpMethod.GET);

        if (httpResponse != null) {
            CustomerHomeApplianceAssetResponse customerHomeApplianceAssetResponse = httpResponse.getBody();
            membershipDetails = customerHomeApplianceAssetResponse.getData();
        }

        return membershipDetails;
    }

    public CustMemView getCustomerMembershipDetail(String membershipId) {

        CustMemView custMemView = null;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.getBaseUrl() + getCustomerMembershipDetail).queryParam("memId", membershipId);

        ResponseEntity<CustMemViewResponse> httpResponse = this.callUrl(CustMemViewResponse.class, builder.toUriString(), HttpMethod.GET);

        if (httpResponse != null) {
            CustMemViewResponse custMemViewResponse = httpResponse.getBody();
            custMemView = custMemViewResponse.getData();
        }

        return custMemView;
    }

    public String createAdvice(PaymentDto adviceDto) {
        String adviceId = null;
        HttpEntity<PaymentDto> entity = new HttpEntity<>(adviceDto, getHeaders());
        ResponseEntity<String> httpResponse = restTemplateConfiguration.restTemplate().exchange(oasysUrl + advices, HttpMethod.POST, entity, String.class);
        if (httpResponse != null) {
            String responseJson = httpResponse.getBody();
            logger.error(">>>>>>> Response of createAdvice:: " + responseJson);
            JSONObject jsonObject = new JSONObject(responseJson);
            String status = (String) jsonObject.get("status");
            logger.error(">>>>>>> Advice Id Status: " + status);
            if ("success".equalsIgnoreCase(status)) {
                JSONObject responseData = (JSONObject) jsonObject.get("data");
                Object advice = responseData.get("adviceId");
                logger.error(">>>>>>> Advice Id Created: " + advice);
                adviceId = String.valueOf(advice);
            }
        }
        return adviceId;
    }

    public String raiseClaim(CreateClaimDto createClaimDto) {
        String status = null;
        ResponseEntity<String> httpResponse;
        HttpEntity<CreateClaimDto> entity = new HttpEntity<>(createClaimDto, getHeaders());
        httpResponse = restTemplateConfiguration.restTemplate().exchange(oasysUrlPrivate + saveClaim, HttpMethod.POST, entity, String.class);
        if (httpResponse != null) {
            String responseJson = httpResponse.getBody();
            JSONObject jsonObject = new JSONObject(responseJson);
            status = (String) jsonObject.get("status");
            System.out.println("Raise claim status.." + status);
        }

        return status;
    }

    public String raiseClaimInCRM(CreateClaimDto createClaimDto) {
        String status = null;
        ResponseEntity<String> httpResponse;
        HttpEntity<CreateClaimDto> entity = new HttpEntity<>(createClaimDto, getHeaders());
        httpResponse = restTemplateConfiguration.restTemplate().exchange(oasysUrlPrivate + "raiseClaimCRM", HttpMethod.POST, entity, String.class);
        if (httpResponse != null) {
            String responseJson = httpResponse.getBody();
            JSONObject jsonObject = new JSONObject(responseJson);
            status = (String) jsonObject.get("status");
            System.out.println("Raise claim status1.." + status);
        }

        return status;
    }

    // Call is to close the SR in CRM via Portal.
    public String publishCloseRepairSR(String publishCloseRepairSR, CloseRepairSREventDto publishRepairSREventDto) {

        String status = null;
        publishRepairSREventDto.setEventName(ClaimLifecycleEvent.CLOSE_REPAIR_SR);
        HttpEntity<CloseRepairSREventDto> entity = new HttpEntity<>(publishRepairSREventDto, getHeaders());
        ResponseEntity<String> httpResponse = restTemplateConfiguration.restTemplate().exchange(oasysUrl + publishCloseRepairSR, HttpMethod.POST, entity, String.class);
        logger.error(">>>> in publishCloseRepairSR : httpResponse" + httpResponse);

        if (httpResponse != null) {
            String responseJson = httpResponse.getBody();
            JSONObject jsonObject = new JSONObject(responseJson);
            status = (String) jsonObject.get("status");
            logger.error(">>>> in publishCloseRepairSR :publishRepairSREventDto " + publishRepairSREventDto + "- status " + status);
        }

        return status;
    }

    public boolean sendSMS(SmsDto smsDto) {
        String status = null;
        boolean isSuccess = false;
        ResponseEntity<String> httpResponse;
        HttpEntity<SmsDto> entity = new HttpEntity<>(smsDto, getHeaders());
        httpResponse = restTemplateConfiguration.restTemplate().exchange(oasysUrl + smsCommunication, HttpMethod.POST, entity, String.class);
        if (httpResponse != null) {
            String responseJson = httpResponse.getBody();
            JSONObject jsonObject = new JSONObject(responseJson);
            status = (String) jsonObject.get("status");
            System.out.println("status.." + status);

            if (status.equals(Constants.SUCCESS)) {
                isSuccess = true;
            }
        }

        return isSuccess;
    }

    public boolean sendEmail(EmailDto emailDto) {
        String status = null;
        boolean isSuccess = false;
        ResponseEntity<String> httpResponse;
        HttpEntity<EmailDto> entity = new HttpEntity<>(emailDto, getHeaders());
        httpResponse = restTemplateConfiguration.restTemplate().exchange(oasysUrl + emailCommunication, HttpMethod.POST, entity, String.class);

        if (httpResponse != null) {
            String responseJson = httpResponse.getBody();
            JSONObject jsonObject = new JSONObject(responseJson);
            status = (String) jsonObject.get("status");
            System.out.println("status.." + status);

            if (status.equals(Constants.SUCCESS)) {
                isSuccess = true;
            }
        }

        return isSuccess;
    }

    public OASYSCustMemDetails getMembershipAssetDetails(String referenceIds, Long assetId, String category, boolean assetDocRequired) {

        UriComponentsBuilder builder = null;
        builder = UriComponentsBuilder.fromHttpUrl(oasysClaimBaseUrl + getMembershipDetails);
        if (referenceIds != null) {
            builder = builder.queryParam(MEM_ID, referenceIds);
        }
        builder = builder.queryParam(ASSET_REQUIRED, true).queryParam(ASSET_DOCUMENT_REQUIRED, assetDocRequired);
        if (assetId != null) {
            builder = builder.queryParam(ASSET_ID, assetId);
        }
        if (!StringUtils.isEmpty(category)) {
            builder = builder.queryParam(CATEGORY, category);
        }
        logger.error("Asset Details url:" + builder.toUriString());

        try {
            ResponseEntity<String> httpResponse = this.callUrl(String.class, builder.toUriString(), HttpMethod.GET);
            if (httpResponse != null && StringUtils.isNotBlank(httpResponse.getBody())) {
                Gson gson = new Gson();
                OASYSCustMemDetailsResponse membershipDetails = gson.fromJson(httpResponse.getBody(), OASYSCustMemDetailsResponse.class);
                return membershipDetails.getData();

            }

        } catch (Exception e) {
            logger.error("Exception occurred while getting Membership on Portal:" + referenceIds);
            e.printStackTrace();

        }
        return null;
    }

    public static void main(String args[]) {
        System.out.println(ServiceRequestStatus.COMPLETED.getStatus());
    }

}
