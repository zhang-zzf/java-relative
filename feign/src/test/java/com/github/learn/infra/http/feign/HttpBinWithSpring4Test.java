package com.github.learn.infra.http.feign;

import static org.assertj.core.api.BDDAssertions.then;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.learn.infra.http.dto.HttpMethodPostBody;
import feign.Feign;
import feign.Logger.Level;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import feign.spring.SpringContract;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class HttpBinWithSpring4Test {

  HttpBinWithSpring4 httpBin;

  void defaultConfig() {
    httpBin = Feign.builder()
        .contract(new SpringContract())
        .target(HttpBinWithSpring4.class, "https://httpbin.org/");
  }

  void jsonOkHttpConfig() {
    final ObjectMapper objectMapper = new ObjectMapper()
        .setSerializationInclusion(Include.NON_NULL)
        .configure(SerializationFeature.INDENT_OUTPUT, false)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    httpBin = Feign.builder()
        .contract(new SpringContract())
        .client(new OkHttpClient())
        .decoder(new JacksonDecoder(objectMapper))
        .encoder(new JacksonEncoder(objectMapper))
        .logger(new Slf4jLogger())
        .logLevel(Level.FULL)
        .target(HttpBinWithSpring4.class, "https://httpbin.org/");
  }

  /**
   * RequestLine 路径变量
   */
  @Test
  void givenDefaultConfig_whenRequestLineParam_then() {
    defaultConfig();
    String resp = httpBin.dynamicDataBase64("SFRUUEJJTiBpcyBhd2Vzb21l");
    then(resp).isNotNull();
  }

/**
   * 请求参数，请求 body
   */
  @Test
  void givenQueryParamAndBody_whenPost_then() {
    jsonOkHttpConfig();
    String userId = "zhang-zzf";
    String name = "张占峰";
    Map<String, String> param = new HashMap<>(8) {{
      put("id", "1");
      put("name", name);
    }};
    HttpMethodPostBody body = new HttpMethodPostBody()
        .setFirstName("zhang");
    var resp = httpBin.httpMethodPostWithParamAndBody(userId, param, body);
    then(resp).isNotNull();
  }

}