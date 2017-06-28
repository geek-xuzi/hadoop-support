package com.xuen.hadoop.zookeeper;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionStateListener;

/**
 * @author zheng.xu
 * @since 2017-06-08
 */
public class DefaultZkClient implements ZkClient {

    private CuratorFramework curatorFramework;

    public DefaultZkClient(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    public DefaultZkClient() {
    }

    ;

    @Override
    public void registerListeners(ConnectionStateListener listener) {
        curatorFramework.getConnectionStateListenable().addListener(listener);
    }


    @Override
    public void createForPath(String path, byte[] data) throws Exception {
        String[] split = path.split("/");
        String path1 = "/";
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (!StringUtils.isEmpty(s)) {
                path1 += s;
                if (i == split.length - 1) {
                    curatorFramework.create().forPath(path1);
                    curatorFramework.setData().forPath(path1, data);
                } else {
                    curatorFramework.create().forPath(path1);
                    path1 += "/";
                }
            }
        }
    }

    @Override
    public void createForPath(String path) throws Exception {
        String[] split = path.split("/");
        String path1 = "/";
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (!StringUtils.isEmpty(s)) {
                path1 += s;
                if (i != split.length - 1) {
                    path1 += "/";
                }
                curatorFramework.create().forPath(path1);
            }
        }
    }

}
