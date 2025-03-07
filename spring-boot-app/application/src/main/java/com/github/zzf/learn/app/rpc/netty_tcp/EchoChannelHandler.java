package com.github.zzf.learn.app.rpc.netty_tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2022/06/02
 */
@Slf4j
public class EchoChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        log.info("utf8: {}", buf.toString(StandardCharsets.UTF_8));
        log.info("hex:\n{}", ByteBufUtil.prettyHexDump(buf));
        ctx.writeAndFlush(buf);
    }


}
