package com.ruoyi.perf.util;

/**
 * 绩效管理常量
 * 
 * @author ruoyi
 */
public class PerfConstants
{
    /** 计划状态：未发布 */
    public static final String PLAN_DRAFT = "0";

    /** 计划状态：进行中 */
    public static final String PLAN_RUNNING = "1";

    /** 计划状态：已结束 */
    public static final String PLAN_FINISHED = "2";

    /** 考核单状态：待自评 */
    public static final String APPRAISAL_SELF = "0";

    /** 考核单状态：待评分 */
    public static final String APPRAISAL_REVIEW = "1";

    /** 考核单状态：已完成 */
    public static final String APPRAISAL_DONE = "2";

    /** 指标/模板状态：正常 */
    public static final String NORMAL = "0";

    /** 模板权重合计 */
    public static final int TOTAL_WEIGHT = 100;

    /** 单项最高分 */
    public static final int MAX_SCORE = 100;
}
