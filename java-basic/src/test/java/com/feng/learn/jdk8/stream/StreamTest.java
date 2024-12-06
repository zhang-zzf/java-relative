package com.feng.learn.jdk8.stream;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-06
 */
public class StreamTest {


    /**
     * Collection stream 测试
     */
    @Test
    void givenCollection_whenStream_then() {
        Collection<Integer> integers = new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(3);
            add(4);
            add(5);
        }};
        Stream<Integer> stream = integers.stream();
        Set<Integer> result = stream.limit(2)
            .map(x -> x * 2)
            .collect(toSet());
        then(result.size()).isEqualTo(2);
    }
}
