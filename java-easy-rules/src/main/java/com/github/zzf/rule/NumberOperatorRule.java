package com.github.zzf.rule;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

/**
 * @author zhanfeng.zhang
 * @date 2021/05/01
 */
@Rule
public class NumberOperatorRule {

    @Condition
    public boolean when(@Fact("actualValue") Long actualValue,
        @Fact("operator") String op, @Fact("expectedValue") Long expectedValue) {
        switch (op) {
            case "==":
                return actualValue.equals(expectedValue);
            case ">=":
                return actualValue >= expectedValue;
            case ">":
                return actualValue > expectedValue;
            case "<":
                return actualValue < expectedValue;
            case "<=":
                return actualValue <= expectedValue;
            case "!=":
                return !actualValue.equals(expectedValue);
            default:
                return false;
        }
    }

    @Action
    public void then() {

    }
}
