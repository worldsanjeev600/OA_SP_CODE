package com.oneassist.serviceplatform.commons.enums;

public enum InitiatingSystem {

	PORTAL(1),
	OPS(2),
	CRM(3),
	BATCH(4),
	BUSINESS_PARTNER(5),
	SERVICE_PLATFORM(6),
	MOBILE_ANDROID(7),
	MOBILE_IOS(8);

	private final int initiatingSystem;

	InitiatingSystem(int initiatingSystem) {
		this.initiatingSystem = initiatingSystem;
	}

	public int getInitiatingSystem() {
		return this.initiatingSystem;
	}

	public static InitiatingSystem getInitiatingSystem(int initiatingSystem) {
		for (InitiatingSystem enumInitiatingSystem : InitiatingSystem.values()) {
			if (enumInitiatingSystem.getInitiatingSystem() == initiatingSystem) {
				return enumInitiatingSystem;
			}
		}
		return null;
	}
}
