package com.github.learn.mockito.bdd;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.atLeastOnce;
import static org.mockito.BDDMockito.atMost;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.only;
import static org.mockito.BDDMockito.then;

import com.github.learn.AbstractJUnit4Mockito;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/06
 */
public class BDDMockitoTest extends AbstractJUnit4Mockito {

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
    given(phoneBookRepository.contains(momContactName)).willReturn(true);
    /*
    given(phoneBookRepository.getPhoneNumberByContactName(momContactName))
      .willReturn(momPhoneNumber);
      */
    given(phoneBookRepository.getPhoneNumberByContactName(anyString()))
        .will(
            invocation -> momContactName.equals(invocation.getArgument(0)) ? momPhoneNumber : null);
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
