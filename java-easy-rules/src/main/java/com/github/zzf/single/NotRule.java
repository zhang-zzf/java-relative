package com.github.zzf.single;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.core.BasicRule;

/**
 * @author zhanfeng.zhang
 * @date 2021/05/01
 */
public class NotRule extends BasicRule {

    private final Rule wrapper;

    public NotRule(Rule rule) {
        this.wrapper = rule;
    }

    @Override
    public boolean evaluate(Facts facts) {
        return !wrapper.evaluate(facts);
    }

    public static Rule newRule(Rule rule) {
        return new NotRule(rule);
    }

}
