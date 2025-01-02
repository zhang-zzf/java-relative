package com.github.learn.guava;


import static com.google.common.base.CharMatcher.*;
import static com.google.common.base.Objects.*;
import static com.google.common.base.Preconditions.*;
import static com.google.common.collect.Comparators.greatest;
import static com.google.common.collect.Comparators.least;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Comparator.comparingInt;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import com.google.common.base.*;
import com.google.common.collect.Comparators;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-31
 */
public class GuavaTest {

    @Test
    void givenGuava_whenPrecondition_then() {
        Object obj = new Object();
        Object notNullObj = checkNotNull(obj);
        // 等价于
        Object notNullObj2 = Objects.requireNonNull(obj);
        int i = 5;
        // 参数检查，
        Throwable throwable = catchThrowable(() -> checkArgument(i < 5, "i must be less than 5"));
        then(throwable).isInstanceOfAny(IllegalArgumentException.class);
        // 对象状态检查
        Object nullObj = null;
        Throwable t2 = catchThrowable(() -> checkState(nullObj != null, "nullObj must not be null"));
        then(t2).isInstanceOfAny(IllegalStateException.class);
    }

    @Test
    void givenGuava_whenObjects_then() {
        then(equal(null, null)).isTrue();
        then(Objects.equals(null, null)).isTrue();
    }


    @Test
    void givenGuava_whenOrdering_then() {
        List<Integer> objectList = new ArrayList<>();
        // Ordering 可以使用 Stream.sorted 替代
        // List<Object> sortedList = Ordering.natural().nullsFirst().onResultOf(Object::toString).immutableSortedCopy(objectList);
        List<Object> sortedList = objectList.stream()
            // .sorted(Comparator.comparing(Object::toString))
            // .sorted(Comparator.comparing(Object::toString).reversed())
            .sorted(Comparator.comparing(Object::toString, Comparator.reverseOrder()))
            .collect(toList());
        //
        Comparator naturalOrder = naturalOrder();
        Comparator reverseOrder = Comparator.reverseOrder();
        Comparator nullsFirst = Comparator.nullsFirst(naturalOrder);
        Comparator comparator = nullsFirst.thenComparing(reverseOrder);
        /**
         * For example, to sort a collection of String based on the length
         * and then case-insensitive natural ordering,
         * the comparator can be composed using following code,
         */
        Comparator<String> cmp = comparingInt(String::length)
            .thenComparing(String.CASE_INSENSITIVE_ORDER);
        // topN
        // List<Integer> topN = Ordering.natural().greatestOf(objectList, 5);
        List<Integer> topN = objectList.stream()
            .collect(greatest(5, naturalOrder()));
        List<String> topN2 = Stream.of("foo", "quux", "banana", "elephant")
            .collect(least(2, comparingInt(String::length)));
        // 判断是否有序
        Comparators.isInOrder(topN, naturalOrder);
    }

    @Test
    void givenGuava_whenCollection_then() {
        // ImmutableList<Integer> list = ImmutableList.of(1, 2, 3);
        List<Integer> list = List.of(1, 1, 3);
        // ImmutableSet<Integer> set = ImmutableSet.of(1, 1, 2);
        Set<Integer> set = Set.of(1, 2);
        // ImmutableSet<Integer> setCopyOf = ImmutableSet.copyOf(list); // set.asList();
        Set<Integer> setCopyOf = Set.copyOf(list);
        // ImmutableList<Integer> listCopyOf = ImmutableList.copyOf(set);
        List<Integer> listCopyOf = List.copyOf(set);
        // use as Stream Collector
        Collector<Object, ?, ImmutableList<Object>> collector = ImmutableList.toImmutableList();
        Collections.emptyList();
        Collections.emptySet();
        Collections.emptyMap();
        // Lists
        List<List<Integer>> partitionList = Lists.partition(list, 50);
        // Sets
        Set<Integer> union = Sets.union(set, setCopyOf);
        // Set<Integer> union = Stream.concat(set.stream(), setCopyOf.stream()).collect(toSet());
    }

    @Test
    void givenGuava_whenString_then() {
        // Stream.reduce
        String joined = Joiner.on(";").join(Arrays.asList("foo", "bar", "baz"));
        String[] splited = "foo, bar, baz".split(",");
        // 保留所有 ascii 字符
        CharMatcher.ascii().retainFrom(joined);
        CharMatcher.javaIsoControl();
        // trim
        whitespace().trimFrom(joined);
        whitespace().trimAndCollapseFrom(joined, '_');
        CharMatcher.anyOf("*");
    }


    @Test
    void givenGuava_whenHashing_then() {
        // 无状态，线程安全
        HashFunction sha512 = Hashing.sha512();
        HashCode hashCode = sha512.hashString("foo", UTF_8);
        byte[] bytes = hashCode.asBytes();
        // 有状态，线程栈封闭（方法内使用）
        sha512.newHasher()
            .putByte((byte) 1)
            .putString("foo", UTF_8)
            .putBytes(bytes)
            .hash()
        ;
        //
        // BloomFilter
        BloomFilter<CharSequence> bf = BloomFilter.create(Funnels.stringFunnel(UTF_8), 1000000, 0.0001);
        for (int i = 0; i < 10000; i++) {
            bf.put("" + i);
        }
        for (int i = 0; i < 10000; i++) {
            bf.mightContain("" + i);
        }
    }

}
