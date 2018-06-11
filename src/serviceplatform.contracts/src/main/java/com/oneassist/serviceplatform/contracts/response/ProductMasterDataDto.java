package com.oneassist.serviceplatform.contracts.response;

import com.oneassist.serviceplatform.externalcontracts.ProductMasterDto;

public class ProductMasterDataDto {

    private String productCode;
    private ProductMasterDto productMasterDto;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public ProductMasterDto getProductMasterDto() {
        return productMasterDto;
    }

    public void setProductMasterDto(ProductMasterDto productMasterDto) {
        this.productMasterDto = productMasterDto;
    }

	@Override
	public String toString() {
		return "ProductMasterDataDto [productCode=" + productCode + ", productMasterDto=" + productMasterDto + "]";
	}
    
    
}
