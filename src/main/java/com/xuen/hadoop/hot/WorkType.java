package com.xuen.hadoop.hot;

/**
 * @author zheng.xu
 * @since 2017-06-29
 */
public enum WorkType {
    ZIZHUJI("自助机报修"),WENTI("问题反馈");

    private String typeInfo;

    WorkType(String s) {
        this.typeInfo = s;
    }
}
