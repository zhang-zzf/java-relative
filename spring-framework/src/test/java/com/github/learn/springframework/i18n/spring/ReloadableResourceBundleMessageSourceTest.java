package com.github.learn.springframework.i18n.spring;

import static org.assertj.core.api.BDDAssertions.then;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class ReloadableResourceBundleMessageSourceTest {


    @Test
    void givenSpring_whenI18n_then() {
        Locale aDefault = Locale.getDefault();
        // 英文（语言）_中国（地区）
        then(aDefault.toString()).isEqualTo("en_CN");
        ReloadableResourceBundleMessageSource rrbm = new ReloadableResourceBundleMessageSource();
        rrbm.setBasename("i18n/Exceptions");
        rrbm.setDefaultEncoding(StandardCharsets.UTF_8.name());
        String message = rrbm.getMessage("404", null, Locale.getDefault());
        then(message).isEqualTo("Not Found");
    }
}
