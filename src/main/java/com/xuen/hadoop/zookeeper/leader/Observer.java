package com.xuen.hadoop.zookeeper.leader;

/**
 * User: zhaohuiyu
 * Date: 5/28/13
 * Time: 4:33 PM
 */
public interface Observer {
    String getMyId();

    String getLeaderId() throws Exception;
}
