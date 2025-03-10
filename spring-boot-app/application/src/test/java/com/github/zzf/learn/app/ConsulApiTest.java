package com.github.zzf.learn.app;

import static org.assertj.core.api.BDDAssertions.then;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ConsulApiTest {

    @Disabled
    @Test
    void givenConsulApi_when_then() {
        ConsulClient client = new ConsulClient();
        String key = "consul.api.kv.1";
        String value = "Hello, consul.";
        client.setKVValue(key, value);
        Response<GetValue> kvValue = client.getKVValue(key);
        then(kvValue.getValue().getDecodedValue()).isEqualTo(value);
        Response<GetValue> bqKvValue = client.getKVValue(key, new QueryParams(5 * 60, kvValue.getConsulIndex()));
        then(bqKvValue.getValue().getDecodedValue()).isEqualTo(value);
    }
}
