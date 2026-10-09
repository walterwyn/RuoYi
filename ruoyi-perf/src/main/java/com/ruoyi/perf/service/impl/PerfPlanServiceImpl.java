package com.ruoyi.perf.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.perf.domain.PerfAppraisal;
import com.ruoyi.perf.domain.PerfAppraisalItem;
import com.ruoyi.perf.domain.PerfPlan;
import com.ruoyi.perf.domain.PerfTemplate;
import com.ruoyi.perf.domain.PerfTemplateItem;
import com.ruoyi.perf.mapper.PerfAppraisalMapper;
import com.ruoyi.perf.mapper.PerfPlanMapper;
import com.ruoyi.perf.mapper.PerfTemplateMapper;
import com.ruoyi.perf.service.IPerfPlanService;
import com.ruoyi.perf.util.PerfConstants;
import com.ruoyi.system.service.ISysUserService;

/**
 * 考核计划 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class PerfPlanServiceImpl implements IPerfPlanService
{
    @Autowired
    private PerfPlanMapper planMapper;

    @Autowired
    private PerfTemplateMapper templateMapper;

    @Autowired
    private PerfAppraisalMapper appraisalMapper;

    @Autowired
    private ISysUserService userService;

    /**
     * 查询考核计划列表
     * 
     * @param plan 考核计划
     * @return 考核计划集合
     */
    @Override
    public List<PerfPlan> selectPlanList(PerfPlan plan)
    {
        return planMapper.selectPlanList(plan);
    }

    /**
     * 通过计划ID查询考核计划
     * 
     * @param planId 计划ID
     * @return 考核计划
     */
    @Override
    public PerfPlan selectPlanById(Long planId)
    {
        return planMapper.selectPlanById(planId);
    }

    /**
     * 校验计划名称是否唯一
     * 
     * @param plan 考核计划
     * @return 结果
     */
    @Override
    public boolean checkPlanNameUnique(PerfPlan plan)
    {
        Long planId = StringUtils.isNull(plan.getPlanId()) ? -1L : plan.getPlanId();
        PerfPlan info = planMapper.checkPlanNameUnique(plan.getPlanName());
        if (StringUtils.isNotNull(info) && info.getPlanId().longValue() != planId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增考核计划
     * 
     * @param plan 考核计划
     * @return 结果
     */
    @Override
    public int insertPlan(PerfPlan plan)
    {
        checkPlan(plan);
        return planMapper.insertPlan(plan);
    }

    /**
     * 修改考核计划
     * 
     * @param plan 考核计划
     * @return 结果
     */
    @Override
    public int updatePlan(PerfPlan plan)
    {
        checkPlan(plan);
        int rows = planMapper.updatePlan(plan);
        if (rows == 0)
        {
            throw new ServiceException("只有未发布的考核计划才能修改");
        }
        return rows;
    }

    /**
     * 批量删除考核计划
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deletePlanByIds(String ids)
    {
        Long[] planIds = Convert.toLongArray(ids);
        for (Long planId : planIds)
        {
            PerfPlan plan = planMapper.selectPlanById(planId);
            if (plan != null && !PerfConstants.PLAN_DRAFT.equals(plan.getPlanStatus()))
            {
                throw new ServiceException("考核计划「" + plan.getPlanName() + "」已发布，不能删除");
            }
        }
        return planMapper.deletePlanByIds(planIds);
    }

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
    @Override
    @Transactional
    public int publishPlan(Long planId, Long[] deptIds, Long reviewerId, boolean useDeptLeader, String operName)
    {
        PerfPlan plan = planMapper.selectPlanById(planId);
        if (plan == null)
        {
            throw new ServiceException("考核计划不存在");
        }
        if (!PerfConstants.PLAN_DRAFT.equals(plan.getPlanStatus()))
        {
            throw new ServiceException("考核计划已发布，请勿重复发布");
        }
        if (deptIds == null || deptIds.length == 0)
        {
            throw new ServiceException("请选择参与考核的部门");
        }
        SysUser reviewer = reviewerId == null ? null : userService.selectUserById(reviewerId);
        if (reviewer == null || !UserConstants.NORMAL.equals(reviewer.getStatus()) || "2".equals(reviewer.getDelFlag()))
        {
            throw new ServiceException("请选择有效的默认评分人");
        }
        PerfTemplate template = templateMapper.selectTemplateById(plan.getTemplateId());
        if (template == null || !PerfConstants.NORMAL.equals(template.getStatus()))
        {
            throw new ServiceException("考核模板不存在或已停用，请先修改计划的考核模板");
        }
        List<PerfTemplateItem> templateItems = templateMapper.selectTemplateItems(template.getTemplateId());
        if (templateItems.isEmpty())
        {
            throw new ServiceException("考核模板「" + template.getTemplateName() + "」没有配置考核指标");
        }
        List<PerfAppraisal> candidates = appraisalMapper.selectPublishCandidates(deptIds);
        if (candidates.isEmpty())
        {
            throw new ServiceException("所选部门下没有在职员工");
        }
        if (planMapper.updatePlanStatus(planId, PerfConstants.PLAN_DRAFT, PerfConstants.PLAN_RUNNING, operName) == 0)
        {
            throw new ServiceException("考核计划已发布，请勿重复发布");
        }
        for (PerfAppraisal candidate : candidates)
        {
            PerfAppraisal appraisal = new PerfAppraisal();
            appraisal.setPlanId(planId);
            appraisal.setUserId(candidate.getUserId());
            appraisal.setDeptId(candidate.getDeptId());
            Long leaderId = candidate.getReviewerId();
            boolean leaderValid = useDeptLeader && leaderId != null && !leaderId.equals(candidate.getUserId());
            appraisal.setReviewerId(leaderValid ? leaderId : reviewerId);
            appraisal.setCreateBy(operName);
            appraisalMapper.insertAppraisal(appraisal);

            List<PerfAppraisalItem> items = new ArrayList<PerfAppraisalItem>();
            for (PerfTemplateItem templateItem : templateItems)
            {
                PerfAppraisalItem item = new PerfAppraisalItem();
                item.setAppraisalId(appraisal.getAppraisalId());
                item.setIndicatorId(templateItem.getIndicatorId());
                item.setIndicatorName(templateItem.getIndicatorName());
                item.setIndicatorType(templateItem.getIndicatorType());
                item.setScoringStandard(templateItem.getScoringStandard());
                item.setWeight(templateItem.getWeight());
                item.setOrderNum(templateItem.getOrderNum());
                items.add(item);
            }
            appraisalMapper.batchAppraisalItem(items);
        }
        return candidates.size();
    }

    /**
     * 结束考核计划
     * 
     * @param planId 计划ID
     * @param operName 操作人
     * @return 结果
     */
    @Override
    public int finishPlan(Long planId, String operName)
    {
        PerfPlan plan = planMapper.selectPlanById(planId);
        if (plan == null || !PerfConstants.PLAN_RUNNING.equals(plan.getPlanStatus()))
        {
            throw new ServiceException("只有进行中的考核计划才能结束");
        }
        int unfinished = appraisalMapper.countUnfinishedByPlanId(planId);
        if (unfinished > 0)
        {
            throw new ServiceException("还有" + unfinished + "份考核单未完成，请在考核进度中跟进或移除后再结束考核");
        }
        int rows = planMapper.updatePlanStatus(planId, PerfConstants.PLAN_RUNNING, PerfConstants.PLAN_FINISHED, operName);
        if (rows == 0)
        {
            throw new ServiceException("考核计划状态已变化，请刷新后重试");
        }
        return rows;
    }

    /**
     * 校验计划日期与模板
     * 
     * @param plan 考核计划
     */
    private void checkPlan(PerfPlan plan)
    {
        if (plan.getStartDate() != null && plan.getEndDate() != null && plan.getStartDate().after(plan.getEndDate()))
        {
            throw new ServiceException("考核开始日期不能晚于结束日期");
        }
        PerfTemplate template = templateMapper.selectTemplateById(plan.getTemplateId());
        if (template == null || !PerfConstants.NORMAL.equals(template.getStatus()))
        {
            throw new ServiceException("请选择正常状态的考核模板");
        }
    }
}
