package com.oneassist;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

import com.oneassist.enums.RuleName;

public class RuleEngine {

    public static void execute(RuleName ruleName, Object serviceRequestRuleDto){
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();

        StatelessKieSession kSession = kContainer.newStatelessKieSession(ruleName.getRuleName());

        kSession.execute(serviceRequestRuleDto);
    }
}
