package com.xuen.hadoop.hbase;

import java.io.IOException;
import javax.annotation.Resource;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.shell.find.Result;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTable;
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
    public void deleteRowKey(String tableName, String rowKey) throws IOException {
        HTable hTable =  getHtable(tableName);
        Delete delete = new Delete(rowKey.getBytes());
        hTable.delete(delete);
    }

    private HTable getHtable(String tableName) throws IOException {
        Configuration configuration = hBaseConfiguration.createConfiguration();
        HTable hTable = new HTable(configuration, tableName);
        return hTable;
    }

    @Override
    public Result getValueByRowKey(String tableName, String rowKey) {
        return null;
    }


}
