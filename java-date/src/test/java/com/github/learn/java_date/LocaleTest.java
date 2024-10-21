package com.github.learn.java_date;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Locale;
import java.util.ResourceBundle;
import org.junit.jupiter.api.Test;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-10-21
 */
public class LocaleTest {

    @Test
    void givenLocale_when_then() {
        then(Locale.getDefault().toString()).isEqualTo("en_CN");
        // 更改默认 Locale
        Locale.setDefault(Locale.CHINA);
        then(Locale.CHINA.toString()).isEqualTo("zh_CN");
        then(Locale.getDefault().toString()).isEqualTo("zh_CN");
        then(new Locale("zh", "CN").toString()).isEqualTo("zh_CN");
    }


    @Test
    void givenResourceBundle_when_then() {
        then(ResourceBundle.getBundle("exception",Locale.ENGLISH).getString("key1"))
            .isEqualTo("Hello, World!");
        then(ResourceBundle.getBundle("exception",Locale.CHINA).getString("key1"))
            .isEqualTo("你好，世界！");
    }

}
