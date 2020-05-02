package com.feng.learn.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zhanfeng.zhang
 * @date 2019/10/18
 */
@Configuration
@MapperScan(basePackages = {
    "com.feng.learn.dao",
})
@EnableTransactionManagement
public class DBConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test");
        mysqlDataSource.setUser("test");
        mysqlDataSource.setPassword("test@%Mysql0.");
        return mysqlDataSource;
    }

    /**
     * 用于管理mybatis的事务管理器
     * <p>为事务管理器指定的 DataSource 必须和用来创建 SqlSessionFactoryBean 的是同一个数据源 ，
     * 否则事务管理器就无法工作了</p>
     *
     * @param dataSource 数据源
     * @return 事务管理器
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(ApplicationContext context, DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

        factoryBean.setMapperLocations(
            resourceResolver.getResources("classpath:/com/feng/learn/dao/*.xml")
        );
        factoryBean.setConfigLocation(context.getResource("classpath:mybatis-global-config.xml"));

        return factoryBean.getObject();
    }


}
