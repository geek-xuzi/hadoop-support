package com.xuen.hadoop.hbase;

import org.apache.hadoop.conf.Configuration;

/**
 * @author zheng.xu
 * @since 2017-06-23
 */
public interface HBaseConfiguration {

    Configuration createConfiguration();
}
