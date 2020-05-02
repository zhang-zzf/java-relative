package com.feng.learn.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.feng.learn.dao.UserDao;
import com.feng.learn.dao.model.User;
import com.feng.learn.exception.ServiceException;
import com.feng.learn.rpc.ExternalService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

// @RunWith(MockitoJUnitRunner.class)
@RunWith(PowerMockRunner.class)
public class ServiceAImplUnitTest {

    @Mock
    UserDao userDao;
    @Mock
    ExternalService externalService;
    // 把上面的2个mock 注入到serviceAImpl中
    @InjectMocks
    ServiceAImpl serviceAImpl;


    @Test(expected = ServiceException.class)
    public void serviceUserNotExist() throws ServiceException {
        // mock
        when(userDao.getById(-1)).thenReturn(null);
        int r = serviceAImpl.service(-1);
    }

    // 注意：这里不但要mock System.class, 还要mock 使用System 的ServiceAImpl
    @PrepareForTest({System.class, ServiceAImpl.class})
    @Test
    public void serviceSuccessReturn() throws ServiceException {

        User u = User.builder().id(1).age(28).name("Java").build();
        // mock
        when(userDao.getById(1)).thenReturn(u);
        when(externalService.service(1)).thenReturn("1");
        // mock static
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getProperty("system_age", "0")).thenReturn("2");
        //String system_age = System.getProperty("system_age");

        int r = serviceAImpl.service(1);
        assertEquals(28 + 1 + 2, r);
    }

    @Test
    public void saveUser() {
        serviceAImpl.createUser("Java", 28);
        verify(userDao).createUser(User.builder().name("Java").age(28).build());
    }
}