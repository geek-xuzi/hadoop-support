package com.xuen.hadoop.zookeeper;


/**
 * @author zheng.xu
 * @since 2017-07-10
 */
public interface ConnectionStateListener {

    void stateChanged(ZKClient sender, ConnectionState state);
}
