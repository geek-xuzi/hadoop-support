package com.xuen.hadoop.hbase;

import java.io.IOException;
import java.util.Properties;
import org.apache.hadoop.conf.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

/**
 * @author zheng.xu
 * @since 2017-06-23
 */
@Component
public class HBaseConfigurationFactory implements HBaseConfiguration {

    private static Properties hbaseConfig = new Properties();

    static {
        Resource resource = new ClassPathResource("hbase.properties");
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            hbaseConfig.putAll(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Configuration createConfiguration(){
        Configuration configuration = org.apache.hadoop.hbase.HBaseConfiguration.create();
        hbaseConfig.entrySet().forEach(item -> {
            configuration.set(item.getKey().toString(),item.getValue().toString());
        });
        return configuration;
    }

}
