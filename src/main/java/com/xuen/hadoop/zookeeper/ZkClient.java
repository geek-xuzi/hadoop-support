package com.xuen.hadoop.zookeeper;

import org.apache.curator.framework.state.ConnectionStateListener;

/**
 * @author zheng.xu
 * @since 2017-06-08
 */
public interface ZkClient {

    /**
     * 为zk添加监听器
     * @param listener
     */
    void registerListeners(ConnectionStateListener listener);

    /**
     * 根据path创建node节点
     * @param path  路径  如  /aaa/bbb
     * @param data  数据
     * @throws Exception
     */
    void createForPath(String path, byte[] data) throws Exception;

    /**
     *
     * @param path
     * @throws Exception
     */
    void createForPath(String path) throws Exception;
}
