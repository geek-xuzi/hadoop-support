package com.xuen.hadoop.hdfs;

import java.io.File;
import java.io.IOException;
import javax.annotation.Resource;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Component;

/**
 * @author zheng.xu
 * @since 2017-06-06
 */
@Component
public class DefaultHDFSClient implements HDFSClient {

    @Resource
    private ConfigurationFactory configurationFactory;

    @Override
    public void deleteFile(String path) throws IOException {
        FileSystem fs = createFs();
        fs.delete(new Path(path), true);
    }

    @Override
    public void copyLocalFile(String localPath, String hdfsPath, boolean delSrc, boolean overwrite)
            throws IOException {
        FileSystem fs = createFs();
        fs.copyFromLocalFile(delSrc, overwrite, new Path(localPath), new Path(hdfsPath));

    }

    @Override
    public FileStatus[] listFile(String hdfsPath) throws IOException {
        FileSystem fs = createFs();
        FileStatus[] fileStatuses = fs.listStatus(new Path(hdfsPath));
        return fs.listStatus(new Path(hdfsPath));
    }

    @Override
    public void copyHdfsFileToLocal(String hdfsPath, File localFile) throws IOException {
        FileSystem fs = createFs();
        fs.copyToLocalFile(new Path(hdfsPath), new Path(localFile.getAbsolutePath()));
    }


    private FileSystem createFs() throws IOException {
        Configuration configuration = configurationFactory.createConfiguration();
        // 内部有池化机制
        return FileSystem.get(configuration);
    }


}
