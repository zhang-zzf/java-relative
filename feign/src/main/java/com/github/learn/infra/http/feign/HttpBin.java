package com.github.learn.infra.http.feign;

import com.github.learn.infra.http.dto.HttpMethodsResp;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers({"Content-Type: application/xml"})
public interface HttpBin {

  @RequestLine("GET /get")
  // 同名覆盖
  @Headers({
      "Content-Type: application/json",
      "X-User-Id: {userId}"
  })
  HttpMethodsResp httpMethodGet(@Param("userId") String userId);

  @RequestLine("GET /post")
  HttpMethodsResp httpMethodGetPost();

  @RequestLine("GET /base64/{val}")
  String dynamicDataBase64(@Param("val") String val);

  @RequestLine("POST /post")
  @Headers({"Content-Type: application/json"})
  HttpMethodsResp httpMethodPost();
}

