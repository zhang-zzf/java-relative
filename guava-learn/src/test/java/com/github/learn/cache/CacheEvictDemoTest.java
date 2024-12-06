package com.github.learn.cache;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CacheEvictDemoTest {

    @InjectMocks
    CacheEvictDemo demo;

    @Test
    void givenLoadingCache_when_then() {
        demo.queryFromTimeBasedCache("1");
    }

}