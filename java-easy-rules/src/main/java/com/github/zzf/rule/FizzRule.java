package com.github.zzf.rule;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

/**
 * @author zhanfeng.zhang
 * @date 2021/05/01
 */
@Rule
public class FizzRule {

    @Condition
    public boolean isFizz(@Fact("number") Integer number) {
        return number % 5 == 0;
    }

    @Action
    public void printFizz() {
    }

    @Priority
    public int getPriority() {
        return 1;
    }

}
