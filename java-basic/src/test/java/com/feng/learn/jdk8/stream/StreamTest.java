package com.feng.learn.jdk8.stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.BDDAssertions.then;

import com.feng.learn.jdk8.stream.Person.Sex;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-06
 */
public class StreamTest {

    final List<Person> personList = new ArrayList<>();

    // todo Collectors.mapping / Collectors.reduce


    /**
     * <pre>
     *      Stream reduce
     *      1. java.util.stream.Stream#reduce(T, java.util.function.BinaryOperator<T>) 优先使用此方法
     * </pre>
     *
     */
    @Test
    void givenStream_whenReduce_then() {
        int sum = personList.stream().map(Person::getAge)
            // reduce with init value
            .reduce(0, Integer::sum);
        then(sum).isEqualTo(0);
        //
        Optional<Integer> reduceWithoutInitValue = personList.stream().map(Person::getAge)
            .reduce(Integer::sum);
        //
        BigDecimal sumWithBigDecimal = personList.stream().map(Person::getAge)
            // identity 初始值
            // accumulator 叠加器，表示 BigDecimal 如何叠加 int
            // combiner Stream是支持并发操作的，为了避免竞争，对于reduce线程都会有独立的result，combiner的作用在于合并每个线程的result得到最终结果
            .reduce(BigDecimal.ZERO, (a, b) -> a.add(new BigDecimal(b)), BigDecimal::add);
        // 等同与下面的 code, 优先使用下面的 code
        sumWithBigDecimal = personList.stream().map(Person::getAge)
            .map(BigDecimal::new)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Test
    void givenStream_whenMap_then() {
        //
        IntStream stream = personList.stream()
            // map 需要 Function，new Function。
            // 后面的几种写法全是语法糖。
            // .map(new Function<Person, Integer>() {
            //     @Override
            //     public Integer apply(Person person) {
            //         return person.getAge();
            //     }
            // })
            // .mapToInt((Person p) -> {return p.getAge();})
            // .mapToInt((Person p) ->  p.getAge())
            // .mapToInt((p) -> p.getAge())
            // .mapToInt(p -> p.getAge())
            .mapToInt(Person::getAge);
        then(stream.sum()).isEqualTo(0);
    }

    /**
     * <pre>
     *    groupBy 一定返回 Map
     *      classifier 指定 map 的 key
     *      downstream 指定 map 的 value， 如何处理被 key 分组后的 stream
     * </pre>
     */
    @Test
    void givenStream_whenGroup_then() {
        // 分组
        Map<Sex, List<Person>> genderGroup = personList.stream()
            .collect(groupingBy(Person::getGender));
        // 分组 + downstream
        Map<Sex, Set<Person>> genderGroupWithSetDownstream = personList.stream()
            .collect(groupingBy(Person::getGender, toSet()));
        // 自定义返回的 Map。默认使用 HashMap
        Map<Sex, Set<Person>> genderGroupWithSetDownstreamAndTreeMap = personList.stream()
            .collect(groupingBy(Person::getGender, TreeMap::new, toSet()));
        //
        // 以下分别使用不同的 downstream
        // 分组 + mapping to Set
        Map<Sex, Set<String>> sexToNameSet = personList.stream()
            .collect(groupingBy(Person::getGender, mapping(Person::getName, toSet())));
        // 分组 + 计算 sum
        Map<Sex, Integer> sexToAgeSum = personList.stream()
            .collect(groupingBy(Person::getGender, reducing(0, Person::getAge, Integer::sum)));
    }

    /**
     * <pre>
     *      Collection stream limit 测试
     *      limit(n) 取 stream 中最多 n 个元素
     *
     * </pre>
     */
    @Test
    void givenCollection_whenStream_then() {
        Set<Integer> result = Stream.of(1, 2, 3, 4, 5).limit(2)
            .map(x -> x * 2)
            .collect(toSet());
        then(result).contains(2, 4);
    }

}
