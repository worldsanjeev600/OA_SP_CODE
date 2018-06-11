package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.io.Serializable;
import com.oneassist.serviceplatform.externalcontracts.ClaimEventDto;

public class CloseRepairSREventDto extends ClaimEventDto implements Serializable {

    private static final long serialVersionUID = -8112555782931488412L;

    private Long srNo;

    private String srStatus;

    public Long getSrNo() {
        return srNo;
    }

    public void setSrNo(Long srNo) {
        this.srNo = srNo;
    }

    public String getSrStatus() {
        return srStatus;
    }

    public void setSrStatus(String srStatus) {
        this.srStatus = srStatus;
    }

    @Override
    public String toString() {
        return "CloseRepairSREventDto [srNo=" + srNo + ", srStatus=" + srStatus + "]";
    }
}
