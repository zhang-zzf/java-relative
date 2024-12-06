package com.github.learn.infra.http;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.learn.infra.http.dto.HttpMethodsResp;
import com.github.learn.infra.http.feign.HttpBin;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class HttpBinClient {

    final ObjectMapper objectMapper = new ObjectMapper()
        .setSerializationInclusion(Include.NON_NULL)
        .configure(SerializationFeature.INDENT_OUTPUT, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    final HttpBin httpBin = Feign.builder()
        .decoder(new JacksonDecoder(objectMapper))
        .encoder(new JacksonEncoder(objectMapper))
        .target(HttpBin.class, "https://httpbin.org/");

    HttpMethodsResp get() {
        return httpBin.httpMethodGet(null);
    }
}
