package com.feng.learn.mock;

import com.feng.learn.dao.model.User;
import com.feng.learn.service.impl.ServiceAImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author zhanfeng.zhang
 * @date 2019/10/22
 */

@RunWith(PowerMockRunner.class)
public class PowerMockUnitTest {

  /**
   * mock 静态方法
   */
  @Test
  @PrepareForTest({System.class, User.class, ServiceAImpl.class})
  public void testMockStaticTest() {
    final String key = "url";
    final String value = "http://www.baidu.com";
    // Enable static mocking for all methods of a class
    PowerMockito.mockStatic(System.class);
    PowerMockito.when(System.getProperty(key)).thenReturn(value);

    Assert.assertEquals(value, System.getProperty("url"));
  }

  /**
   * mock 构造器
   */
  @PrepareForTest(User.class)
  @Test
  public void testMockConstructorTest() throws Exception {
    User u = User.builder().id(-1).name("mock").age(-2).build();
    PowerMockito.whenNew(User.class).withNoArguments().thenReturn(u);
    Assert.assertEquals(u, new User());
  }

  /**
   * 其他mock 请自行搜索
   */

}
