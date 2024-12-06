package com.github.learn;

import cn.hutool.crypto.digest.DigestUtil;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.BDDAssertions.then;

@Slf4j
public class HutoolUtilTest {

    @SneakyThrows
    @Test
    public void givenHutool_whenMd5_then() {
        String sn = "K212239P00051";
        String stationId = "8440";
        byte[] md5Bytes = DigestUtil.md5(stationId);
        String md5Str = Base64.getEncoder().encodeToString(md5Bytes);
        log.info("base64(md5({}) -> {}", stationId, md5Str);
        then(md5Str).isNotBlank();
        String auth = Base64.getEncoder().encodeToString((sn + ":" + md5Str).getBytes(StandardCharsets.UTF_8));
        log.info("base64({}:{}) -> {}", sn, md5Str, auth);
    }
}
