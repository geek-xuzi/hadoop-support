package com.xuen.hadoop.zookeeper;

import java.util.List;
import org.apache.zookeeper.data.Stat;

/**
 * @author zheng.xu
 * @since 2017-07-10
 */
public interface ZKClient {

    /**
     * 获取给定路径的子节点
     *
     * @param path
     * @return
     * @throws Exception
     */
    List<String> getChildren(String path) throws Exception;

    /**
     * 监听给定路径子节点变化，并且获取子节点集合
     *
     * @param parent   父路径
     * @param listener 监听器
     * @param sync     该方法是否同步获取子节点，如果为true，
     *                 则方法返回的时候一定获得最新子节点集合
     * @return
     * @throws Exception
     */
    List<String> listenChildrenPath(final String parent, final NodeListener listener, final boolean sync) throws Exception;
    /**
     * 在给定路径下添加临时子节点
     *
     * @param parent
     * @param node
     * @return
     * @throws Exception
     */
    String addEphemeralNode(String parent, String node) throws Exception;
    /**
     * 添加临时节点
     *
     * @param path 临时节点全路径
     * @return
     * @throws Exception
     */
    String addEphemeralNode(String path) throws Exception;
    String addEphemeralNode(String path, byte[] data) throws Exception;
    /**
     * 添加永久节点
     *
     * @param path 永久节点全路径
     * @throws Exception
     */
    void addPersistentNode(String path) throws Exception;

    /**
     * 给节点设置数据
     *
     * @param path
     * @param data
     * @return
     * @throws Exception
     */
    Stat setData(String path, byte[] data) throws Exception;

    /**
     * 获取数据
     */
    byte[] getData(String path) throws Exception;

    boolean casSetData(String path, byte[] oldData, byte[] newData) throws Exception;
    /**
     * 是否已经连接上
     *
     * @return
     */
    boolean isConnected();

    /**
     * 注册连接状态监听器
     *
     * @param listener
     */
    void addConnectionChangeListenter(final ConnectionStateListener listener);

    /**
     * 删除给定路径
     *
     * @param path
     * @throws Exception
     */
    void deletePath(String path) throws Exception;
    boolean casDeletePath(String path, byte[] oldData) throws Exception;

    /**
     * 判断给定路径是否存在
     *
     * @param path
     * @return
     */
    boolean checkExist(String path);

    /**
     * 获取给定路径的元数据信息，如果该路径不存在则返回null
     *
     * @param path
     * @return
     */
    Stat stat(String path) throws Exception;

    /**
     * leader选举，创建一个集群
     *
     * @param leaderPath
     * @param id
     * @return
     */
    com.xuen.hadoop.zookeeper.leader.Leader createLeader(String leaderPath, String id);

    /**
     * 创建集群感知的观察者
     *
     * @param leaderPath
     * @param id
     * @return
     */
    com.xuen.hadoop.zookeeper.leader.Observer createObserver(String leaderPath, String id);

    /**
     * 关闭
     */
    void close();
}
