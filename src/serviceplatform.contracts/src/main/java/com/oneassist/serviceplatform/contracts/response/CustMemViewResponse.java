package com.oneassist.serviceplatform.contracts.response;

import com.oneassist.serviceplatform.externalcontracts.CustMemView;

public class CustMemViewResponse {

    private CustMemView data;

    public CustMemView getData() {
        return data;
    }

    public void setData(CustMemView data) {
        this.data = data;
    }
}
