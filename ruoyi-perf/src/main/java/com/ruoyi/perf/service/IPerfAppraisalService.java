package com.ruoyi.perf.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.perf.domain.PerfAppraisal;

/**
 * 员工考核单 服务层
 * 
 * @author ruoyi
 */
public interface IPerfAppraisalService
{
    /**
     * 查询考核单列表（不做数据权限过滤，用于我的绩效、绩效评分）
     * 
     * @param appraisal 查询条件
     * @return 考核单集合
     */
    public List<PerfAppraisal> selectAppraisalList(PerfAppraisal appraisal);

    /**
     * 查询考核单列表（按数据权限过滤，用于考核进度、绩效结果）
     * 
     * @param appraisal 查询条件
     * @return 考核单集合
     */
    public List<PerfAppraisal> selectAppraisalScopeList(PerfAppraisal appraisal);

    /**
     * 按绩效等级统计（按数据权限过滤）
     * 
     * @param appraisal 查询条件
     * @return 统计结果
     */
    public List<Map<String, Object>> selectGradeStats(PerfAppraisal appraisal);

    /**
     * 查询考核单（含明细）
     * 
     * @param appraisalId 考核单ID
     * @return 考核单
     */
    public PerfAppraisal selectAppraisalById(Long appraisalId);

    /**
     * 校验当前用户是否可以按数据权限查看考核单
     * 
     * @param appraisalId 考核单ID
     */
    public void checkAppraisalDataScope(Long appraisalId);

    /**
     * 保存或提交员工自评
     * 
     * @param appraisal 自评内容（含明细自评分）
     * @param userId 当前用户
     * @param submit 是否提交
     * @param operName 操作人
     * @return 结果
     */
    public int saveSelfEvaluation(PerfAppraisal appraisal, Long userId, boolean submit, String operName);

    /**
     * 提交上级评分
     * 
     * @param appraisal 评分内容（含明细上级评分）
     * @param userId 当前用户
     * @param operName 操作人
     * @return 结果
     */
    public int submitReview(PerfAppraisal appraisal, Long userId, String operName);

    /**
     * 退回重新自评
     * 
     * @param appraisalId 考核单ID
     * @param reason 退回原因
     * @param userId 当前用户
     * @param operName 操作人
     * @return 结果
     */
    public int rejectAppraisal(Long appraisalId, String reason, Long userId, String operName);

    /**
     * 更换评分人
     * 
     * @param appraisalId 考核单ID
     * @param reviewerId 新评分人
     * @param operName 操作人
     * @return 结果
     */
    public int changeReviewer(Long appraisalId, Long reviewerId, String operName);

    /**
     * 从进行中的计划中移除未完成的考核单
     * 
     * @param appraisalId 考核单ID
     * @return 结果
     */
    public int deleteAppraisalById(Long appraisalId);
}
