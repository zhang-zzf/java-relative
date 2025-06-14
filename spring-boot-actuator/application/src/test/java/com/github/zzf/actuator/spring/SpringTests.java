package com.github.zzf.actuator.spring;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.StandardEnvironment;

import static org.assertj.core.api.BDDAssertions.then;

public class SpringTests {


    @Test
    void givenSpringEnvironment_when_then() {
        StandardEnvironment env = new StandardEnvironment();
        then(env.getActiveProfiles()).hasSize(0);
        then(env.getDefaultProfiles()).hasSize(0);
        then(env.getProperty("java_home")).isNotEmpty();
    }
}
