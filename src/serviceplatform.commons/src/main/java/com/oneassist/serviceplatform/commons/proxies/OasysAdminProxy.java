package com.oneassist.serviceplatform.commons.proxies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneassist.serviceplatform.commons.cache.base.RestTemplateConfiguration;
import com.oneassist.serviceplatform.commons.proxies.base.BaseProxy;
import com.oneassist.serviceplatform.contracts.dtos.CmsDocumentDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CreateClaimDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.EditClaimDetailDto;
import com.oneassist.serviceplatform.contracts.response.UserProfileData;
import com.oneassist.serviceplatform.contracts.response.UserProfileResponseDto;
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
public class OasysAdminProxy extends BaseProxy {

    private final Logger logger = Logger.getLogger(OasysAdminProxy.class);

    @Value("${OASYSAdminUrl}")
    private String oasysAdminUrl;

    @Value("${oasysAdminUsername}")
    private String oasysAdminUsername;

    @Value("${oasysAdminPassword}")
    private String oasysAdminPassword;

    @Value("${getUsers}")
    private String getUsersApiUrl;

    @Value("${saveClaim}")
    private String saveClaimUrl;

    @Value("${saveOrUpdateClaimDocumentUrl}")
    private String saveOrUpdateClaimDocumentUrl;

    @Value("${updateClaimUrl}")
    private String updateClaimUrl;

    @Value("${claimEligibilityUrl}")
    private String claimEligibilityUrl;

    @Autowired
    private RestTemplateConfiguration restTemplateConfiguration;

    @Override
    protected HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("userName", oasysAdminUsername);
        headers.set("password", oasysAdminPassword);

        return headers;
    }

    @Override
    protected String getBaseUrl() {
        return oasysAdminUrl;
    }

    public UserProfileData getTechnicianUserProfile(String userId) {

        UserProfileData userProfileData = null;
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.getBaseUrl() + getUsersApiUrl).path(userId);
            ResponseEntity<UserProfileResponseDto> httpResponse = this.callUrl(UserProfileResponseDto.class, builder.toUriString(), HttpMethod.GET);

            if (httpResponse != null) {
                UserProfileResponseDto userProfileResponseDto = httpResponse.getBody();
                userProfileData = userProfileResponseDto.getData();
            }
        } catch (Exception e) {
            logger.error("Not able to fetch technician profile details from ADMIN server", e);
        }

        return userProfileData;
    }

    public String raiseClaim(CreateClaimDto createClaimDto) throws JsonProcessingException {
        String responseJson = null;
        ResponseEntity<String> httpResponse;
        HttpEntity<CreateClaimDto> entity = new HttpEntity<>(createClaimDto, getHeaders());
        logger.error("Save claim url :" + this.getBaseUrl() + saveClaimUrl);
        logger.error("Create claim request ::" + new ObjectMapper().writeValueAsString(createClaimDto));
        httpResponse = restTemplateConfiguration.restTemplate().exchange(this.getBaseUrl() + saveClaimUrl, HttpMethod.POST, entity, String.class);
        if (httpResponse != null) {
            responseJson = httpResponse.getBody();
            logger.error("Raise claim .." + responseJson);
        }

        return responseJson;
    }

    public String sendDocumentDetailsToCMS(String claimPk, String storageRefId, CmsDocumentDto cmsDocumentDto) throws Exception {

        String status = null;
        ResponseEntity<String> httpResponse;

        HttpEntity<CmsDocumentDto> entity = new HttpEntity<>(cmsDocumentDto, getHeaders());
        httpResponse = restTemplateConfiguration.restTemplate().exchange(this.getBaseUrl() + saveOrUpdateClaimDocumentUrl.replace("@claimPk", claimPk).replace("@mongoRefId", storageRefId),
                HttpMethod.POST, entity, String.class);

        if (httpResponse != null) {
            logger.info("Response for send document details to CMS: " + httpResponse);
            String responseJson = httpResponse.getBody();
            JSONObject jsonObject = new JSONObject(responseJson);
            status = (String) jsonObject.get("status");
            logger.info("Response Status for send document details to CMS.." + status);
        }

        return status;
    }

    public String updateClaim(EditClaimDetailDto editClaimDetailDto) throws Exception {
        String status = null;
        ResponseEntity<String> httpResponse;
        HttpEntity<EditClaimDetailDto> entity = new HttpEntity<>(editClaimDetailDto, getHeaders());
        logger.error("Save claim url :" + this.getBaseUrl() + updateClaimUrl);
        logger.error("Update claim request ::" + new ObjectMapper().writeValueAsString(editClaimDetailDto));
        httpResponse = restTemplateConfiguration.restTemplate().exchange(this.getBaseUrl() + updateClaimUrl, HttpMethod.PUT, entity, String.class);
        if (httpResponse != null) {
            logger.info("Response for send document details to CMS: " + httpResponse);
            String responseJson = httpResponse.getBody();
            JSONObject jsonObject = new JSONObject(responseJson);
            status = (String) jsonObject.get("status");
            logger.info("Response Status for update claim details to CMS.." + status);
        }
        return status;
    }

    public String checkClaimEligibility(String referenceNo, String assetId) {
        String responseJson = null;
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.getBaseUrl() + claimEligibilityUrl).path(referenceNo).path("/").path(assetId);
            ResponseEntity<String> httpResponse = this.callUrl(String.class, builder.toUriString(), HttpMethod.GET);

            if (httpResponse != null) {
                logger.info("Response for get eligibility details from CMS: " + httpResponse);
                responseJson = httpResponse.getBody();
            }
        } catch (Exception e) {
            logger.error("Exception while checking claim eligibility for " + referenceNo + " and " + assetId);
        }

        return responseJson;
    }

}
