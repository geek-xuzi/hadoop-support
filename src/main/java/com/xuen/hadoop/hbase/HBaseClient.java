package com.xuen.hadoop.hbase;

import java.io.IOException;
import org.apache.hadoop.hbase.client.Result;

/**
 * @author zheng.xu
 * @since 2017-06-23
 */
public interface HBaseClient {

//    void createTable(String tableName,String Family);
//
//    void dropTable(String tableName);

    void deleteRowKey(String tableName,String rowKey) throws IOException;

    Result getResultByRowKey(String tableName,String rowKey) throws IOException;
}
