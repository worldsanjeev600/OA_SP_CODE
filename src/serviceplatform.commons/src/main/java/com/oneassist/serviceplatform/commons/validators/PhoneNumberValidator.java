package com.oneassist.serviceplatform.commons.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator {

    /**
     * Validate ohone no
     * 
     * @param phoneNo
     * @return
     */
    public static final boolean isValidPhoneNo(String phoneNo) {

        try {

            // phone no contain only "+" or "0-9" or "-" and length should be 5 to 17
            String regex = "^\\+?[0-9. ()-]{5,17}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(phoneNo);

            if (matcher.matches()) {
                return true;
            }
        } catch (Exception ex) {

            return false;
        }
        return false;
    }

}
