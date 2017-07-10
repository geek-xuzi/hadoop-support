package com.xuen.hadoop.zookeeper.leader;

import java.util.EventListener;

/**
 * User: zhaohuiyu
 * Date: 1/28/13
 * Time: 11:58 AM
 */
public interface LeaderChangedListener extends EventListener {
    void own();

    void lost();
}
