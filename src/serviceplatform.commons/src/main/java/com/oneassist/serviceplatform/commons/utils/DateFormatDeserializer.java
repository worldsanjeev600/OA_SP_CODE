package com.oneassist.serviceplatform.commons.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.log4j.Logger;

public class DateFormatDeserializer extends JsonDeserializer<Date> {

    private static final Logger logger = Logger.getLogger(DateFormatDeserializer.class);

    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {

        String date = jsonparser.getText();
        Date formattedDate = null;
        if (date != null) {
            try {
                formattedDate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").parse(date);
            } catch (ParseException e) {
                try {
                    formattedDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(date);
                } catch (ParseException parseExc) {
                    try {
                        formattedDate = new SimpleDateFormat("dd-MMM-yyyy").parse(date);
                    } catch (ParseException ex) {
                        logger.error("Exception while parsing the date ::" + date);
                        try {
                            formattedDate = new Date(Long.parseLong(date));
                        } catch (Exception ex1) {
                            try {
                                formattedDate = new Date(date);
                            } catch (Exception ex2) {
                                logger.error("Exception while parsing :" + date);
                            }
                        }
                    }
                }
            }
        }

        return formattedDate;
    }
}
