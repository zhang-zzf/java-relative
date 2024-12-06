package com.github.learn.mockito;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class Junit5 {

    @Mock
    List list;

    @Test
    void testJunit5WithMockito() {
        given(list.size()).willReturn(5);
        then(list.size()).isEqualTo(5);
    }


}
