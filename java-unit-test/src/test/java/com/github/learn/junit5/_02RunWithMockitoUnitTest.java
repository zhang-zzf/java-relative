package com.github.learn.junit5;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;

import com.github.learn.AbstractJUnit5Mockito;
import com.github.learn.mockito.bdd.PhoneBookRepository;
import com.github.learn.mockito.bdd.PhoneBookService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * @author zhanfeng.zhang
 * @date 2019/12/28
 */
public class _02RunWithMockitoUnitTest extends AbstractJUnit5Mockito {

  @Mock
  PhoneBookRepository phoneBookRepository;
  @InjectMocks
  PhoneBookService phoneBookService;
  String momContactName = "Mom";
  String momPhoneNumber = "01234";
  String xContactName = "x";
  String tooLongPhoneNumber = "01111111111111";

  @Before
  public void junit4Before() {

  }

  @BeforeEach
  void junit5BeforeEach() {

  }

  @Test
  public void givenValidContactName_whenSearchInPhoneBook_thenReturnPhoneNum() {
    // given
    given(phoneBookRepository.contains(momContactName)).willReturn(true);
    given(phoneBookRepository.getPhoneNumberByContactName(anyString()))
        .will(
            invocation -> momContactName.equals(invocation.getArgument(0)) ? momPhoneNumber : null);

    // when
    String number = phoneBookService.search(momContactName);

    // then
    // verify
    BDDMockito.then(phoneBookRepository).should(atLeastOnce()).contains(momContactName);
    BDDMockito.then(phoneBookRepository).should(atMost(1))
        .getPhoneNumberByContactName(momContactName);
    Assert.assertThat(number, is(momPhoneNumber));
  }

}
