package com.github.learn.infra.http.feign;

import com.github.learn.infra.http.dto.HttpMethodsGetParam;
import com.github.learn.infra.http.dto.HttpMethodsResp;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

@Headers({"Content-Type: application/xml"})
public interface HttpBin {

  @RequestLine("GET /get")
  @Headers({
      "Content-Type: application/json",// 同名覆盖
      "X-User-Id: {userId}"
  })
  HttpMethodsResp httpMethodGet(@Param("userId") String userId);

  /**
   * @param param 没有添加任何注解，会被序列化到 http 的 body 中
   */
  @RequestLine("GET /get")
  @Headers({
      "Content-Type: application/json",// 同名覆盖
      "X-User-Id: {userId}"
  })
  HttpMethodsResp httpMethodGet(
      @Param("userId") String userId,
      HttpMethodsGetParam param
  );

  /**
   * @param param 添加 QueryMap 参数，序列化到 url 的请求参数中
   */
  @RequestLine("GET /get?user_id={userId}")
  @Headers({
      "Content-Type: application/json",// 同名覆盖
      "X-User-Id: {userId}"
  })
  HttpMethodsResp httpMethodGetWithParam(
      @Param("userId") String userId,
      @QueryMap HttpMethodsGetParam param
  );

  @RequestLine("GET /post")
  HttpMethodsResp httpMethodGetPost();

  @RequestLine("GET /base64/{val}")
  String dynamicDataBase64(@Param("val") String val);

  @RequestLine("POST /post")
  @Headers({"Content-Type: application/json"})
  HttpMethodsResp httpMethodPost();
}

