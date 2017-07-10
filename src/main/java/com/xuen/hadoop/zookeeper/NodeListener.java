package com.xuen.hadoop.zookeeper;

/**
 * @author zheng.xu
 * @since 2017-07-10
 */
public interface NodeListener {

    void nodeChanged(ZKClient sender, ChangedEvent event) throws Exception;
}
