package com.github.learn.infra.http.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HttpMethodsGetParam {
  Long id;
  String name;
}
