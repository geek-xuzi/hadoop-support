package com.xuen.hadoop.hive;

import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zheng.xu
 * @since 2017-06-28
 */
@Component
public class DefaultHiveClient implements HiveClient {

    @Resource(name = "hiveJdbcTemplate")
    private JdbcTemplate hiveJdbcTemplate;

    @Override
    public void load2Hive(String hdfsPath, String hiveTableName,String partitionArgsInfo) {
        String sql = String.format(HiveConstants.LOAD_DATA_TO_HIVE_BASE_SQL, hdfsPath, hiveTableName)
                +  partitionArgsInfo;
        hiveJdbcTemplate.execute(sql);
    }

    @Override
    public void loadLocal2Hive(String path, String hiveTableName,String partitionArgsInfo) {
        String sql = String.format(HiveConstants.LOAD_LOCAL_DATA_TO_HIVE_BASE_SQL,path , hiveTableName)
                + partitionArgsInfo;
        hiveJdbcTemplate.execute(sql);

    }


}
