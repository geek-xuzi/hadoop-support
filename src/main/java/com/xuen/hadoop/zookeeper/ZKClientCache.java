package com.xuen.hadoop.zookeeper;

import com.beust.jcommander.internal.Maps;
import java.util.Map;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zheng.xu
 * @since 2017-07-10
 */
public class ZKClientCache {
    private static final Logger logger = LoggerFactory.getLogger(ZKClientCache.class);

    private static final Map<String, ZKClientImpl> CACHE = Maps.newHashMap();

    public synchronized static ZKClient get(String address){
        ZKClientImpl client = CACHE.get(address);
        if (client == null){
            CACHE.put(address, new ZKClientImpl(address));
        }
        client = CACHE.get(address);
        client.incrementReference();
        return client;

    }

    public static ZKClient getFixed(String address){
        return get(address);
    }

}
