package com.ruoyi.perf.controller;

import java.util.List;
import java.util.Map;
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
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.perf.domain.PerfAppraisal;
import com.ruoyi.perf.domain.PerfPlan;
import com.ruoyi.perf.service.IPerfAppraisalService;
import com.ruoyi.perf.service.IPerfPlanService;
import com.ruoyi.perf.util.PerfConstants;
import com.ruoyi.system.service.ISysDeptService;

/**
 * 绩效结果 信息操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/perf/result")
public class PerfResultController extends BaseController
{
    private String prefix = "perf/result";

    @Autowired
    private IPerfAppraisalService appraisalService;

    @Autowired
    private IPerfPlanService planService;

    @Autowired
    private ISysDeptService deptService;

    @RequiresPermissions("perf:result:view")
    @GetMapping()
    public String result(ModelMap mmap)
    {
        List<PerfPlan> plans = planService.selectPlanList(new PerfPlan());
        plans.removeIf(plan -> PerfConstants.PLAN_DRAFT.equals(plan.getPlanStatus()));
        mmap.put("plans", plans);
        return prefix + "/result";
    }

    /**
     * 查询已完成的考核结果
     */
    @RequiresPermissions("perf:result:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PerfAppraisal appraisal)
    {
        startPage();
        appraisal.setAppraisalStatus(PerfConstants.APPRAISAL_DONE);
        List<PerfAppraisal> list = appraisalService.selectAppraisalScopeList(appraisal);
        return getDataTable(list);
    }

    /**
     * 绩效等级分布统计
     */
    @RequiresPermissions("perf:result:list")
    @PostMapping("/stats")
    @ResponseBody
    public AjaxResult stats(PerfAppraisal appraisal)
    {
        appraisal.setAppraisalStatus(PerfConstants.APPRAISAL_DONE);
        List<Map<String, Object>> stats = appraisalService.selectGradeStats(appraisal);
        return success(stats);
    }

    /**
     * 导出考核结果
     */
    @Log(title = "绩效结果", businessType = BusinessType.EXPORT)
    @RequiresPermissions("perf:result:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PerfAppraisal appraisal)
    {
        appraisal.setAppraisalStatus(PerfConstants.APPRAISAL_DONE);
        List<PerfAppraisal> list = appraisalService.selectAppraisalScopeList(appraisal);
        ExcelUtil<PerfAppraisal> util = new ExcelUtil<PerfAppraisal>(PerfAppraisal.class);
        return util.exportExcel(list, "绩效结果数据");
    }

    /**
     * 加载部门列表树
     */
    @RequiresPermissions("perf:result:list")
    @GetMapping("/deptTreeData")
    @ResponseBody
    public List<Ztree> deptTreeData()
    {
        return deptService.selectDeptTree(new SysDept());
    }

    /**
     * 考核结果详细
     */
    @RequiresPermissions("perf:result:list")
    @GetMapping("/detail/{appraisalId}")
    public String detail(@PathVariable("appraisalId") Long appraisalId, ModelMap mmap)
    {
        appraisalService.checkAppraisalDataScope(appraisalId);
        mmap.put("appraisal", appraisalService.selectAppraisalById(appraisalId));
        return "perf/appraisal/detail";
    }
}
