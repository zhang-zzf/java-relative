package com.feng.learn.jdk8.lambda;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import org.junit.jupiter.api.Test;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/28
 */
public class FunctionTest {

    @Test
    void test() {
        Map<String, Long> map = new HashMap<>();
        // map.computeIfAbsent
        Long zzf = map.computeIfAbsent("zzf", (k) -> (long) k.length());
        then(zzf).isEqualTo(3);
        // Function compose
        Function<Integer, String> toString = Objects::toString;
        Function<String, String> quote = s -> "'" + s + "'";
        Function<Integer, String> compose = quote.compose(toString);
        then(compose.apply(5)).isEqualTo("'5'");
        // Function andThen
        Function<Integer, Boolean> numberGt999 = toString.andThen(string -> string.length() > 3);
        then(numberGt999.apply(999)).isFalse();
        //
        //
        IntFunction<Boolean> gt9 = i -> i > 9;
        // true -> 1 ; false -> 0
        ToIntFunction<Boolean> toIntFunction = b -> b ? 1 : 0;
    }

}
