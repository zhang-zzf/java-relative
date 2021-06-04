package com.github.zzf.rule;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

/**
 * @author zhanfeng.zhang
 * @date 2021/05/01
 */
@Rule(name = "ruleA")
public class RuleA {

    @Condition
    public boolean when(@Fact("name") String fact) {
        return "A".equals(fact);
    }

    @Action(order = 1)
    public void then(Facts facts) {
        facts.put("nameEqualsA", true);
    }

    @Action(order = 2)
    public void finallyAct(Facts facts) {
        facts.put("nameEqualsATag", true);
    }

}
