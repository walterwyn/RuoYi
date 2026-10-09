package com.ruoyi.perf.service;

import java.util.List;
import com.ruoyi.perf.domain.PerfTemplate;
import com.ruoyi.perf.domain.PerfTemplateItem;

/**
 * 考核模板 服务层
 * 
 * @author ruoyi
 */
public interface IPerfTemplateService
{
    /**
     * 查询考核模板列表
     * 
     * @param template 考核模板
     * @return 考核模板集合
     */
    public List<PerfTemplate> selectTemplateList(PerfTemplate template);

    /**
     * 查询所有正常状态的考核模板
     * 
     * @return 考核模板集合
     */
    public List<PerfTemplate> selectTemplateNormal();

    /**
     * 通过模板ID查询考核模板（含模板指标）
     * 
     * @param templateId 模板ID
     * @return 考核模板
     */
    public PerfTemplate selectTemplateById(Long templateId);

    /**
     * 查询模板指标
     * 
     * @param templateId 模板ID
     * @return 模板指标集合
     */
    public List<PerfTemplateItem> selectTemplateItems(Long templateId);

    /**
     * 校验模板名称是否唯一
     * 
     * @param template 考核模板
     * @return 结果
     */
    public boolean checkTemplateNameUnique(PerfTemplate template);

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
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTemplateByIds(String ids);
}
