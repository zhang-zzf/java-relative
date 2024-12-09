package com.github.zzf.dd.repo.mysql.my_db;

import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.devtools.autoconfigure.DevToolsDataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 定义用于测试spring jdbc 的上下文。包括 DataSource / Flyway / Mybatis / Transaction等
 * <p>注意：用于Flyway / Mybatis / Transaction 的DataSource 必须是同一个Bean</p>
 * <p></p>
 * <p>使用方法：</p>
 * <p>1. 定义DBConfigFor${DB_ID} 类</p>
 * <p>2. 更改 DB_ID / MYBATIS_MAPPER_LOCATION，定义成正确的值</p>
 * <p>3. 配置flyway，把测试环境需要的数据初始化脚本放置到 src/test/resources/db/migration/${DB_ID}
 * </p>
 * <p>4. 配置mybatis，把@MapperScan 中的sqlSessionFactoryRef / basePackages
 * 更改为正确的值</p>
 */
// @Configuration("DBConfig/" + DBConfig.DB_ID)
// 开始事务管理
@EnableTransactionManagement
// 配置mapper接口包
@MapperScan(sqlSessionFactoryRef = DBConfig.SQL_SESSION_FACTORY_BEAN_ID)
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:app-repo.properties")
public class DBConfig {

    public static final String DB_ID = "my-db";
    public static final String SQL_SESSION_FACTORY_BEAN_ID = "SqlSessionFactory/" + DB_ID;
    public static final String DATASOURCE_BEAN_ID = "DataSource/" + DB_ID;
    public static final String JDBC_TEMPLATE_BEAN_ID = "JdbcTemplate/" + DB_ID;

    @Bean(DATASOURCE_BEAN_ID)
    @ConfigurationProperties(prefix = "spring.datasource.hikari." + DB_ID)
    HikariDataSource dataSource() {
        HikariDataSource ret = new HikariDataSource();
        ret.setPoolName("HikariPool/" + DB_ID);
        return ret;
    }

    /**
     * 定义mybatis
     */
    @Bean(SQL_SESSION_FACTORY_BEAN_ID)
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(
        @Qualifier(DATASOURCE_BEAN_ID) DataSource dataSource) {
        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
        return factory;
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

    @Bean("Flyway/" + DB_ID)
    @ConditionalOnClass(DevToolsDataSourceAutoConfiguration.class)
    public Flyway flyway(@Qualifier(DATASOURCE_BEAN_ID) DataSource dataSource) {
        Flyway flyway = Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration/" + DB_ID.replaceAll("-", "_"))
            .load();
        // spring 容器启动时 start migrations
        flyway.migrate();
        return flyway;
    }

    /**
     * 启动一个默认的 Flyway，只创建对象，不迁移
     * <p>屏蔽 org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration 自动配置</p>
     */
    @Bean("Flyway/" + DB_ID)
    @ConditionalOnMissingBean(Flyway.class)
    public Flyway flywayNoMigrate(@Qualifier(DATASOURCE_BEAN_ID) DataSource dataSource) {
        return Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration/" + DB_ID.replaceAll("-", "_"))
            .load();
    }


    @Bean(JDBC_TEMPLATE_BEAN_ID)
    public JdbcTemplate jdbcTemplate(
        @Qualifier(DATASOURCE_BEAN_ID) DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
