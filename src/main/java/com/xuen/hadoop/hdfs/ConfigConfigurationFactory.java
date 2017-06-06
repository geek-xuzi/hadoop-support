package com.xuen.hadoop.hdfs;

import java.io.IOException;
import java.util.Properties;
import org.apache.hadoop.conf.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

/**
 * @author zheng.xu
 * @since 2017-06-06
 */
@Component
public class ConfigConfigurationFactory implements ConfigurationFactory {

    private static final Properties hadoopConfig = new Properties();

    static {
        Resource resource = new ClassPathResource("/hadoop.properties");
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            hadoopConfig.putAll(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHDFSBasePath() {

        return hadoopConfig.getOrDefault(Constants.DEFAULT_FS, "").toString();
    }

    @Override
    public Configuration createConfiguration() {
        final Configuration configuration = new Configuration();
        hadoopConfig.entrySet().forEach(item -> {
            configuration.set(item.getKey().toString(), item.getValue().toString());
        });
        return configuration;
    }


}
