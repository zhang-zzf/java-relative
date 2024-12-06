package com.github.learn;

import static org.assertj.core.api.BDDAssertions.then;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ApplicationTest {

    @Test
    public void givenOne_whenEqualToOne_thenSuccess() {
        then(1).isEqualTo(1);
    }

}
