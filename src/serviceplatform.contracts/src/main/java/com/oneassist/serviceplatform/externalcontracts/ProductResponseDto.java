package com.oneassist.serviceplatform.externalcontracts;

import java.io.Serializable;
import java.util.Map;

public class ProductResponseDto implements Serializable {

    private static final long serialVersionUID = 7632893845059525401L;
    PlanMasterDto plan;

    /**
     * @return the plan
     */
    public PlanMasterDto getPlan() {
        return plan;
    }

    /**
     * @param plan
     *            the plan to set
     */
    public void setPlan(PlanMasterDto plan) {
        this.plan = plan;
    }

    Map<String, ProductMasterDto> productsAssosiatedWithPlan;

    /**
     * @return the productsForPlanRequestDetailResponse
     */
    public Map<String, ProductMasterDto> getProductsForPlanRequestDetailResponse() {
        return productsAssosiatedWithPlan;
    }

    /**
     * @param productsForPlanRequestDetailResponse
     *            the productsForPlanRequestDetailResponse to set
     */
    public void setProductsForPlanRequestDetailResponse(Map<String, ProductMasterDto> productsAssosiatedWithPlan) {
        this.productsAssosiatedWithPlan = productsAssosiatedWithPlan;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProductResponseDto [plan=");
		builder.append(plan);
		builder.append(", productsAssosiatedWithPlan=");
		builder.append(productsAssosiatedWithPlan);
		builder.append("]");
		return builder.toString();
	}
    
}
