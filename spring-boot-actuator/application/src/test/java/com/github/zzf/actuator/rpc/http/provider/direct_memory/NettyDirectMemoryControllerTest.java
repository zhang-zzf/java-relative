package com.github.zzf.actuator.rpc.http.provider.direct_memory;

import static org.junit.jupiter.api.Assertions.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.junit.jupiter.api.Test;

class NettyDirectMemoryControllerTest {


    @Test
    void givenByteBuf_whenNoRelease_then() {
        for (int i = 0; i < 50; ++i) {
            ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer(1024);
            byteBuf = null;
        }
        System.gc();
    }

}