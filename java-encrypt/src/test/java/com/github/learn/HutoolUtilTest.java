package com.github.learn;

import cn.hutool.crypto.digest.DigestUtil;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.BDDAssertions.then;

public class HutoolUtilTest {

    @Test
    public void givenHutool_whenMd5_then() {
        String stationId = "8440";
        String stationName = "商品来源关系专用0002";
        byte[] md5Bytes = DigestUtil.md5(stationId + stationName);
        String md5Str = Base64.getEncoder().encodeToString(md5Bytes);
        then(md5Str).isNotBlank();

    }
}
