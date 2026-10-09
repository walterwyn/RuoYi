package com.ruoyi.perf.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.perf.domain.PerfPlan;

/**
 * 考核计划 数据层
 * 
 * @author ruoyi
 */
public interface PerfPlanMapper
{
    /**
     * 查询考核计划列表
     * 
     * @param plan 考核计划
     * @return 考核计划集合
     */
    public List<PerfPlan> selectPlanList(PerfPlan plan);

    /**
     * 通过计划ID查询考核计划
     * 
     * @param planId 计划ID
     * @return 考核计划
     */
    public PerfPlan selectPlanById(Long planId);

    /**
     * 校验计划名称是否唯一
     * 
     * @param planName 计划名称
     * @return 结果
     */
    public PerfPlan checkPlanNameUnique(String planName);

    /**
     * 新增考核计划
     * 
     * @param plan 考核计划
     * @return 结果
     */
    public int insertPlan(PerfPlan plan);

    /**
     * 修改考核计划（仅未发布）
     * 
     * @param plan 考核计划
     * @return 结果
     */
    public int updatePlan(PerfPlan plan);

    /**
     * 变更计划状态
     * 
     * @param planId 计划ID
     * @param oldStatus 原状态
     * @param newStatus 新状态
     * @param updateBy 更新者
     * @return 结果
     */
    public int updatePlanStatus(@Param("planId") Long planId, @Param("oldStatus") String oldStatus, @Param("newStatus") String newStatus, @Param("updateBy") String updateBy);

    /**
     * 批量删除考核计划（仅未发布）
     * 
     * @param planIds 需要删除的计划ID
     * @return 结果
     */
    public int deletePlanByIds(Long[] planIds);
}
