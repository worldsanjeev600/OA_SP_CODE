package com.oneassist.serviceplatform.contracts.dtos.allocationmaster;

import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;

public class PincodeMasterDataDto {

    private String pincode;
    private PincodeMasterDto pincodeMaster;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public PincodeMasterDto getPincodeMaster() {
        return pincodeMaster;
    }

    public void setPincodeMaster(PincodeMasterDto pincodeMaster) {
        this.pincodeMaster = pincodeMaster;
    }

	@Override
	public String toString() {
		return "PincodeMasterDataDto [pincode=" + pincode + ", pincodeMaster=" + pincodeMaster + "]";
	}
    
}
