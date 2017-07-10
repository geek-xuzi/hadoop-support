package com.xuen.hadoop.zookeeper;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import com.xuen.hadoop.zookeeper.ChangedEvent.Type;
import com.xuen.hadoop.zookeeper.leader.Leader;
import com.xuen.hadoop.zookeeper.leader.Observer;
import com.xuen.hadoop.zookeeper.leader.ZKLeader;
import com.xuen.hadoop.zookeeper.leader.ZKObserver;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PreDestroy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.ThreadUtils;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zheng.xu
 * @since 2017-07-10
 */
public class ZKClientImpl implements ZKClient{
    private static final Logger logger = LoggerFactory.getLogger(ZKClientImpl.class);

    private final CuratorFramework client;
    private ExecutorService EVENT_THREAD_POOL = Executors.newFixedThreadPool(1, ThreadUtils.newThreadFactory("PathChildrenCache"));
    private Executor SAME_EXECUTOR = MoreExecutors.sameThreadExecutor();
    private final List<ZKLeader> leaders;
    private final List<ZKObserver> observers;
    private final AtomicInteger REFERENCE_COUNT = new AtomicInteger(0);

    ZKClientImpl(String adds) {
        client = CuratorFrameworkFactory.builder()
                .connectString(adds)
                .retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000))
                .connectionTimeoutMs(5000).build();
        waitUntilZkStart();
        leaders = Lists.newArrayList();
        observers = Lists.newArrayList();
    }

    private void waitUntilZkStart() {

        CountDownLatch latch = new CountDownLatch(1);
        addConnectionChangeListenter(new ConnectionWatcher(latch));
        client.start();
        try {
            latch.wait();
        } catch (InterruptedException e) {
            logger.error("start zk latch.wait() error",e);
        }
    }


    @Override
    public List<String> getChildren(String path) throws Exception {
        return client.getChildren().forPath(path);
    }

    @Override
    public List<String> listenChildrenPath(String parent, NodeListener listener,
            boolean sync) throws Exception {
        PathChildrenCache cache = new PathChildrenCache(client, parent, false, false, EVENT_THREAD_POOL);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework c, PathChildrenCacheEvent e)
                    throws Exception {
                if (e.getType() == null) return;
                switch (e.getType()){
                    case CHILD_ADDED:
                        listener.nodeChanged(ZKClientImpl.this, new ChangedEvent(e.getData().getPath(), Type.CHILD_ADDED));
                        break;
                    case CHILD_REMOVED:
                        listener.nodeChanged(ZKClientImpl.this, new ChangedEvent(e.getData().getPath(), Type.CHILD_REMOVED));
                        break;
                    case CHILD_UPDATED:
                        listener.nodeChanged(ZKClientImpl.this, new ChangedEvent(e.getData().getPath(), Type.CHILD_UPDATED));
                        break;
                }
            }
        },SAME_EXECUTOR);
        PathChildrenCache.StartMode mode = sync ? PathChildrenCache.StartMode.BUILD_INITIAL_CACHE : PathChildrenCache.StartMode.NORMAL;
        cache.start(mode);
        List<ChildData> children = cache.getCurrentData();
        List<String> result = Lists.newArrayList();
        for (ChildData child: children){
            result.add(child.getPath());
        }
        return result;
    }

    @Override
    public String addEphemeralNode(String parent, String node) throws Exception {
        return addEphemeralNode(ZKPaths.makePath(parent, node));
    }

    @Override
    public String addEphemeralNode(String path) throws Exception {
        return client.create().withMode(CreateMode.EPHEMERAL).forPath(path);
    }

    @Override
    public String addEphemeralNode(String path, byte[] data) throws Exception {
        return client.create().withMode(CreateMode.EPHEMERAL).forPath(path, data);
    }

    @Override
    public void addPersistentNode(String path) throws Exception {
        try{
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path);
        } catch (NodeExistsException e){
            logger.warn("Node already exists: {}", path);
        } catch (Exception e){
            throw new Exception("addPersistentNode is error", e);
        }

    }

    @Override
    public Stat setData(String path, byte[] data) throws Exception {
        return client.setData().forPath(path, data);
    }

    @Override
    public byte[] getData(String path) throws Exception {
        return client.getData().forPath(path);
    }

    @Override
    public boolean casSetData(String path, byte[] oldData, byte[] newData) throws Exception {
        Stat stat = new Stat();
        byte[] remoteData = client.getData().storingStatIn(stat).forPath(path);
        if(Arrays.equals(oldData, remoteData)){
            try{
                client.setData().withVersion(stat.getVersion()).forPath(path, newData);
                return true;
            }catch (Exception e){
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isConnected() {
        return client.getZookeeperClient().isConnected();
    }

    @Override
    public void addConnectionChangeListenter(ConnectionStateListener listener) {
        if (listener != null){
            client.getConnectionStateListenable().addListener(
                    new org.apache.curator.framework.state.ConnectionStateListener() {
                        @Override
                        public void stateChanged(CuratorFramework client,
                                org.apache.curator.framework.state.ConnectionState state) {
                                listener.stateChanged(ZKClientImpl.this, convertTo(state));
                        }
                    });
        }
    }

    private ConnectionState convertTo(org.apache.curator.framework.state.ConnectionState state) {
        switch (state) {
            case CONNECTED:
                return ConnectionState.CONNECTED;
            case SUSPENDED:
                return ConnectionState.SUSPENDED;
            case RECONNECTED:
                return ConnectionState.RECONNECTED;
            case LOST:
                return ConnectionState.LOST;
            default:
                return ConnectionState.READ_ONLY;
        }
    }

    @Override
    public void deletePath(String path) throws Exception {
        client.delete().forPath(path);
    }

    @Override
    public boolean casDeletePath(String path, byte[] oldData) throws Exception {
        Stat stat = new Stat();
        byte[] remoteData = client.getData().storingStatIn(stat).forPath(path);
        if (Arrays.equals(oldData, remoteData)) {
            try {
                client.delete().withVersion(stat.getVersion()).forPath(path);
                return true;
            } catch (KeeperException.BadVersionException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean checkExist(String path) {
        try {
            return stat(path) != null;
        } catch (Exception e) {
            logger.error("check exist error", e);
            return false;
        }
    }

    @Override
    public Stat stat(String path) throws Exception {
        return client.checkExists().forPath(path);
    }

    @Override
    public Leader createLeader(String leaderPath, String id) {
        ZKLeader leader = new ZKLeader();
        LeaderSelector selector = new LeaderSelector(this.client, leaderPath, leader);
        selector.setId(id);
        leaders.add(leader);
        return leader;
    }

    @Override
    public Observer createObserver(String leaderPath, String id) {
        ZKObserver observer = new ZKObserver();
        LeaderSelector selector = new LeaderSelector(this.client, leaderPath, observer);
        selector.setId(id);
        observer.setSelector(selector);
        observers.add(observer);
        return observer;
    }

    @PreDestroy
    @Override
    public void close() {
        logger.info("Call close of ZKClient, reference count is: {}", REFERENCE_COUNT.get());
        if (REFERENCE_COUNT.decrementAndGet() == 0) {
            for (ZKLeader leader : leaders) {
                leader.destroy();
            }
            for (ZKObserver observer : observers) {
                observer.destroy();
            }
            client.close();
            logger.info("ZKClient is closed");
        }
    }

    private class ConnectionWatcher implements ConnectionStateListener {
        CountDownLatch latch;

        public ConnectionWatcher(CountDownLatch latch) {
            this.latch = latch;
        }


        @Override
        public void stateChanged(ZKClient sender, ConnectionState state) {
            if (state == ConnectionState.CONNECTED){
                latch.countDown();
            }
        }
    }

    protected void incrementReference() {
        REFERENCE_COUNT.incrementAndGet();
    }
}
