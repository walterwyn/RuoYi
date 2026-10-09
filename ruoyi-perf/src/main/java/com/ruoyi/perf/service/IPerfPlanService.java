package com.ruoyi.perf.service;

import java.util.List;
import com.ruoyi.perf.domain.PerfPlan;

/**
 * 考核计划 服务层
 * 
 * @author ruoyi
 */
public interface IPerfPlanService
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
     * @param plan 考核计划
     * @return 结果
     */
    public boolean checkPlanNameUnique(PerfPlan plan);

    /**
     * 新增考核计划
     * 
     * @param plan 考核计划
     * @return 结果
     */
    public int insertPlan(PerfPlan plan);

    /**
     * 修改考核计划
     * 
     * @param plan 考核计划
     * @return 结果
     */
    public int updatePlan(PerfPlan plan);

    /**
     * 批量删除考核计划
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePlanByIds(String ids);

    /**
     * 发布考核计划，为参与部门的在职员工生成考核单
     * 
     * @param planId 计划ID
     * @param deptIds 参与部门ID
     * @param reviewerId 默认评分人
     * @param useDeptLeader 是否优先由部门负责人评分
     * @param operName 操作人
     * @return 生成的考核单数量
     */
    public int publishPlan(Long planId, Long[] deptIds, Long reviewerId, boolean useDeptLeader, String operName);

    /**
     * 结束考核计划
     * 
     * @param planId 计划ID
     * @param operName 操作人
     * @return 结果
     */
    public int finishPlan(Long planId, String operName);
}
