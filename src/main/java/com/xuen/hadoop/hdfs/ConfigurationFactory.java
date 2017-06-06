package com.xuen.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;

/**
 * @author zheng.xu
 * @since 2017-06-06
 */
public interface ConfigurationFactory {

    /**
     * 获取hdfs的基础路径
     */
    String getHDFSBasePath();

    Configuration createConfiguration();

}
