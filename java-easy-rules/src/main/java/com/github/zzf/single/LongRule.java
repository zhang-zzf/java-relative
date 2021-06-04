package com.github.zzf.single;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.core.RuleProxy;

/**
 * @author zhanfeng.zhang
 * @date 2021/05/01
 */
@Rule
public class LongRule {

    public static final String ACTUAL = "actual";
    public static final String MIN = "min";
    public static final String MAX = "max";

    @Condition
    public boolean when(Facts facts) {
        Long actual = Long.valueOf(facts.get(ACTUAL).toString());
        Long min = Long.valueOf(facts.get(MIN).toString());
        Long max = Long.valueOf(facts.get(MAX).toString());
        return actual >= min && actual <= max;
    }

    @Action
    public void then(Facts facts) {
    }

    public static org.jeasy.rules.api.Rule newRule() {
        return RuleProxy.asRule(new LongRule());
    }

}
