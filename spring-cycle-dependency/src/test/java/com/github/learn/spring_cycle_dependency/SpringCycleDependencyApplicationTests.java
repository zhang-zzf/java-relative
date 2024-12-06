package com.github.learn.spring_cycle_dependency;

import com.github.learn.spring_cycle_dependency.domain.ServiceA;
import com.github.learn.spring_cycle_dependency.domain.ServiceB;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringCycleDependencyApplicationTests {

    @Autowired
    ServiceA serviceA;

    @Autowired
    ServiceB serviceB;

    @Test
    void contextLoads() {
        serviceA.methodB();
        serviceB.methodA();
    }

}
