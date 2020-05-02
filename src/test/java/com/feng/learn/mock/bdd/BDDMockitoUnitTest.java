package com.feng.learn.mock.bdd;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.BDDMockito.*;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/06
 */
//@RunWith(MockitoJUnitRunner.class)
public class BDDMockitoUnitTest {

    // better than @RunWith(MockitoJUnitRunner.class)
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

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
        // stub
        given(phoneBookRepository.contains(momContactName))
            .willReturn(true);
    /*
    given(phoneBookRepository.getPhoneNumberByContactName(momContactName))
      .willReturn(momPhoneNumber);
      */
        given(phoneBookRepository.getPhoneNumberByContactName(anyString()))
            .will(invocation -> momContactName.equals(invocation.getArgument(0)) ? momPhoneNumber : null);

        // when
        String number = phoneBookService.search(momContactName);

        // then
        // verify
        then(phoneBookRepository).should(atLeastOnce()).contains(momContactName);
        then(phoneBookRepository).should(atMost(1)).getPhoneNumberByContactName(momContactName);
        Assert.assertThat(number, is(momPhoneNumber));
    }

    /**
     * 2个verify相当于2个断言
     */
    @Test
    public void givenInvalidContactName_whenSearch_thenReturnNull() {
        // stub
        given(phoneBookRepository.contains(xContactName)).willReturn(false);

        // when
        String number = phoneBookService.search(xContactName);

        // then
        // verify == assert
        then(phoneBookRepository).should(only()).contains(xContactName);
        then(phoneBookRepository).should(never()).getPhoneNumberByContactName(xContactName);
        Assert.assertThat(number, is(nullValue()));
    }


}
