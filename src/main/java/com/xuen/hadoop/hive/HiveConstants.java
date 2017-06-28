package com.xuen.hadoop.hive;

import java.io.File;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

/**
 * @author zheng.xu
 * @since 2017-06-28
 */
public class HiveConstants {
    public static final String LOG_DIR;

    static {
        String baseDir = System.getProperty("catalina.base");
        if (StringUtils.isEmpty(baseDir)) {
            baseDir = SystemUtils.JAVA_IO_TMPDIR;
        }
        LOG_DIR = baseDir + File.separator + "logs";
    }

    public static final String DEFAULT_FS = "fs.defaultFS";
    public static final String LOAD_DATA_TO_HIVE_BASE_SQL = "load data inpath \'%s\' overwrite into table %s ";
    public static final String PARTITION_SQL = "partition (source=\'%s\',day=\'%s\')";
    public static final String LOAD_LOCAL_DATA_TO_HIVE_BASE_SQL = "load data local inpath \'%s\' overwrite into table %s ";
    public static final String HIVE_ORDER_TABLE = "mts_order";

}
