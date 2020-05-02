package com.feng.learn.junit5;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/07
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class _01StartersUnitTest {

    @Test
    @Disabled
    void givenDisable_whenAssertEquals_thenShowUsage() {

    }

    @Test
    @Timeout(1)
    void givenTimeout_whenAssertEquals_thenShowUsage() throws InterruptedException {
        //Thread.sleep(1200);
    }

    @Test
    @Order(5)
    void givenAssertions_whenAssertEquals_thenShowUsage() {
        assertEquals(2, 1 + 1);
    }

    @Test
    @Order(-1)
    void givenAssertions_whenAssertAll_thenShowUsage() {
        assertAll(() -> {
        }, () -> {
        });
    }

    @RepeatedTest(5)
    @Order(0)
    void givenAssertions_whenAssertTrueOrFalse_thenShowUsage() {
        assertFalse(false);
        assertTrue(() -> true);
    }

    @Test
    @Order(1)
    void givenAssertions_whenAssertThrowsAndTimeout_thenShowUsage() {
        assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException();
        });

        assertTimeout(Duration.ofSeconds(1), () -> {
            Thread.sleep(10);
        });

        assertTimeoutPreemptively(Duration.ofMillis(500), () -> {
            Thread.sleep(3);
        });

    }

    @ParameterizedTest(name = "{0}+{1}={2}")
    @CsvSource({
        "1,1,2",
        "0,0,0",
        "0,1,1",
    })
    void givenParameterizedTest_whenTest_thenShowUsage(int first, int second, int expected) {
        assertEquals(expected, first + second,
            () -> first + " + " + second + " = " + expected);

    }

}
