package com.xuen.hadoop.hot;

import java.lang.instrument.Instrumentation;

/**
 * @author zheng.xu
 * @since 2017-06-23
 */
public interface FixByByteBuddy {

    /**
     * 通过Pid获得对应JVM的Instrumentation实例
     * @return
     */
    Instrumentation getInstrumentationByPid(String pid);

    /**
     * 加载Agent
     * @param pid 进程号
     * @param jarfilePath  jar文件
     */
    void attach(String pid,String jarfilePath);


    /**
     * 获取当前进程的Instrumentation实例
     * @return
     */
    Instrumentation getInstrumentation();
}
