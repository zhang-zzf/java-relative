package com.feng.learn.spring;

import com.feng.learn.spring.config.NoneContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {NoneContext.class})
public class SpringTest {

  @Repeat(3) // 需要依赖spring context
  @Test
  public void testRepeat() {
    System.out.println("testRepeat");
  }

}
