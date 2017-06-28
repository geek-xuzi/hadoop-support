package com.xuen.hadoop.hbase;

import java.io.IOException;
import javax.annotation.Resource;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.stereotype.Component;

/**
 * @author zheng.xu
 * @since 2017-06-23
 */
@Component
public class DefaultHBaseClient implements HBaseClient{

    @Resource
    private HBaseConfiguration hBaseConfiguration;

    @Override
    public void deleteRowKey(String hTableName, String rowKey) throws IOException {
        Connection connection = getConnection();
        HTable hTable = getHtable(hTableName);
        Delete delete = new Delete(rowKey.getBytes());
        hTable.delete(delete);
    }


    @Override
    public Result getResultByRowKey(String hTableName, String rowKey) throws IOException {
        HTable hTable = getHtable(hTableName);
        Get get = new Get(rowKey.getBytes());
        Result result = hTable.get(get);
        return result;
    }


    private Connection getConnection() throws IOException {
        Configuration configuration = hBaseConfiguration.createConfiguration();
        Connection connection = ConnectionFactory.createConnection(configuration);
        return connection;
    }

    private HTable getHtable(String hTableName) throws IOException {
        Connection connection = getConnection();
        HTable hTable = (HTable) connection.getTable(TableName.valueOf(hTableName));
        return hTable;
    }

}
