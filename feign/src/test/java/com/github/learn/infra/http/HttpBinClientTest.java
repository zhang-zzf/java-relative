package com.github.learn.infra.http;

import static org.assertj.core.api.BDDAssertions.then;

import com.github.learn.infra.http.dto.HttpMethodsResp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HttpBinClientTest {

    @InjectMocks
    HttpBinClient client;

    @Test
    void givenFein_when_then() {
        HttpMethodsResp resp = client.get();
        then(resp).isNotNull();
    }

}