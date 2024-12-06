package com.github.learn.springframework.i18n.jdk;

import static org.assertj.core.api.BDDAssertions.then;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ResourceBundleTest {

    /**
     * jdk 默认实现的 i18n
     *
     * <pre>
     * miniaa4a @ main git:(master) ✗ ➜ tree -L 2 resources
     * resources
     * ├── application.yaml
     * └── i18n
     *     ├── Exceptions.properties
     *     └── Exceptions_zh_CN.properties
     * </pre>
     * 测试时默认 Locale 为 en_CN，为对应的 properties，使用 Exceptions.properties
     */
    @Test
    void givenJDK_whenI18n_then() {
        // 测试时默认时 en_CN
        Locale localeDefault = Locale.getDefault();
        log.info("Locale.Default -> {}", localeDefault);
        then(localeDefault.toString()).isNotEqualTo("zh_CN");
        ResourceBundle defaultRb = ResourceBundle.getBundle("i18n/Exceptions");
        then(defaultRb).returns(true, d -> d.containsKey("404"))
            .returns("Not Found", d -> d.getString("404"));
    }

    /**
     * jdk 默认实现的 i18n
     *
     * <pre>
     * miniaa4a @ main git:(master) ✗ ➜ tree -L 2 resources
     * resources
     * ├── application.yaml
     * └── i18n
     *     ├── Exceptions.properties
     *     └── Exceptions_zh_CN.properties
     * </pre>
     */
    @Test
    void givenJDK_whenI18nZhCN_then() {
        // 测试时默认时 en_CN
        Locale.setDefault(Locale.CHINA);
        Locale localeDefault = Locale.getDefault();
        log.info("Locale.Default -> {}", localeDefault);
        then(localeDefault.toString()).isEqualTo("zh_CN");
        ResourceBundle defaultRb = ResourceBundle.getBundle("i18n/Exceptions");
        then(defaultRb).returns(true, d -> d.containsKey("404"))
            .returns("你需要的资源被猫吃了", d -> d.getString("404"))
            // Exceptions_zh_CN.properties 不包含 400
            // Exceptions.properties 包含 400
            // 查找 400 时，先查找 Exceptions_zh_CN.properties，找不到，查找 Exceptions_zh_CN.properties 的父 Exceptions.properties
            .returns("Bad Request", d -> d.getString("400"))
        ;
    }

    /**
     * jdk 默认实现的 i18n
     *
     * <pre>
     * miniaa4a @ main git:(master) ✗ ➜ tree -L 2 resources
     * resources
     * ├── application.yaml
     * └── i18n
     *     ├── Exceptions.properties
     *     └── Exceptions_zh_CN.properties
     * </pre>
     * 测试时默认 Locale 为 en_CN，为对应的 properties，使用 Exceptions.properties
     */
    @Test
    void givenJDK_whenI18nWithPlaceHolder_then() {
        // 测试时默认时 en_CN
        Locale localeDefault = Locale.getDefault();
        log.info("Locale.Default -> {}", localeDefault);
        then(localeDefault.toString()).isNotEqualTo("zh_CN");
        ResourceBundle defaultRb = ResourceBundle.getBundle("i18n/Exceptions");
        String messageHolder = defaultRb.getString("messageHolder");
        then(messageHolder).isEqualTo("Hello, {0}");
        String actualValue = MessageFormat.format(messageHolder, "zhang.zzf");
        then(actualValue).isEqualTo("Hello, zhang.zzf");
    }

}
