package com.xuen.hadoop.hive;

/**
 * @author zheng.xu
 * @since 2017-06-28
 */
public interface HiveClient {

    void load2Hive(String hdfsPath,String hiveTableName,String partitionArgsInfo);

    void loadLocal2Hive(String Path,String hiveTableName,String partitionArgsInfo);
}
