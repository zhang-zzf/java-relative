package com.github.zzf.learn.app.rpc.netty_tcp;

import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.netty4.NettyEventExecutorMetrics;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author zhanfeng.zhang
 * @date 2022/06/02
 */
@Slf4j
@Component
public class EchoServer {

    NioEventLoopGroup nioEventLoopGroup;

    @SneakyThrows
    @PostConstruct
    public void init() {
        final int port = 8888;
        nioEventLoopGroup = new NioEventLoopGroup();
        final ServerBootstrap serverBootstrap = new ServerBootstrap()
            .group(nioEventLoopGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new EchoChannelHandler());
                }
            });
        serverBootstrap.bind(port).addListener((future -> log.info("server listened at {}", port)));
    }

    @PreDestroy
    public void destroy() {
        nioEventLoopGroup.shutdownGracefully();
    }

    @Configuration
    public static class NettyEventExecutorMetricsConfiguration {

        @Bean
        public MeterBinder NettyEventExecutorMetrics(EchoServer echoServer) {
            return new NettyEventExecutorMetrics(echoServer.nioEventLoopGroup);
        }

    }
}
