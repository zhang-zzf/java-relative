package com.feng.learn.junit5;

import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;

import com.feng.learn.dao.UserDao;
import com.feng.learn.dao.model.User;
import com.feng.learn.mock.bdd.PhoneBookRepository;
import com.feng.learn.mock.bdd.PhoneBookService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author zhanfeng.zhang
 * @date 2019/12/28
 */
@ExtendWith(MockitoExtension.class)
@RunWith(PowerMockRunner.class)
public class _02RunWithMockitoUnitTest {

    @Before
    public void junit4Before() {

    }

    @BeforeEach
    void junit5BeforeEach() {

    }

    @Mock
    PhoneBookRepository phoneBookRepository;
    @InjectMocks
    PhoneBookService phoneBookService;

    String momContactName = "Mom";
    String momPhoneNumber = "01234";
    String xContactName = "x";
    String tooLongPhoneNumber = "01111111111111";

    @Test
    public void givenValidContactName_whenSearchInPhoneBook_thenReturnPhoneNum() {
        // given
        given(phoneBookRepository.contains(momContactName)).willReturn(true);
        given(phoneBookRepository.getPhoneNumberByContactName(anyString()))
            .will(invocation -> momContactName.equals(invocation.getArgument(0)) ? momPhoneNumber : null);

        // when
        String number = phoneBookService.search(momContactName);

        // then
        // verify
        BDDMockito.then(phoneBookRepository).should(atLeastOnce()).contains(momContactName);
        BDDMockito.then(phoneBookRepository).should(atMost(1)).getPhoneNumberByContactName(momContactName);
        Assert.assertThat(number, is(momPhoneNumber));
    }

    @Test
    void givenJunit5Parameter_whenTest_thenDemo(@Mock UserDao userDaoMock) {
        User u = User.builder().build();
        given(userDaoMock.getById(anyLong())).willReturn(u);

        User byId = userDaoMock.getById(1L);

        then(byId).isSameAs(u);

    }


    @org.junit.Test
    @PrepareForTest({System.class})
    public void givenJunit5_whenUsingJunit4_thenDemo() {
        final String str = "PowerMock";
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getProperty(anyString())).thenReturn(str);
        String s = System.getProperty("any String");
        then(s).isSameAs(str);
    }
}
