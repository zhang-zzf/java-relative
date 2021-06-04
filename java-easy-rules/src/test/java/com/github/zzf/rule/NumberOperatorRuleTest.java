package com.github.zzf.rule;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Map;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.junit.jupiter.api.Test;

/**
 * @author zhanfeng.zhang
 * @date 2021/05/01
 */
class NumberOperatorRuleTest {


    @Test
    void testNumberOperationRule() {
        // rules
        NumberOperatorRule ruleA = new NumberOperatorRule();
        Rules rules = new Rules(ruleA);
        // data to check
        Facts facts = new Facts();
        facts.put("actualValue", 10L);
        facts.put("operator", ">");
        facts.put("expectedValue", 9L);
        // engine
        RulesEngine rulesEngine = new DefaultRulesEngine();
        Map<Rule, Boolean> check = rulesEngine.check(rules, facts);
        then(check).allSatisfy((rule, result) -> Boolean.TRUE.equals(result));
    }

}