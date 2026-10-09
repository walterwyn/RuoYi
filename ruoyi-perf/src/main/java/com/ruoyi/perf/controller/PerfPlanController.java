package com.ruoyi.perf.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
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
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.perf.domain.PerfAppraisal;
import com.ruoyi.perf.domain.PerfPlan;
import com.ruoyi.perf.service.IPerfAppraisalService;
import com.ruoyi.perf.service.IPerfPlanService;
import com.ruoyi.perf.service.IPerfTemplateService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 考核计划 信息操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/perf/plan")
public class PerfPlanController extends BaseController
{
    private String prefix = "perf/plan";

    @Autowired
    private IPerfPlanService planService;

    @Autowired
    private IPerfTemplateService templateService;

    @Autowired
    private IPerfAppraisalService appraisalService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysUserService userService;

    @RequiresPermissions("perf:plan:view")
    @GetMapping()
    public String plan()
    {
        return prefix + "/plan";
    }

    /**
     * 查询考核计划列表
     */
    @RequiresPermissions("perf:plan:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PerfPlan plan)
    {
        startPage();
        List<PerfPlan> list = planService.selectPlanList(plan);
        return getDataTable(list);
    }

    /**
     * 导出考核计划列表
     */
    @Log(title = "考核计划", businessType = BusinessType.EXPORT)
    @RequiresPermissions("perf:plan:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PerfPlan plan)
    {
        List<PerfPlan> list = planService.selectPlanList(plan);
        ExcelUtil<PerfPlan> util = new ExcelUtil<PerfPlan>(PerfPlan.class);
        return util.exportExcel(list, "考核计划数据");
    }

    /**
     * 新增考核计划
     */
    @RequiresPermissions("perf:plan:add")
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("templates", templateService.selectTemplateNormal());
        return prefix + "/add";
    }

    /**
     * 新增保存考核计划
     */
    @RequiresPermissions("perf:plan:add")
    @Log(title = "考核计划", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated PerfPlan plan)
    {
        if (!planService.checkPlanNameUnique(plan))
        {
            return error("新增计划'" + plan.getPlanName() + "'失败，计划名称已存在");
        }
        plan.setCreateBy(getLoginName());
        return toAjax(planService.insertPlan(plan));
    }

    /**
     * 修改考核计划
     */
    @RequiresPermissions("perf:plan:edit")
    @GetMapping("/edit/{planId}")
    public String edit(@PathVariable("planId") Long planId, ModelMap mmap)
    {
        mmap.put("plan", planService.selectPlanById(planId));
        mmap.put("templates", templateService.selectTemplateNormal());
        return prefix + "/edit";
    }

    /**
     * 修改保存考核计划
     */
    @RequiresPermissions("perf:plan:edit")
    @Log(title = "考核计划", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated PerfPlan plan)
    {
        if (!planService.checkPlanNameUnique(plan))
        {
            return error("修改计划'" + plan.getPlanName() + "'失败，计划名称已存在");
        }
        plan.setUpdateBy(getLoginName());
        return toAjax(planService.updatePlan(plan));
    }

    /**
     * 删除考核计划
     */
    @RequiresPermissions("perf:plan:remove")
    @Log(title = "考核计划", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(planService.deletePlanByIds(ids));
    }

    /**
     * 校验计划名称
     */
    @PostMapping("/checkPlanNameUnique")
    @ResponseBody
    public boolean checkPlanNameUnique(PerfPlan plan)
    {
        return planService.checkPlanNameUnique(plan);
    }

    /**
     * 发布考核计划
     */
    @RequiresPermissions("perf:plan:publish")
    @GetMapping("/publish/{planId}")
    public String publish(@PathVariable("planId") Long planId, ModelMap mmap)
    {
        mmap.put("plan", planService.selectPlanById(planId));
        mmap.put("users", selectNormalUsers());
        mmap.put("loginUserId", getUserId());
        return prefix + "/publish";
    }

    /**
     * 发布保存考核计划
     */
    @RequiresPermissions("perf:plan:publish")
    @Log(title = "考核计划", businessType = BusinessType.GRANT)
    @PostMapping("/publish")
    @ResponseBody
    public AjaxResult publishSave(Long planId, String deptIds, Long reviewerId, Boolean useDeptLeader)
    {
        Long[] deptIdArray = Convert.toLongArray(deptIds);
        for (Long deptId : deptIdArray)
        {
            deptService.checkDeptDataScope(deptId);
        }
        int count = planService.publishPlan(planId, deptIdArray, reviewerId, Boolean.TRUE.equals(useDeptLeader), getLoginName());
        return success("发布成功，已为" + count + "名员工生成考核单");
    }

    /**
     * 加载部门列表树
     */
    @RequiresPermissions("perf:plan:publish")
    @GetMapping("/deptTreeData")
    @ResponseBody
    public List<Ztree> deptTreeData()
    {
        return deptService.selectDeptTree(new SysDept());
    }

    /**
     * 结束考核计划
     */
    @RequiresPermissions("perf:plan:finish")
    @Log(title = "考核计划", businessType = BusinessType.UPDATE)
    @PostMapping("/finish")
    @ResponseBody
    public AjaxResult finish(Long planId)
    {
        return toAjax(planService.finishPlan(planId, getLoginName()));
    }

    /**
     * 考核进度
     */
    @RequiresPermissions("perf:plan:list")
    @GetMapping("/progress/{planId}")
    public String progress(@PathVariable("planId") Long planId, ModelMap mmap)
    {
        mmap.put("plan", planService.selectPlanById(planId));
        return prefix + "/progress";
    }

    /**
     * 查询考核进度列表
     */
    @RequiresPermissions("perf:plan:list")
    @PostMapping("/progress/list")
    @ResponseBody
    public TableDataInfo progressList(PerfAppraisal appraisal)
    {
        startPage();
        List<PerfAppraisal> list = appraisalService.selectAppraisalScopeList(appraisal);
        return getDataTable(list);
    }

    /**
     * 考核单详细
     */
    @RequiresPermissions("perf:plan:list")
    @GetMapping("/appraisal/{appraisalId}")
    public String appraisalDetail(@PathVariable("appraisalId") Long appraisalId, ModelMap mmap)
    {
        appraisalService.checkAppraisalDataScope(appraisalId);
        mmap.put("appraisal", appraisalService.selectAppraisalById(appraisalId));
        return "perf/appraisal/detail";
    }

    /**
     * 更换评分人
     */
    @RequiresPermissions("perf:plan:edit")
    @GetMapping("/changeReviewer/{appraisalId}")
    public String changeReviewer(@PathVariable("appraisalId") Long appraisalId, ModelMap mmap)
    {
        appraisalService.checkAppraisalDataScope(appraisalId);
        mmap.put("appraisal", appraisalService.selectAppraisalById(appraisalId));
        mmap.put("users", selectNormalUsers());
        return prefix + "/changeReviewer";
    }

    /**
     * 保存更换评分人
     */
    @RequiresPermissions("perf:plan:edit")
    @Log(title = "考核计划", businessType = BusinessType.UPDATE)
    @PostMapping("/changeReviewer")
    @ResponseBody
    public AjaxResult changeReviewerSave(Long appraisalId, Long reviewerId)
    {
        appraisalService.checkAppraisalDataScope(appraisalId);
        return toAjax(appraisalService.changeReviewer(appraisalId, reviewerId, getLoginName()));
    }

    /**
     * 移除未完成的考核单
     */
    @RequiresPermissions("perf:plan:edit")
    @Log(title = "考核计划", businessType = BusinessType.DELETE)
    @PostMapping("/removeAppraisal")
    @ResponseBody
    public AjaxResult removeAppraisal(Long appraisalId)
    {
        appraisalService.checkAppraisalDataScope(appraisalId);
        return toAjax(appraisalService.deleteAppraisalById(appraisalId));
    }

    /**
     * 查询正常状态的用户（用于选择评分人）
     */
    private List<SysUser> selectNormalUsers()
    {
        SysUser user = new SysUser();
        user.setStatus("0");
        return userService.selectUserList(user);
    }
}
