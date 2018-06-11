package com.oneassist.serviceplatform.commons.enums;

public enum DHLQualityCheckCodes {

    CHECK("CHECK"), GEN_ITEM_DESC_CHECK("GEN_ITEM_DESC_CHECK"), GEN_ITEM_IMEI_CHECK("GEN_ITEM_IMEI_CHECK");

    private final String checkCode;

    DHLQualityCheckCodes(String checkCode) {

        this.checkCode = checkCode;
    }

    public String getCheckCode() {

        return this.checkCode;
    }

    public static DHLQualityCheckCodes getQualityCheckCodes(String checkCode) {
        for (DHLQualityCheckCodes dhlQualityCheckCode : DHLQualityCheckCodes.values()) {
            if (dhlQualityCheckCode.getCheckCode().equals(checkCode)) {
                return dhlQualityCheckCode;
            }
        }

        return null;
    }
}