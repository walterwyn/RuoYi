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
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.perf.domain.PerfIndicator;
import com.ruoyi.perf.service.IPerfIndicatorService;

/**
 * 考核指标 信息操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/perf/indicator")
public class PerfIndicatorController extends BaseController
{
    private String prefix = "perf/indicator";

    @Autowired
    private IPerfIndicatorService indicatorService;

    @RequiresPermissions("perf:indicator:view")
    @GetMapping()
    public String indicator()
    {
        return prefix + "/indicator";
    }

    /**
     * 查询考核指标列表
     */
    @RequiresPermissions("perf:indicator:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PerfIndicator indicator)
    {
        startPage();
        List<PerfIndicator> list = indicatorService.selectIndicatorList(indicator);
        return getDataTable(list);
    }

    /**
     * 导出考核指标列表
     */
    @Log(title = "考核指标", businessType = BusinessType.EXPORT)
    @RequiresPermissions("perf:indicator:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PerfIndicator indicator)
    {
        List<PerfIndicator> list = indicatorService.selectIndicatorList(indicator);
        ExcelUtil<PerfIndicator> util = new ExcelUtil<PerfIndicator>(PerfIndicator.class);
        return util.exportExcel(list, "考核指标数据");
    }

    /**
     * 新增考核指标
     */
    @RequiresPermissions("perf:indicator:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存考核指标
     */
    @RequiresPermissions("perf:indicator:add")
    @Log(title = "考核指标", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated PerfIndicator indicator)
    {
        if (!indicatorService.checkIndicatorNameUnique(indicator))
        {
            return error("新增指标'" + indicator.getIndicatorName() + "'失败，指标名称已存在");
        }
        indicator.setCreateBy(getLoginName());
        return toAjax(indicatorService.insertIndicator(indicator));
    }

    /**
     * 修改考核指标
     */
    @RequiresPermissions("perf:indicator:edit")
    @GetMapping("/edit/{indicatorId}")
    public String edit(@PathVariable("indicatorId") Long indicatorId, ModelMap mmap)
    {
        mmap.put("indicator", indicatorService.selectIndicatorById(indicatorId));
        return prefix + "/edit";
    }

    /**
     * 修改保存考核指标
     */
    @RequiresPermissions("perf:indicator:edit")
    @Log(title = "考核指标", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated PerfIndicator indicator)
    {
        if (!indicatorService.checkIndicatorNameUnique(indicator))
        {
            return error("修改指标'" + indicator.getIndicatorName() + "'失败，指标名称已存在");
        }
        indicator.setUpdateBy(getLoginName());
        return toAjax(indicatorService.updateIndicator(indicator));
    }

    /**
     * 删除考核指标
     */
    @RequiresPermissions("perf:indicator:remove")
    @Log(title = "考核指标", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(indicatorService.deleteIndicatorByIds(ids));
    }

    /**
     * 校验指标名称
     */
    @PostMapping("/checkIndicatorNameUnique")
    @ResponseBody
    public boolean checkIndicatorNameUnique(PerfIndicator indicator)
    {
        return indicatorService.checkIndicatorNameUnique(indicator);
    }
}
