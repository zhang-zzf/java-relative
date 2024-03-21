package com.github.learn.mockito;


import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class Junit4 {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    List list;

    @Test
    public void testJunit4WithMockito() {
        given(list.size()).willReturn(5);
        then(list.size()).isEqualTo(5);
    }


}
