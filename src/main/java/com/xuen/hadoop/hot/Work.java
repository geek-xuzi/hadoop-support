package com.xuen.hadoop.hot;

/**
 * @author zheng.xu
 * @since 2017-06-29
 */
public class Work {

    /**
     * 工单编号
     */
    private String workId;

    /**
     * 工单类别
     */
    private WorkType workType;

    /**
     * 工单状态
     */
    private WorkStatus workStatus;


    /**
     * 工单的详细信息
     */
    private WorkInfo workInfo;

    /**
     * 工单的发起机构
     */
    private String organize;

    /**
     * 工单的转派人
     */
    private String[] sends;

    /**
     * 工单的验证人
     */
    private String confirm;
    /**
     * 工单的操作记录
     */
    private String commit;
}
