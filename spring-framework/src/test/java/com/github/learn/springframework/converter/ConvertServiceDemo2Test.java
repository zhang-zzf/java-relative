package com.github.learn.springframework.converter;

import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;

class ConvertServiceDemo2Test {
    final ConvertServiceDemo2 convertServiceDemo2 = new ConvertServiceDemo2();

    final ConversionService cs = new DefaultConversionService();

    @Test
    void givenCollection_whenConvertString_when() {
        List<Long> longList = List.of(1L, 2L, 3L);
        String result = cs.convert(longList, String.class);
        then(result).isNotNull();
    }

    @Test
    void givenCollection_whenConvertToIntArray_when() {
        List<Long> longList = List.of(1L, 2L, 3L);
        int[] result = cs.convert(longList, int[].class);
        then(result).isNotNull();
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenIntArray_whenConvertToListInteger_when() {
        int[] ints = {1, 2, 3};
        TypeDescriptor srcDesc = TypeDescriptor.array(
            TypeDescriptor.valueOf(Integer.TYPE));
        // TypeDescriptor srcDesc = TypeDescriptor.forObject(ints); // 和上面的等同
        TypeDescriptor dstDesc = TypeDescriptor.collection(List.class,
            TypeDescriptor.valueOf(Long.class));
        List<Long> list = (List<Long>) cs.convert(ints, srcDesc, dstDesc);
        then(list).isNotNull();
    }

    @Test
    void intArrayIsInstanceOfObjectArray() {
        Integer[] ints = new Integer[]{};
        then(Object[].class.isAssignableFrom(ints.getClass())).isTrue();
        Throwable t = catchThrowable(
            () -> Stream.of(1).collect(toMap(id -> id, id -> null)));
        then(t).isNotNull().isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNoSpringIoC_when_then() {
        String name = convertServiceDemo2.convertToString(1);
        then(name).isEqualTo("1");
    }

    @Test
    void givenSpringIoC_whenObjectConvertToString_then() {
        // 使用默认的 FallbackObjectToStringConverter ，需要满足以下条件
        // 2. Person 上存在可以从 String 构造的构造器/静态方法
        // 2.1  `public Person(String){}`
        // 2.2  `static Person of(String){}` / `static Person from(String){}` / `static Person valueOf(String){}`
        // 其中的一条时，可以使用个 FallbackObjectToStringConverter 把 Person 转换为 String
        //
        // 我们是要把 Person 转成 String，这里的判断条件是 `可否把 String 转成 Person ?`
        // 无法理解
        // 其中的一个解释是：若 Person 可以从 String 构建，那么 Person 也可以转成 String
        Person person = new Person("zhang.zzf", 18);
        String name = convertServiceDemo2.convertObjectToString(person);
        then(name).isEqualTo(
            "ConvertServiceDemo2Test.Person(name=zhang.zzf, age=18)");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Person {
        String name;
        int age;

        public static Person from(String json) {
            return new Person().setName("json");
        }
    }


}