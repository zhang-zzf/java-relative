package com.feng.learn.final_;

import static org.assertj.core.api.BDDAssertions.then;

import com.feng.learn.final_.Price.Price1;
import com.feng.learn.final_.Price.Price2;
import com.feng.learn.final_.Price.Price3;
import com.feng.learn.final_.Price.Price4;
import com.feng.learn.final_.Price.Price5;
import org.junit.jupiter.api.Test;

class PriceTest {


    @Test
    void givenPrice1_when_then() {
        then(Price1.INSTANCE.getCurrentPrice()).isEqualTo(17);
    }

    @Test
    void givenPrice2_when_then() {
        then(Price2.INSTANCE.getCurrentPrice()).isEqualTo(-3);
    }


    @Test
    void givenPrice3_when_then() {
        // 和预想中 -3 不一致
        // 宏替换的优先级更高
        then(Price3.INSTANCE.getCurrentPrice()).isEqualTo(-3);
    }

    @Test
    void givenPrice4_when_then() {
        then(Price4.INSTANCE.getCurrentPrice()).isEqualTo(-3);
    }

    @Test
    void givenPrice5_when_then() {
        then(Price5.INSTANCE.getCurrentPrice()).isEqualTo(17);
    }

}