package com.github.learn.springframework.resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ResourceLoaderInjectTest {

    @Autowired
    ResourceLoaderInject resourceBean;


    @Test
    void given_when_then() {
        resourceBean.loadApplicationYaml();
    }

}