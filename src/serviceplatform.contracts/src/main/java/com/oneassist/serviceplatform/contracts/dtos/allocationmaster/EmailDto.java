package com.oneassist.serviceplatform.contracts.dtos.allocationmaster;

import java.util.Map;
import com.oneassist.serviceplatform.externalcontracts.EmailVO;

public class EmailDto {

    private EmailVO emailVo;
    private Map<String, Object> modelEmail;

    public EmailVO getEmailVo() {
        return emailVo;
    }

    public void setEmailVo(EmailVO emailVo) {
        this.emailVo = emailVo;
    }

    public Map<String, Object> getModelEmail() {
        return modelEmail;
    }

    public void setModelEmail(Map<String, Object> modelEmail) {
        this.modelEmail = modelEmail;
    }

    @Override
    public String toString() {
        return "EmailDto [emailVo=" + emailVo + ", modelEmail=" + modelEmail + "]";
    }
}
