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
import com.ruoyi.perf.domain.PerfAppraisal;
import com.ruoyi.perf.service.IPerfAppraisalService;
import com.ruoyi.perf.util.PerfConstants;

/**
 * 我的绩效 信息操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/perf/self")
public class PerfSelfController extends BaseController
{
    private String prefix = "perf/self";

    @Autowired
    private IPerfAppraisalService appraisalService;

    @RequiresPermissions("perf:self:view")
    @GetMapping()
    public String self()
    {
        return prefix + "/self";
    }

    /**
     * 查询本人考核单列表（不含未发布的计划）
     */
    @RequiresPermissions("perf:self:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PerfAppraisal appraisal)
    {
        startPage();
        appraisal.setUserId(getUserId());
        List<PerfAppraisal> list = appraisalService.selectAppraisalList(appraisal);
        return getDataTable(list);
    }

    /**
     * 员工自评
     */
    @RequiresPermissions("perf:self:evaluate")
    @GetMapping("/evaluate/{appraisalId}")
    public String evaluate(@PathVariable("appraisalId") Long appraisalId, ModelMap mmap)
    {
        PerfAppraisal appraisal = selectOwnAppraisal(appraisalId);
        mmap.put("appraisal", appraisal);
        if (!PerfConstants.PLAN_RUNNING.equals(appraisal.getPlanStatus()) || !PerfConstants.APPRAISAL_SELF.equals(appraisal.getAppraisalStatus()))
        {
            return "perf/appraisal/detail";
        }
        return prefix + "/evaluate";
    }

    /**
     * 暂存自评
     */
    @RequiresPermissions("perf:self:evaluate")
    @Log(title = "员工自评", businessType = BusinessType.UPDATE)
    @PostMapping("/save")
    @ResponseBody
    public AjaxResult save(PerfAppraisal appraisal)
    {
        appraisalService.saveSelfEvaluation(appraisal, getUserId(), false, getLoginName());
        return success("暂存成功");
    }

    /**
     * 提交自评
     */
    @RequiresPermissions("perf:self:evaluate")
    @Log(title = "员工自评", businessType = BusinessType.UPDATE)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxResult submit(PerfAppraisal appraisal)
    {
        appraisalService.saveSelfEvaluation(appraisal, getUserId(), true, getLoginName());
        return success("自评已提交，等待上级评分");
    }

    /**
     * 本人考核单详细
     */
    @RequiresPermissions("perf:self:list")
    @GetMapping("/detail/{appraisalId}")
    public String detail(@PathVariable("appraisalId") Long appraisalId, ModelMap mmap)
    {
        mmap.put("appraisal", selectOwnAppraisal(appraisalId));
        return "perf/appraisal/detail";
    }

    private PerfAppraisal selectOwnAppraisal(Long appraisalId)
    {
        PerfAppraisal appraisal = appraisalService.selectAppraisalById(appraisalId);
        if (appraisal == null || !getUserId().equals(appraisal.getUserId()))
        {
            throw new ServiceException("考核单不存在或不属于当前用户");
        }
        return appraisal;
    }
}
