package com.xuen.hadoop.zookeeper.leader;


import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PreDestroy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;

/**
 * User: zhaohuiyu
 * Date: 5/28/13
 * Time: 4:35 PM
 */
public class ZKObserver implements Observer, LeaderSelectorListener {
    private LeaderSelector selector;

    private AtomicBoolean STATE = new AtomicBoolean(Boolean.TRUE);

    @Override
    public String getMyId() {
        return selector.getId();
    }

    @Override
    public String getLeaderId() throws Exception {
        return selector.getLeader().getId();
    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        return;
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
    }

    public void setSelector(LeaderSelector selector) {
        this.selector = selector;
    }

    @PreDestroy
    public void destroy() {
        if (STATE.compareAndSet(Boolean.TRUE, Boolean.FALSE)) {
            if (selector != null) {
                this.selector.close();
            }
        }
    }
}
