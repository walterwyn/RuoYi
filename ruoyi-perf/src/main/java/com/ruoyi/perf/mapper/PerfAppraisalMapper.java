package com.ruoyi.perf.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.perf.domain.PerfAppraisal;
import com.ruoyi.perf.domain.PerfAppraisalItem;

/**
 * 员工考核单 数据层
 * 
 * @author ruoyi
 */
public interface PerfAppraisalMapper
{
    /**
     * 查询考核单列表
     * 
     * @param appraisal 查询条件
     * @return 考核单集合
     */
    public List<PerfAppraisal> selectAppraisalList(PerfAppraisal appraisal);

    /**
     * 按绩效等级统计人数与平均分
     * 
     * @param appraisal 查询条件
     * @return 统计结果
     */
    public List<Map<String, Object>> selectGradeStats(PerfAppraisal appraisal);

    /**
     * 通过考核单ID查询考核单
     * 
     * @param appraisalId 考核单ID
     * @return 考核单
     */
    public PerfAppraisal selectAppraisalById(Long appraisalId);

    /**
     * 查询考核单明细
     * 
     * @param appraisalId 考核单ID
     * @return 明细集合
     */
    public List<PerfAppraisalItem> selectAppraisalItems(Long appraisalId);

    /**
     * 统计计划中未完成的考核单数量
     * 
     * @param planId 计划ID
     * @return 结果
     */
    public int countUnfinishedByPlanId(Long planId);

    /**
     * 查询发布计划时的参与人员
     * 
     * @param deptIds 部门ID
     * @return 参与人员（userId、deptId、部门负责人reviewerId）
     */
    public List<PerfAppraisal> selectPublishCandidates(Long[] deptIds);

    /**
     * 新增考核单
     * 
     * @param appraisal 考核单
     * @return 结果
     */
    public int insertAppraisal(PerfAppraisal appraisal);

    /**
     * 批量新增考核单明细
     * 
     * @param items 明细
     * @return 结果
     */
    public int batchAppraisalItem(List<PerfAppraisalItem> items);

    /**
     * 更新明细自评分
     * 
     * @param item 明细
     * @return 结果
     */
    public int updateItemSelfScore(PerfAppraisalItem item);

    /**
     * 更新明细上级评分
     * 
     * @param item 明细
     * @return 结果
     */
    public int updateItemLeaderScore(PerfAppraisalItem item);

    /**
     * 保存自评
     * 
     * @param appraisal 考核单
     * @return 结果
     */
    public int updateSelfEvaluation(PerfAppraisal appraisal);

    /**
     * 提交上级评分
     * 
     * @param appraisal 考核单
     * @return 结果
     */
    public int updateReview(PerfAppraisal appraisal);

    /**
     * 退回重新自评
     * 
     * @param appraisal 考核单
     * @return 结果
     */
    public int updateReject(PerfAppraisal appraisal);

    /**
     * 更换评分人
     * 
     * @param appraisal 考核单
     * @return 结果
     */
    public int updateReviewer(PerfAppraisal appraisal);

    /**
     * 删除未完成的考核单
     * 
     * @param appraisalId 考核单ID
     * @return 结果
     */
    public int deleteAppraisalById(Long appraisalId);

    /**
     * 删除考核单明细
     * 
     * @param appraisalId 考核单ID
     * @return 结果
     */
    public int deleteAppraisalItemByAppraisalId(Long appraisalId);
}
