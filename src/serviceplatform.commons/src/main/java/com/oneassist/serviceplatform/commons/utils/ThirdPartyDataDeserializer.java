package com.oneassist.serviceplatform.commons.utils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

public class ThirdPartyDataDeserializer extends JsonDeserializer<Map<String,Object>> {

    private static final Logger logger = Logger.getLogger(ThirdPartyDataDeserializer.class);

    @Override
    public Map<String,Object> deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {

        String thirdPartyProperties = jsonparser.getText();
        Map<String,Object> thirdPartyData = null;
        if (!Strings.isNullOrEmpty(thirdPartyProperties)) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                thirdPartyData = objectMapper.readValue(thirdPartyProperties, new TypeReference<Map<String, Object>>(){});
            } catch (Exception e) {
                logger.error("Exception while parsing :" + thirdPartyProperties, e);
            }
        }

        return thirdPartyData;
    }

}
