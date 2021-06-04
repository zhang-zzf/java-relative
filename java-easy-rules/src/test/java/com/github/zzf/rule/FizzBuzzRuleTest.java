package com.github.zzf.rule;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;
import org.jeasy.rules.support.composite.UnitRuleGroup;
import org.junit.jupiter.api.Test;

/**
 * @author zhanfeng.zhang
 * @date 2021/05/01
 */
@Slf4j
class FizzBuzzRuleTest {

    @Test
    void testFizzBuzz() {
        // rules
        FizzRule fizzRule = new FizzRule();
        BuzzRule buzzRule = new BuzzRule();
        UnitRuleGroup fizzBuzzRule = new UnitRuleGroup("fizzBuzz", "fizzAndBuzz", 3);
        fizzBuzzRule.addRule(fizzRule);
        fizzBuzzRule.addRule(buzzRule);
        Rules rules = new Rules(fizzBuzzRule, fizzRule, buzzRule);
        // engine
        RulesEngine rulesEngine = new DefaultRulesEngine();
        Facts facts = new Facts();
        for (int i = 0; i < 100; i++) {
            facts.put("number", i);
            Map<Rule, Boolean> check = rulesEngine.check(rules, facts);
            log.info("{} -> {}", i, check.get(fizzBuzzRule) ? "fizzbuzz"
                : check.get(fizzRule) ? "fizz"
                    : check.get(buzzRule) ? "buzz" : "");
        }
    }

}