package com.ruoyi.perf.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.perf.domain.PerfAppraisal;
import com.ruoyi.perf.domain.PerfAppraisalItem;
import com.ruoyi.perf.mapper.PerfAppraisalMapper;
import com.ruoyi.perf.service.IPerfAppraisalService;
import com.ruoyi.perf.util.PerfConstants;
import com.ruoyi.perf.util.PerfScoreUtils;
import com.ruoyi.system.service.ISysUserService;

/**
 * 员工考核单 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class PerfAppraisalServiceImpl implements IPerfAppraisalService
{
    /** 评语最大长度 */
    private static final int MAX_COMMENT_LENGTH = 1000;

    @Autowired
    private PerfAppraisalMapper appraisalMapper;

    @Autowired
    private ISysUserService userService;

    /**
     * 查询考核单列表（不做数据权限过滤，用于我的绩效、绩效评分）
     * 
     * @param appraisal 查询条件
     * @return 考核单集合
     */
    @Override
    public List<PerfAppraisal> selectAppraisalList(PerfAppraisal appraisal)
    {
        return appraisalMapper.selectAppraisalList(appraisal);
    }

    /**
     * 查询考核单列表（按数据权限过滤，用于考核进度、绩效结果）
     * 
     * @param appraisal 查询条件
     * @return 考核单集合
     */
    @Override
    @DataScope(deptAlias = "a", userAlias = "a")
    public List<PerfAppraisal> selectAppraisalScopeList(PerfAppraisal appraisal)
    {
        return appraisalMapper.selectAppraisalList(appraisal);
    }

    /**
     * 按绩效等级统计（按数据权限过滤）
     * 
     * @param appraisal 查询条件
     * @return 统计结果
     */
    @Override
    @DataScope(deptAlias = "a", userAlias = "a")
    public List<Map<String, Object>> selectGradeStats(PerfAppraisal appraisal)
    {
        return appraisalMapper.selectGradeStats(appraisal);
    }

    /**
     * 查询考核单（含明细）
     * 
     * @param appraisalId 考核单ID
     * @return 考核单
     */
    @Override
    public PerfAppraisal selectAppraisalById(Long appraisalId)
    {
        PerfAppraisal appraisal = appraisalMapper.selectAppraisalById(appraisalId);
        if (StringUtils.isNotNull(appraisal))
        {
            appraisal.setItems(appraisalMapper.selectAppraisalItems(appraisalId));
        }
        return appraisal;
    }

    /**
     * 校验当前用户是否可以按数据权限查看考核单
     * 
     * @param appraisalId 考核单ID
     */
    @Override
    public void checkAppraisalDataScope(Long appraisalId)
    {
        if (!ShiroUtils.isAdmin())
        {
            PerfAppraisal query = new PerfAppraisal();
            query.setAppraisalId(appraisalId);
            List<PerfAppraisal> list = SpringUtils.getAopProxy(this).selectAppraisalScopeList(query);
            if (StringUtils.isEmpty(list))
            {
                throw new ServiceException("没有权限访问考核数据！");
            }
        }
    }

    /**
     * 保存或提交员工自评
     * 
     * @param form 自评内容（含明细自评分）
     * @param userId 当前用户
     * @param submit 是否提交
     * @param operName 操作人
     * @return 结果
     */
    @Override
    @Transactional
    public int saveSelfEvaluation(PerfAppraisal form, Long userId, boolean submit, String operName)
    {
        PerfAppraisal appraisal = selectAppraisalById(form.getAppraisalId());
        if (appraisal == null || !userId.equals(appraisal.getUserId()))
        {
            throw new ServiceException("考核单不存在或不属于当前用户");
        }
        checkPlanRunning(appraisal);
        if (!PerfConstants.APPRAISAL_SELF.equals(appraisal.getAppraisalStatus()))
        {
            throw new ServiceException("自评已提交，不能再修改");
        }
        checkCommentLength(form.getSelfComment(), "自评总结");
        Map<Long, PerfAppraisalItem> submitted = toItemMap(form.getItems());
        for (PerfAppraisalItem item : appraisal.getItems())
        {
            PerfAppraisalItem input = submitted.get(item.getItemId());
            BigDecimal score = input == null ? null : input.getSelfScore();
            if (submit || score != null)
            {
                PerfScoreUtils.checkScore(score, item.getIndicatorName());
            }
            item.setSelfScore(score);
            appraisalMapper.updateItemSelfScore(item);
        }
        if (submit && StringUtils.isBlank(form.getSelfComment()))
        {
            throw new ServiceException("请填写自评总结");
        }
        PerfAppraisal update = new PerfAppraisal();
        update.setAppraisalId(appraisal.getAppraisalId());
        update.setSelfComment(StringUtils.trim(form.getSelfComment()));
        update.setSelfScore(PerfScoreUtils.weightedScore(appraisal.getItems(), PerfAppraisalItem::getSelfScore));
        update.setAppraisalStatus(submit ? PerfConstants.APPRAISAL_REVIEW : null);
        update.setUpdateBy(operName);
        int rows = appraisalMapper.updateSelfEvaluation(update);
        if (rows == 0)
        {
            throw new ServiceException("考核单状态已变化，请刷新后重试");
        }
        return rows;
    }

    /**
     * 提交上级评分
     * 
     * @param form 评分内容（含明细上级评分）
     * @param userId 当前用户
     * @param operName 操作人
     * @return 结果
     */
    @Override
    @Transactional
    public int submitReview(PerfAppraisal form, Long userId, String operName)
    {
        PerfAppraisal appraisal = selectReviewAppraisal(form.getAppraisalId(), userId);
        checkCommentLength(form.getLeaderComment(), "评语");
        if (StringUtils.isBlank(form.getLeaderComment()))
        {
            throw new ServiceException("请填写评语");
        }
        Map<Long, PerfAppraisalItem> submitted = toItemMap(form.getItems());
        for (PerfAppraisalItem item : appraisal.getItems())
        {
            PerfAppraisalItem input = submitted.get(item.getItemId());
            BigDecimal score = input == null ? null : input.getLeaderScore();
            PerfScoreUtils.checkScore(score, item.getIndicatorName());
            item.setLeaderScore(score);
            appraisalMapper.updateItemLeaderScore(item);
        }
        BigDecimal leaderScore = PerfScoreUtils.weightedScore(appraisal.getItems(), PerfAppraisalItem::getLeaderScore);
        BigDecimal finalScore = PerfScoreUtils.finalScore(appraisal.getSelfScore(), leaderScore, appraisal.getSelfWeight());
        PerfAppraisal update = new PerfAppraisal();
        update.setAppraisalId(appraisal.getAppraisalId());
        update.setLeaderComment(StringUtils.trim(form.getLeaderComment()));
        update.setLeaderScore(leaderScore);
        update.setFinalScore(finalScore);
        update.setGrade(PerfScoreUtils.grade(finalScore));
        update.setUpdateBy(operName);
        int rows = appraisalMapper.updateReview(update);
        if (rows == 0)
        {
            throw new ServiceException("考核单状态已变化，请刷新后重试");
        }
        return rows;
    }

    /**
     * 退回重新自评
     * 
     * @param appraisalId 考核单ID
     * @param reason 退回原因
     * @param userId 当前用户
     * @param operName 操作人
     * @return 结果
     */
    @Override
    public int rejectAppraisal(Long appraisalId, String reason, Long userId, String operName)
    {
        selectReviewAppraisal(appraisalId, userId);
        if (StringUtils.isBlank(reason))
        {
            throw new ServiceException("请填写退回原因");
        }
        checkCommentLength(reason, "退回原因");
        PerfAppraisal update = new PerfAppraisal();
        update.setAppraisalId(appraisalId);
        update.setLeaderComment("【退回】" + StringUtils.trim(reason));
        update.setUpdateBy(operName);
        int rows = appraisalMapper.updateReject(update);
        if (rows == 0)
        {
            throw new ServiceException("考核单状态已变化，请刷新后重试");
        }
        return rows;
    }

    /**
     * 更换评分人
     * 
     * @param appraisalId 考核单ID
     * @param reviewerId 新评分人
     * @param operName 操作人
     * @return 结果
     */
    @Override
    public int changeReviewer(Long appraisalId, Long reviewerId, String operName)
    {
        PerfAppraisal appraisal = appraisalMapper.selectAppraisalById(appraisalId);
        if (appraisal == null)
        {
            throw new ServiceException("考核单不存在");
        }
        checkPlanRunning(appraisal);
        if (PerfConstants.APPRAISAL_DONE.equals(appraisal.getAppraisalStatus()))
        {
            throw new ServiceException("考核已完成，不能更换评分人");
        }
        SysUser reviewer = reviewerId == null ? null : userService.selectUserById(reviewerId);
        if (reviewer == null || !UserConstants.NORMAL.equals(reviewer.getStatus()) || "2".equals(reviewer.getDelFlag()))
        {
            throw new ServiceException("请选择有效的评分人");
        }
        PerfAppraisal update = new PerfAppraisal();
        update.setAppraisalId(appraisalId);
        update.setReviewerId(reviewerId);
        update.setUpdateBy(operName);
        int rows = appraisalMapper.updateReviewer(update);
        if (rows == 0)
        {
            throw new ServiceException("考核单状态已变化，请刷新后重试");
        }
        return rows;
    }

    /**
     * 从进行中的计划中移除未完成的考核单
     * 
     * @param appraisalId 考核单ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteAppraisalById(Long appraisalId)
    {
        PerfAppraisal appraisal = appraisalMapper.selectAppraisalById(appraisalId);
        if (appraisal == null)
        {
            throw new ServiceException("考核单不存在");
        }
        checkPlanRunning(appraisal);
        if (PerfConstants.APPRAISAL_DONE.equals(appraisal.getAppraisalStatus()))
        {
            throw new ServiceException("考核已完成，不能移除");
        }
        int rows = appraisalMapper.deleteAppraisalById(appraisalId);
        if (rows == 0)
        {
            throw new ServiceException("考核单状态已变化，请刷新后重试");
        }
        appraisalMapper.deleteAppraisalItemByAppraisalId(appraisalId);
        return rows;
    }

    /**
     * 查询待当前用户评分的考核单
     * 
     * @param appraisalId 考核单ID
     * @param userId 当前用户
     * @return 考核单
     */
    private PerfAppraisal selectReviewAppraisal(Long appraisalId, Long userId)
    {
        PerfAppraisal appraisal = selectAppraisalById(appraisalId);
        if (appraisal == null || !(userId.equals(appraisal.getReviewerId()) || ShiroUtils.isAdmin(userId)))
        {
            throw new ServiceException("考核单不存在或评分人不是当前用户");
        }
        checkPlanRunning(appraisal);
        if (!PerfConstants.APPRAISAL_REVIEW.equals(appraisal.getAppraisalStatus()))
        {
            throw new ServiceException("该考核单当前不是待评分状态");
        }
        return appraisal;
    }

    /**
     * 校验所属计划是否进行中
     * 
     * @param appraisal 考核单
     */
    private void checkPlanRunning(PerfAppraisal appraisal)
    {
        if (!PerfConstants.PLAN_RUNNING.equals(appraisal.getPlanStatus()))
        {
            throw new ServiceException("考核计划「" + appraisal.getPlanName() + "」不在进行中，不能修改考核单");
        }
    }

    /**
     * 校验评语长度
     * 
     * @param comment 评语
     * @param label 名称
     */
    private void checkCommentLength(String comment, String label)
    {
        if (comment != null && comment.length() > MAX_COMMENT_LENGTH)
        {
            throw new ServiceException(label + "长度不能超过" + MAX_COMMENT_LENGTH + "个字符");
        }
    }

    /**
     * 将提交的明细按ID索引
     * 
     * @param items 明细
     * @return 索引
     */
    private Map<Long, PerfAppraisalItem> toItemMap(List<PerfAppraisalItem> items)
    {
        Map<Long, PerfAppraisalItem> map = new HashMap<Long, PerfAppraisalItem>();
        if (items != null)
        {
            for (PerfAppraisalItem item : items)
            {
                if (item != null && item.getItemId() != null)
                {
                    map.put(item.getItemId(), item);
                }
            }
        }
        return map;
    }
}
