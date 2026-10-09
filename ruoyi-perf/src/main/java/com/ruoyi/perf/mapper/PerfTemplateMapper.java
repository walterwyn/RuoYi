package com.ruoyi.perf.mapper;

import java.util.List;
import com.ruoyi.perf.domain.PerfTemplate;
import com.ruoyi.perf.domain.PerfTemplateItem;

/**
 * 考核模板 数据层
 * 
 * @author ruoyi
 */
public interface PerfTemplateMapper
{
    /**
     * 查询考核模板列表
     * 
     * @param template 考核模板
     * @return 考核模板集合
     */
    public List<PerfTemplate> selectTemplateList(PerfTemplate template);

    /**
     * 通过模板ID查询考核模板
     * 
     * @param templateId 模板ID
     * @return 考核模板
     */
    public PerfTemplate selectTemplateById(Long templateId);

    /**
     * 校验模板名称是否唯一
     * 
     * @param templateName 模板名称
     * @return 结果
     */
    public PerfTemplate checkTemplateNameUnique(String templateName);

    /**
     * 查询模板指标
     * 
     * @param templateId 模板ID
     * @return 模板指标集合
     */
    public List<PerfTemplateItem> selectTemplateItems(Long templateId);

    /**
     * 统计引用模板的考核计划数量
     * 
     * @param templateIds 模板ID
     * @return 结果
     */
    public int countPlanByTemplateIds(Long[] templateIds);

    /**
     * 新增考核模板
     * 
     * @param template 考核模板
     * @return 结果
     */
    public int insertTemplate(PerfTemplate template);

    /**
     * 修改考核模板
     * 
     * @param template 考核模板
     * @return 结果
     */
    public int updateTemplate(PerfTemplate template);

    /**
     * 批量删除考核模板
     * 
     * @param templateIds 需要删除的模板ID
     * @return 结果
     */
    public int deleteTemplateByIds(Long[] templateIds);

    /**
     * 批量删除模板指标
     * 
     * @param templateIds 模板ID
     * @return 结果
     */
    public int deleteTemplateItemByTemplateIds(Long[] templateIds);

    /**
     * 批量新增模板指标
     * 
     * @param items 模板指标
     * @return 结果
     */
    public int batchTemplateItem(List<PerfTemplateItem> items);
}
