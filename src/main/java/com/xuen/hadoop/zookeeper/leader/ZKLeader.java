package com.xuen.hadoop.zookeeper.leader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PreDestroy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: zhaohuiyu
 * Date: 5/21/13
 * Time: 2:32 PM
 */
public class ZKLeader implements Leader, LeaderSelectorListener {
    private static final Logger logger = LoggerFactory.getLogger(ZKLeader.class);

    private final List<LeaderChangedListener> leaderChangedListeners;

    private LeaderSelector selector;

    private Semaphore taskLatch = new Semaphore(0);

    private volatile boolean isLeader = Boolean.FALSE;

    private AtomicBoolean STATE = new AtomicBoolean(Boolean.TRUE);

    public ZKLeader() {
        this.leaderChangedListeners = new ArrayList<LeaderChangedListener>();
    }

    @Override
    public String getMyId() {
        return this.selector.getId();
    }

    @Override
    public String getLeaderId() throws Exception {
        return this.selector.getLeader().getId();
    }

    @Override
    public void addListener(LeaderChangedListener listener) {
        this.leaderChangedListeners.add(listener);
    }

    @Override
    public void takeLeadership(CuratorFramework sender) throws Exception {
        try {
            isLeader = true;
            for (LeaderChangedListener listener : leaderChangedListeners) {
                listener.own();
            }
        } catch (Exception e) {
            logger.error("execute dotask error, error message is {}", e.getMessage(), e);
            taskLatch.release();
        }
        try {
            taskLatch.acquire();
        } catch (Exception e) {
            logger.error("taskLatch.acquire error, error message is {}", e.getMessage(), e);
        } finally {
            for (LeaderChangedListener listener : leaderChangedListeners) {
                listener.lost();
            }
        }
    }

    @Override
    public void stateChanged(CuratorFramework sender, ConnectionState state) {
        if (isLeader) {
            isLeader = false;
            if (state == ConnectionState.LOST) {
                taskLatch.release();
            }
            if (state == ConnectionState.SUSPENDED) {
                taskLatch.release();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    logger.error("thread sleep error", e);
                }
                selector.requeue();
            }
        }
    }

    public void setSelector(LeaderSelector selector) {
        this.selector = selector;
    }

    public void start() {
        this.selector.start();
    }

    @PreDestroy
    public void destroy() {
        if (STATE.compareAndSet(Boolean.TRUE, Boolean.FALSE)) {
            if (selector != null) {
                selector.close();
            }
        }
    }
}
