package com.hjf.oa.global;

import java.util.ArrayList;
import java.util.List;

public class Constant {

    // 职务
    public static final String POST_STAFF = "员工";
    public static final String POST_FM = "部门经理";
    public static final String POST_GM = "总经理";
    public static final String POST_CASHIER = "财务";
    /**
     * 获得职务列表
     * @return
     */
    public static List<String> getPost() {

        List<String> list = new ArrayList<String>();
        list.add(POST_STAFF);
        list.add(POST_FM);
        list.add(POST_GM);
        list.add(POST_CASHIER);
        return list;
    }

    /**
     * 获得费用类别
     * @return
     */
    public static List<String> getItems() {

        List<String> list = new ArrayList<String>();
        list.add("交通");
        list.add("餐饮");
        list.add("住宿");
        list.add("办公");
        return list;
    }

    // 报销单状态
    public static final String CLAIM_STATUS_CREATED = "新创建";
    public static final String CLAIM_STATUS_SUBMIT = "已提交";
    public static final String CLAIM_STATUS_APPROVED = "已审核";
    public static final String CLAIM_STATUS_BACK = "打回";
    public static final String CLAIM_STATUS_TERMINATED = "终止";
    public static final String CLAIM_STATUS_RECHECK = "待复审";
    public static final String CLAIM_STATUS_PAID = "已打款";

    // 审核额度
    public static final double LIMIT_CHECK = 5000.00;

    // 处理方式
    public static final String DEAL_CREATE = "创建";
    public static final String DEAL_SUBMIT = "提交";
    public static final String DEAL_UPDATE = "修改";
    public static final String DEAL_BACK = "打回";
    public static final String DEAL_REJECT = "拒绝";
    public static final String DEAL_PASS = "通过";
    public static final String DEAL_PAID = "打款";

}
