package com.xuen.hadoop.hot;

/**
 * @author zheng.xu
 * @since 2017-06-29
 */
public enum WorkStatus {

    ALLOCATION("带分配"),
    ALLOCATIONING("分配中"),
    HANDLEING("处理中"),
    HANDLED("已经处理");


    WorkStatus(String s) {

    }
}
