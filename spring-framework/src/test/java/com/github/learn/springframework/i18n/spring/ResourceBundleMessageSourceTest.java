package com.github.learn.springframework.i18n.spring;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

public class ResourceBundleMessageSourceTest {


  @Test
  void givenSpring_whenI18n_then() {
    ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
    ms.addBasenames("i18n/Exceptions");
    String message = ms.getMessage("404", null, Locale.getDefault());
    then(message).isEqualTo("Not Found");
  }
}
