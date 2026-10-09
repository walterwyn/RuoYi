package com.ruoyi.perf.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.perf.domain.PerfAppraisal;
import com.ruoyi.perf.service.IPerfAppraisalService;
import com.ruoyi.perf.util.PerfConstants;

/**
 * 绩效评分 信息操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/perf/review")
public class PerfReviewController extends BaseController
{
    private String prefix = "perf/review";

    @Autowired
    private IPerfAppraisalService appraisalService;

    @RequiresPermissions("perf:review:view")
    @GetMapping()
    public String review()
    {
        return prefix + "/review";
    }

    /**
     * 查询指派给本人评分的考核单（超级管理员可查看全部）
     */
    @RequiresPermissions("perf:review:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PerfAppraisal appraisal)
    {
        startPage();
        if (!ShiroUtils.isAdmin())
        {
            appraisal.setReviewerId(getUserId());
        }
        List<PerfAppraisal> list = appraisalService.selectAppraisalList(appraisal);
        return getDataTable(list);
    }

    /**
     * 上级评分
     */
    @RequiresPermissions("perf:review:score")
    @GetMapping("/score/{appraisalId}")
    public String score(@PathVariable("appraisalId") Long appraisalId, ModelMap mmap)
    {
        PerfAppraisal appraisal = selectReviewAppraisal(appraisalId);
        mmap.put("appraisal", appraisal);
        if (!PerfConstants.PLAN_RUNNING.equals(appraisal.getPlanStatus()) || !PerfConstants.APPRAISAL_REVIEW.equals(appraisal.getAppraisalStatus()))
        {
            return "perf/appraisal/detail";
        }
        return prefix + "/score";
    }

    /**
     * 提交上级评分
     */
    @RequiresPermissions("perf:review:score")
    @Log(title = "绩效评分", businessType = BusinessType.UPDATE)
    @PostMapping("/score")
    @ResponseBody
    public AjaxResult scoreSave(PerfAppraisal appraisal)
    {
        appraisalService.submitReview(appraisal, getUserId(), getLoginName());
        return success("评分已提交");
    }

    /**
     * 退回重新自评
     */
    @RequiresPermissions("perf:review:score")
    @Log(title = "绩效评分", businessType = BusinessType.UPDATE)
    @PostMapping("/reject")
    @ResponseBody
    public AjaxResult reject(Long appraisalId, String reason)
    {
        appraisalService.rejectAppraisal(appraisalId, reason, getUserId(), getLoginName());
        return success("已退回员工重新自评");
    }

    /**
     * 考核单详细
     */
    @RequiresPermissions("perf:review:list")
    @GetMapping("/detail/{appraisalId}")
    public String detail(@PathVariable("appraisalId") Long appraisalId, ModelMap mmap)
    {
        mmap.put("appraisal", selectReviewAppraisal(appraisalId));
        return "perf/appraisal/detail";
    }

    private PerfAppraisal selectReviewAppraisal(Long appraisalId)
    {
        PerfAppraisal appraisal = appraisalService.selectAppraisalById(appraisalId);
        if (appraisal == null || !(getUserId().equals(appraisal.getReviewerId()) || ShiroUtils.isAdmin()))
        {
            throw new ServiceException("考核单不存在或评分人不是当前用户");
        }
        return appraisal;
    }
}
