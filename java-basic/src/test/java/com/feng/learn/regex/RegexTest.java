package com.feng.learn.regex;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-10-22
 */
public class RegexTest {


    @Test
    void givenRegex_when_then() {
        Pattern p = Pattern.compile("(\\d+)aa(\\d+)bb");
        then(p.matcher("92aa8091bb").matches()).isTrue();
        then(p.matcher("dfdf92aa8091bb").matches()).isFalse();
        then(p.matcher("92aa8091bbc").matches()).isFalse();
        then(p.matcher("92aaa8091bb").matches()).isFalse();

    }
}
