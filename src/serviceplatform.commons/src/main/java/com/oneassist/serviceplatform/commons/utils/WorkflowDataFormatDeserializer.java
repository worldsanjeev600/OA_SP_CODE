package com.oneassist.serviceplatform.commons.utils;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import org.apache.log4j.Logger;

public class WorkflowDataFormatDeserializer extends JsonDeserializer<WorkflowData> {

    private static final Logger logger = Logger.getLogger(WorkflowDataFormatDeserializer.class);

    @Override
    public WorkflowData deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {

        String worfkFlowDataString = jsonparser.getText();
        WorkflowData workflowData = null;
        if (worfkFlowDataString != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                workflowData = objectMapper.readValue(worfkFlowDataString, WorkflowData.class);
            } catch (Exception e) {
                logger.error("Exception while parsing :" + worfkFlowDataString, e);
            }
        }

        return workflowData;
    }

}
