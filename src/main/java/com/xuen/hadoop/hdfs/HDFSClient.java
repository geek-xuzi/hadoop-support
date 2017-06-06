package com.xuen.hadoop.hdfs;

import java.io.File;
import java.io.IOException;
import org.apache.hadoop.fs.FileStatus;

/**
 * @author zheng.xu
 * @since 2017-06-06
 */
public interface HDFSClient {

    /**
     * 删除指定路径下的文件
     *
     * @param path 指定路径
     * @throws IOException
     */
    void deleteFile(String path) throws IOException;

    /**
     * 上传文件
     *
     * @param localPath 本地文件路径
     * @param hdfsPath hdfs文件路径
     */
    void copyLocalFile(String localPath, String hdfsPath, boolean delSrc, boolean overwrite)
            throws IOException;

    /**
     * 列出 hdfs 指定目录的文件
     *
     * @param hdfsPath
     * @return
     * @throws IOException
     */
    FileStatus[] listFile(String hdfsPath) throws IOException;


    void copyHdfsFileToLocal(String hdfsPath,File localFile) throws IOException;
}
