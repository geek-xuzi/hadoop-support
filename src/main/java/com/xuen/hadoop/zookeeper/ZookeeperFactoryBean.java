package com.xuen.hadoop.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author zheng.xu
 * @since 2017-06-08
 */
public class ZookeeperFactoryBean {


    private String zkConnectString;
    private RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    private int connectionTimeoutMs;
    private int sessionTimeoutMs;


    public CuratorFramework build() {
        return CuratorFrameworkFactory.builder().connectString(zkConnectString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }

    public static ZookeeperFactoryBean builder() {
        return new ZookeeperFactoryBean();
    }

    public ZookeeperFactoryBean setZkConnectString(String zkConnectString) {
        this.zkConnectString = zkConnectString;
        return this;
    }

    public ZookeeperFactoryBean setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
        return this;
    }

    public ZookeeperFactoryBean setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
        return this;
    }

    public ZookeeperFactoryBean setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
        return this;
    }
}
