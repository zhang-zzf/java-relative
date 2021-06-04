package com.github.zzf.rule;

import static org.assertj.core.api.Assertions.from;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.Map;
import org.jeasy.rules.api.Fact;
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
class RuleATest {


    @Test
    void testRuleA() {
        // rules
        RuleA ruleA = new RuleA();
        Rules rules = new Rules(ruleA);
        // data to check
        Facts facts = new Facts();
        facts.put("name", "A");
        // engine
        RulesEngine rulesEngine = new DefaultRulesEngine();
        Map<Rule, Boolean> check = rulesEngine.check(rules, facts);
        rulesEngine.fire(rules, facts);
        then(facts.getFact("nameEqualsA")).returns(true, from(Fact::getValue));
    }

}