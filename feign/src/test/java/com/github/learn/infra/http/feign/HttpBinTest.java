package com.github.learn.infra.http.feign;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.learn.infra.http.dto.HttpMethodPostBody;
import com.github.learn.infra.http.dto.HttpMethodsGetParam;
import com.github.learn.infra.http.dto.HttpMethodsResp;
import feign.Feign;
import feign.FeignException.MethodNotAllowed;
import feign.Logger.Level;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class HttpBinTest {

    HttpBin httpBin;

    void defaultConfig() {
        httpBin = Feign.builder()
            .target(HttpBin.class, "https://httpbin.org/");
    }

    void jsonConfig() {
        final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(Include.NON_NULL)
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        httpBin = Feign.builder()
            .decoder(new JacksonDecoder(objectMapper))
            .encoder(new JacksonEncoder(objectMapper))
            .logger(new Slf4jLogger())
            .logLevel(Level.FULL)
            .target(HttpBin.class, "https://httpbin.org/");
    }

    void jsonOkHttpConfig() {
        final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(Include.NON_NULL)
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        httpBin = Feign.builder()
            .client(new OkHttpClient())
            .decoder(new JacksonDecoder(objectMapper))
            .encoder(new JacksonEncoder(objectMapper))
            .logger(new Slf4jLogger())
            .logLevel(Level.FULL)
            .target(HttpBin.class, "https://httpbin.org/");
    }


    @Test
    void givenDefaultConfig_whenGet_then() {
        jsonConfig();
        HttpMethodsResp getResp = httpBin.httpMethodGet(null);
        then(getResp).isNotNull();
        HttpMethodsResp postResp = httpBin.httpMethodPost();
        then(postResp).isNotNull();
    }

    /**
     * 405 抛异常
     */
    @Test
    void givenDefaultConfig_whenGetPOST_then() {
        jsonConfig();
        // 405
        Throwable t = catchThrowable(() -> httpBin.httpMethodGetPost());
        then(t).isInstanceOf(MethodNotAllowed.class)
            .returns(405, e -> ((MethodNotAllowed) e).status());
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
     * 请求头变量
     */
    @Test
    void givenHeaderParam_when_then() {
        jsonConfig();
        String userId = "zhang-zzf";
        HttpMethodsResp resp = httpBin.httpMethodGet(userId);
        then(resp.getHeaders()).containsValues(userId);
        log.info("Ok");
    }

    /**
     * 请求参数
     */
    @Test
    void givenQueryParam_whenGet_then() {
        jsonOkHttpConfig();
        String userId = "zhang-zzf";
        String name = "张占峰";
        HttpMethodsGetParam param = new HttpMethodsGetParam()
            .setId(1L).setName(name);
        Throwable t = catchThrowable(() -> httpBin.httpMethodGet(userId, param));
        // GET 请求携带 body 时，okhttp 抛出异常
        then(t).isNotNull().isInstanceOf(IllegalArgumentException.class);
        HttpMethodsResp resp = httpBin.httpMethodGetWithParam(userId, param);
        then(resp).isNotNull();
        then(resp.getArgs()).containsValues(name, userId);
    }

    /**
     * 请求参数，请求 body
     */
    @Test
    void givenQueryParamAndBody_whenPost_then() {
        jsonOkHttpConfig();
        String userId = "zhang-zzf";
        String name = "张占峰";
        HttpMethodsGetParam param = new HttpMethodsGetParam()
            .setId(1L).setName(name);
        HttpMethodPostBody body = new HttpMethodPostBody()
            .setFirstName("zhang");
        var resp = httpBin.httpMethodPostWithParamAndBody(userId, param, body);
        then(resp).isNotNull();
        then(resp.getArgs()).containsValues(name, userId);
    }

}