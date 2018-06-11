package com.oneassist.enums;

public enum RuleName {
	CRITERIA_RULE(1, "criteria-rule"),
	PARTNER_PRODUCTCODE_SLA_RULE(2, "partnerproductcodesla-rules"),
	PRODUCTCODE_SLA_RULE(3, "productcodesla-rules"),
	HUB_ALLOCATION_RULE(4, "huballocation-rules"),
	PARTNER_SLA_RULE(5, "partnersla-rules");
	
	private final int		ruleId;
	private final String	ruleName;

	RuleName(int ruleId, String ruleName) {

		this.ruleId = ruleId;
		this.ruleName = ruleName;		
	}

	public int getRuleId() {

		return this.ruleId;
	}
	
	public String getRuleName() {

		return this.ruleName;
	}
}