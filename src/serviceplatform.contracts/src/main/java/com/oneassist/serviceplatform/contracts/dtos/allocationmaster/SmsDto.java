package com.oneassist.serviceplatform.contracts.dtos.allocationmaster;

import java.util.Map;
import com.oneassist.serviceplatform.externalcontracts.SmsVO;

public class SmsDto {

    private SmsVO smsVo;
    private Map<String, Object> modelSms;

    public SmsVO getSmsVo() {
        return smsVo;
    }

    public void setSmsVo(SmsVO smsVo) {
        this.smsVo = smsVo;
    }

    public Map<String, Object> getModelSms() {
        return modelSms;
    }

    public void setModelSms(Map<String, Object> modelSms) {
        this.modelSms = modelSms;
    }

    @Override
    public String toString() {
        return "SmsDto [smsVo=" + smsVo + ", modelSms=" + modelSms + "]";
    }

}
