package com.ruoyi.perf.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.Logical;
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
import com.ruoyi.perf.domain.PerfIndicator;
import com.ruoyi.perf.domain.PerfTemplate;
import com.ruoyi.perf.service.IPerfIndicatorService;
import com.ruoyi.perf.service.IPerfTemplateService;
import com.ruoyi.perf.util.PerfConstants;

/**
 * 考核模板 信息操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/perf/template")
public class PerfTemplateController extends BaseController
{
    private String prefix = "perf/template";

    @Autowired
    private IPerfTemplateService templateService;

    @Autowired
    private IPerfIndicatorService indicatorService;

    @RequiresPermissions("perf:template:view")
    @GetMapping()
    public String template()
    {
        return prefix + "/template";
    }

    /**
     * 查询考核模板列表
     */
    @RequiresPermissions("perf:template:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PerfTemplate template)
    {
        startPage();
        List<PerfTemplate> list = templateService.selectTemplateList(template);
        return getDataTable(list);
    }

    /**
     * 考核模板详细
     */
    @RequiresPermissions("perf:template:list")
    @GetMapping("/detail/{templateId}")
    public String detail(@PathVariable("templateId") Long templateId, ModelMap mmap)
    {
        mmap.put("template", templateService.selectTemplateById(templateId));
        return prefix + "/detail";
    }

    /**
     * 新增考核模板
     */
    @RequiresPermissions("perf:template:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存考核模板
     */
    @RequiresPermissions("perf:template:add")
    @Log(title = "考核模板", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated PerfTemplate template)
    {
        if (!templateService.checkTemplateNameUnique(template))
        {
            return error("新增模板'" + template.getTemplateName() + "'失败，模板名称已存在");
        }
        template.setCreateBy(getLoginName());
        return toAjax(templateService.insertTemplate(template));
    }

    /**
     * 修改考核模板
     */
    @RequiresPermissions("perf:template:edit")
    @GetMapping("/edit/{templateId}")
    public String edit(@PathVariable("templateId") Long templateId, ModelMap mmap)
    {
        mmap.put("template", templateService.selectTemplateById(templateId));
        return prefix + "/edit";
    }

    /**
     * 修改保存考核模板
     */
    @RequiresPermissions("perf:template:edit")
    @Log(title = "考核模板", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated PerfTemplate template)
    {
        if (!templateService.checkTemplateNameUnique(template))
        {
            return error("修改模板'" + template.getTemplateName() + "'失败，模板名称已存在");
        }
        template.setUpdateBy(getLoginName());
        return toAjax(templateService.updateTemplate(template));
    }

    /**
     * 删除考核模板
     */
    @RequiresPermissions("perf:template:remove")
    @Log(title = "考核模板", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(templateService.deleteTemplateByIds(ids));
    }

    /**
     * 校验模板名称
     */
    @PostMapping("/checkTemplateNameUnique")
    @ResponseBody
    public boolean checkTemplateNameUnique(PerfTemplate template)
    {
        return templateService.checkTemplateNameUnique(template);
    }

    /**
     * 选择考核指标
     */
    @RequiresPermissions(value = { "perf:template:add", "perf:template:edit" }, logical = Logical.OR)
    @GetMapping("/selectIndicator")
    public String selectIndicator()
    {
        return prefix + "/selectIndicator";
    }

    /**
     * 查询可选的考核指标（仅正常状态）
     */
    @RequiresPermissions(value = { "perf:template:add", "perf:template:edit" }, logical = Logical.OR)
    @PostMapping("/indicatorList")
    @ResponseBody
    public TableDataInfo indicatorList(PerfIndicator indicator)
    {
        startPage();
        indicator.setStatus(PerfConstants.NORMAL);
        List<PerfIndicator> list = indicatorService.selectIndicatorList(indicator);
        return getDataTable(list);
    }
}
