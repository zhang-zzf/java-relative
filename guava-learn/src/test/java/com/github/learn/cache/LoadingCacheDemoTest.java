package com.github.learn.cache;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoadingCacheDemoTest {

  @Spy
  DataSource dataSource;

  @InjectMocks
  LoadingCacheDemo demo;

  @Test
  void givenLoadingCache_when_then() {
    demo.getByStringKey("1");
    demo.getByStringKey("1");
    // verify
    // 仅溯源 1 次
    BDDMockito.then(dataSource).should(times(1)).queryBy("1");
  }

}