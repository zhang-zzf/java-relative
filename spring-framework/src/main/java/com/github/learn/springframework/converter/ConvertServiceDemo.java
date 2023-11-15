package com.github.learn.springframework.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConvertServiceDemo {
  final ConversionService conversionService;

  public String convertToString(Integer x) {
    if (conversionService.canConvert(Integer.class, String.class)) {
      return conversionService.convert(x, String.class);
    }
    throw new IllegalArgumentException();
  }

}
