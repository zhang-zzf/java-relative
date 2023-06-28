package com.feng.learn.spring.config.dbconfig;

import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Tcp 型H2 DB 可以被多个客户端使用。
 *
 * @author zhanfeng.zhang
 * @date 2019/10/25
 */
@Configuration
@Slf4j
public class H2TcpServerConfig {

  public static final String TCP_PASSWORD = "h2";

  @Bean(destroyMethod = "stop")
  public Server h2TcpServer() throws SQLException {
    Server tcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers",
        "-tcpPort", "8043", "-tcpPassword", TCP_PASSWORD, "-ifNotExists"
    ).start();
    log.info("h2 started: {}", tcpServer.getURL());
    return tcpServer;
  }

}
