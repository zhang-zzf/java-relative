package com.github.zzf.single;

import java.util.List;
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
public class InRule {

    public static final String ACTUAL = "actual";
    public static final String EXPECT = "expect";

    @Condition
    public boolean when(Facts facts) {
        final String actual = facts.get(ACTUAL);
        final List<String> expect = facts.get(EXPECT);
        return expect.contains(actual);
    }

    @Action
    public void then(Facts facts) {
    }

    public static org.jeasy.rules.api.Rule newRule() {
        return RuleProxy.asRule(new InRule());
    }

}
