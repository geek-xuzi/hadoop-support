package com.xuen.hadoop.zookeeper.leader;

/**
 * User: zhaohuiyu
 * Date: 5/21/13
 * Time: 2:32 PM
 * <p/>
 * 用于leader选举
 */
public interface Leader {

    /**
     * 获取自己的id
     *
     * @return id
     */
    String getMyId();

    /**
     * 获取当前leader的id
     *
     * @return leader id
     * @throws Exception
     */
    String getLeaderId() throws Exception;

    /**
     * 注册监听leader切换的事件
     *
     * @param listener
     */
    void addListener(LeaderChangedListener listener);

    /**
     * 开始选举
     */
    void start();
}
