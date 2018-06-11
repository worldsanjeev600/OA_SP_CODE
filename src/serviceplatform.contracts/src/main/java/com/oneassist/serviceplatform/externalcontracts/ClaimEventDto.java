package com.oneassist.serviceplatform.externalcontracts;

import java.io.Serializable;

public class ClaimEventDto extends BaseEventDto implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1452922384739854206L;
    private Long claimSrNo;

    public Long getClaimSrNo() {

        return claimSrNo;
    }

    public void setClaimSrNo(Long claimSrNo) {

        this.claimSrNo = claimSrNo;
    }

	@Override
	public String toString() {
		return "ClaimEventDto [claimSrNo=" + claimSrNo + "]";
	}
    
}
