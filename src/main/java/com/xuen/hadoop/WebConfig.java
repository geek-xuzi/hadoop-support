package com.xuen.hadoop;

import javax.sql.DataSource;
import org.apache.hive.jdbc.HiveDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * @author zheng.xu
 * @since 2017-06-06
 */
@Configuration
@ComponentScan("com.xuen.hadoop.*")
public class WebConfig {

    @Bean(name = "hiveDriver")
    public HiveDriver hiveDriver(){
        return new HiveDriver();
    }

    @Bean(name = "hiveDataSource")
    public DataSource hiveDataSource(){
        return new SimpleDriverDataSource(hiveDriver(),"jdbc:hive2://10.32.64.15:10000/test");
    }

    @Bean(name = "hiveJdbcTemplate")
    public JdbcTemplate hiveJdbcTemplate(){
        return new JdbcTemplate(hiveDataSource());
    }
}
