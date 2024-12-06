package com.github.learn.springframework.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@Slf4j
public class ConvertServiceDemo2 {
    final ConversionService conversionService = new DefaultConversionService();

    public String convertToString(Integer x) {
        if (conversionService.canConvert(Integer.class, String.class)) {
            return conversionService.convert(x, String.class);
        }
        throw new IllegalArgumentException();
    }

    public String convertObjectToString(Object o) {
        return conversionService.convert(o, String.class);
    }

}
