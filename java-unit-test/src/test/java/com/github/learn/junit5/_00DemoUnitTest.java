package com.github.learn.junit5;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * <p>@Test</p>
 * <p>@ParameterizedTest</p>
 * <p>@RepeatedTest</p>
 * <p>@DisplayName</p>
 * <p>@BeforeEach</p>
 * <p>@BeforeAll</p>
 * <p>@AfterEach</p>
 * <p>@AfterAll</p>
 * <p>@Nested</p>
 * <p>@Disabled</p>
 * <p>@Timeout</p>
 * <p>@ExtendWith</p>
 *
 * @author zhanfeng.zhang
 * @date 2019/12/29
 */
@DisplayName("junit 5 learn demo")
public class _00DemoUnitTest {

  static Stream<Arguments> stringIntAndListProvider() {
    return Stream.of(
        arguments("apple", 1, Arrays.asList("a", "b")),
        arguments("lemon", 2, Arrays.asList("x", "y"))
    );
  }

  @DisplayName("@ParameterizedTest with ValueSource")
  @ParameterizedTest(name = "using parameter: {0}")
  @NullSource
  @EmptySource
  @NullAndEmptySource
  @ValueSource(strings = {"  ", " ", "    "})
  void givenParameterizedTest_whenWithValueSource_thenSuccess(String str) {
    boolean b = str == null || str.trim().isEmpty();
    then(b).isTrue();
  }

  @DisplayName("@ParameterizedTest with CsvSource")
  @ParameterizedTest(name = "concat({0}, {1}) is {2}")
  @CsvSource({
      "4,5,45",
      "1,2,12",
  })
  void givenParameterizedTest_whenWithCsvSource_thenSuccess(int i, long l, String str) {
    String r = "" + i + l;
    then(r).isEqualTo(str);
  }

  @DisplayName("@ParameterizedTest with CsvFileSource")
  @ParameterizedTest(name = "concat({0}, {1}) is {2}")
  @CsvFileSource(resources = {"/csv_file.txt"})
  void givenParameterizedTest_whenWithCsvFileSource_thenSuccess(int i, long l, String str) {
    String r = "" + i + l;
    then(r).isEqualTo(str);
  }

  @DisplayName("@ParameterizedTest with EnumSource")
  @ParameterizedTest()
  @EnumSource(value = TimeUnit.class, names = {"MINUTES", "DAYS"})
  void givenParameterizedTest_whenWithEnumParams_thenSuccess(TimeUnit unit) {
    then(EnumSet.allOf(TimeUnit.class).contains(unit)).isTrue();
  }

  @DisplayName("@ParameterizedTest with MethodSource")
  @ParameterizedTest
  @MethodSource("stringIntAndListProvider")
  void testWithMultiArgMethodSource(String str, int num, List<String> list) {
    assertEquals(5, str.length());
    assertTrue(num >= 1 && num <= 2);
    assertEquals(2, list.size());
  }
}
