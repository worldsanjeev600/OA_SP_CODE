package com.oneassist.serviceplatform.commons.enums;

public enum PartnerPriority {
    ONE(1), TWO(2), THREE(3), FOUR(4);

    private final int priority;

    PartnerPriority(int priority) {

        this.priority = priority;
    }

    public int getPriority() {

        return this.priority;
    }

    public static PartnerPriority getPriority(int priority) {
        for (PartnerPriority prior : PartnerPriority.values()) {
            if (prior.getPriority() == priority) {
                return prior;
            }
        }
        return null;
    }
}
