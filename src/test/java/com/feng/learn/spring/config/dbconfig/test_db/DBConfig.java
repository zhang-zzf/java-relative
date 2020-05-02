package com.feng.learn.spring.config.dbconfig.test_db;

import static com.feng.learn.spring.config.dbconfig.test_db.DBConfig.DB_ID;
import static com.feng.learn.spring.config.dbconfig.test_db.DBConfig.SQLSESSIONFACTORY_BEAN_ID;

import com.feng.learn.spring.config.dbconfig.H2TcpServerConfig;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.Server;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 定义用于测试spring jdbc 的上下文。包括 H2 / Flyway / Mybatis / Transaction等
 * <p>注意：用于Flyway / Mybatis / Transaction 的DataSource 必须是同一个Bean</p>
 * <p></p>
 * <p>使用方法：</p>
 * <p>1. 定义DBConfigFor${DB_ID} 类</p>
 * <p>2. 更改 DB_ID / MYBATIS_MAPPER_LOCATION，定义成正确的值</p>
 * <p>3. 配置flyway，把测试环境需要的数据初始化脚本放置到 src/test/resources/db/migration/${DB_ID} </p>
 * <p>4. 配置mybatis，把@MapperScan 中的sqlSessionFactoryRef / basePackages 更改为正确的值</p>
 *
 * @author zhanfeng.zhang
 * @date 2019/10/24
 */
@Configuration(DB_ID + "DBConfig")
// 开始事务管理
@EnableTransactionManagement
// 配置mapper接口包
@MapperScan(sqlSessionFactoryRef = SQLSESSIONFACTORY_BEAN_ID,
    basePackages = {"com.feng.learn.dao"})
@Import({
    H2TcpServerConfig.class,
})
@Slf4j
public class DBConfig {

    public static final String DB_ID = "test_db";
    public static final String SQLSESSIONFACTORY_BEAN_ID = DB_ID + "SqlSessionFactory";
    public static final String DATASOURCE_BEAN_ID = DB_ID + "DataSource";
    public static final String JDBCTEMPLATE_BEAN_ID = DB_ID + "JdbcTemplate";
    private static final String MYBATIS_MAPPER_LOCATION = "classpath:/com/feng/learn/dao";

    // 以下2个 DataSource 选用一个即可

    /**
     * 定义h2 内存型DataSource
     */
  /*
  @Bean(DATASOURCE_BEAN_ID)
  public DataSource dataSource() {
    JdbcConnectionPool dataSource = JdbcConnectionPool.create("jdbc:h2:mem:test_db;MODE=MYSQL", "", "");
    return dataSource;
  }
  */

    /**
     * 定义DataSource
     *
     * @return Tcp H2DataSource
     */
    @Bean(DATASOURCE_BEAN_ID)
    public DataSource dataSource(Server h2Server) {
        return JdbcConnectionPool.create("jdbc:h2:" + h2Server.getURL() + "/mem:" + DB_ID + ";MODE=MYSQL",
            "", H2TcpServerConfig.TCP_PASSWORD);
    }


    /**
     * 定义mybatis
     */
    @Bean(SQLSESSIONFACTORY_BEAN_ID)
    public SqlSessionFactory sqlSessionFactory(@Qualifier(DATASOURCE_BEAN_ID) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        // 配置DataSource
        factoryBean.setDataSource(dataSource);

        // 扫描mapper.xml
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        factoryBean.setMapperLocations(
            resourceResolver.getResources(MYBATIS_MAPPER_LOCATION + "/*.xml")
        );

        // mybatis 自定义配置
        factoryBean.setConfiguration(customMybatisConfiguration());

        return factoryBean.getObject();
    }

    /**
     * 用于管理mybatis的事务管理器
     * <p>为事务管理器指定的 DataSource 必须和用来创建 SqlSessionFactoryBean 的是同一个数据源 ，
     * 否则事务管理器就无法工作了</p>
     *
     * @param dataSource 数据源
     * @return 事务管理器
     */
    @Bean(DB_ID + "DataSourceTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager(
        @Qualifier(DATASOURCE_BEAN_ID) DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 定义flyway， 用于测试数据库的初始化
     * <p>初始化脚本放在 src/test/resources/db/migration/${DB_ID} 目录下 </p>
     *
     * @param dataSource 用于测试的DataSource
     */
    @Bean(DB_ID + "Flyway")
    public Flyway initDb(@Qualifier(DATASOURCE_BEAN_ID) DataSource dataSource) {
        Flyway flyway = Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db.migration." + DB_ID)
            .load();
        flyway.migrate();
        return flyway;
    }

    @Bean(JDBCTEMPLATE_BEAN_ID)
    public JdbcTemplate jdbcTemplate(@Qualifier(DATASOURCE_BEAN_ID) DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    private org.apache.ibatis.session.Configuration customMybatisConfiguration() {
        org.apache.ibatis.session.Configuration c = new org.apache.ibatis.session.Configuration();
        c.setMapUnderscoreToCamelCase(true);
        // 查询日志使用
        c.setLogPrefix("dao." + DB_ID + ".");
        return c;
    }

}
