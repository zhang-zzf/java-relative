package com.feng.learn.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import com.feng.learn.dao.UserDao;
import com.feng.learn.dao.model.User;
import com.feng.learn.exception.ServiceException;
import com.feng.learn.rpc.ExternalService;
import com.feng.learn.service.ServiceA;
import com.feng.learn.service.impl.ServiceAImplTest.MockContextConfig;
import com.feng.learn.spring.config.dbconfig.test_db.DBConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    ServiceAImpl.class,
    MockContextConfig.class,
    DBConfig.class,

})
public class ServiceAImplTest {

  @Autowired
  ExternalService externalService;
  @Autowired
  ServiceA serviceA;
  @Autowired
  UserDao userDao;

  @Ignore
  @Test(expected = ServiceException.class)
  public void testUserNotExist() throws ServiceException {
    when(externalService.service(anyLong())).thenReturn("1");
    // mock static
    int r = serviceA.service(1);
    assertEquals(28 + 1 + 0, r);
  }

  // 注意：这里不但要mock System.class, 还要mock 使用System 的ServiceAImpl
  @Transactional
  @Test
  public void serviceSuccessReturn() throws ServiceException {

    User u = User.builder().id(1).age(28).name("Java").build();
    // 测试保存
    long userId = serviceA.createUser(u.getName(), u.getAge());
    // mock 外部调用
    when(externalService.service(anyLong())).thenReturn("1");
    // mock static
    int r = serviceA.service((int) userId);
    assertEquals(28 + 1 + 0, r);
  }

  @Repeat(3)
  @Transactional
  @Test
  public void saveUser() {
    User target = User.builder().name("Java").age(28).build();
    long id = serviceA.createUser(target.getName(), target.getAge());
    target.setId(id);
    User dbData = userDao.getById(id);
    assertEquals(target, dbData);
  }

  @Configuration
  public static class MockContextConfig {

    @Bean
    public ExternalService externalServiceMock() {
      return Mockito.mock(ExternalService.class);
    }
  }

}