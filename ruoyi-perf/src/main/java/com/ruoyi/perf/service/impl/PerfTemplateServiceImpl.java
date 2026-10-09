package com.ruoyi.perf.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.perf.domain.PerfIndicator;
import com.ruoyi.perf.domain.PerfTemplate;
import com.ruoyi.perf.domain.PerfTemplateItem;
import com.ruoyi.perf.mapper.PerfIndicatorMapper;
import com.ruoyi.perf.mapper.PerfTemplateMapper;
import com.ruoyi.perf.service.IPerfTemplateService;
import com.ruoyi.perf.util.PerfConstants;

/**
 * 考核模板 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class PerfTemplateServiceImpl implements IPerfTemplateService
{
    @Autowired
    private PerfTemplateMapper templateMapper;

    @Autowired
    private PerfIndicatorMapper indicatorMapper;

    /**
     * 查询考核模板列表
     * 
     * @param template 考核模板
     * @return 考核模板集合
     */
    @Override
    public List<PerfTemplate> selectTemplateList(PerfTemplate template)
    {
        return templateMapper.selectTemplateList(template);
    }

    /**
     * 查询所有正常状态的考核模板
     * 
     * @return 考核模板集合
     */
    @Override
    public List<PerfTemplate> selectTemplateNormal()
    {
        PerfTemplate template = new PerfTemplate();
        template.setStatus(PerfConstants.NORMAL);
        return templateMapper.selectTemplateList(template);
    }

    /**
     * 通过模板ID查询考核模板（含模板指标）
     * 
     * @param templateId 模板ID
     * @return 考核模板
     */
    @Override
    public PerfTemplate selectTemplateById(Long templateId)
    {
        PerfTemplate template = templateMapper.selectTemplateById(templateId);
        if (StringUtils.isNotNull(template))
        {
            template.setItems(templateMapper.selectTemplateItems(templateId));
        }
        return template;
    }

    /**
     * 查询模板指标
     * 
     * @param templateId 模板ID
     * @return 模板指标集合
     */
    @Override
    public List<PerfTemplateItem> selectTemplateItems(Long templateId)
    {
        return templateMapper.selectTemplateItems(templateId);
    }

    /**
     * 校验模板名称是否唯一
     * 
     * @param template 考核模板
     * @return 结果
     */
    @Override
    public boolean checkTemplateNameUnique(PerfTemplate template)
    {
        Long templateId = StringUtils.isNull(template.getTemplateId()) ? -1L : template.getTemplateId();
        PerfTemplate info = templateMapper.checkTemplateNameUnique(template.getTemplateName());
        if (StringUtils.isNotNull(info) && info.getTemplateId().longValue() != templateId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增考核模板
     * 
     * @param template 考核模板
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTemplate(PerfTemplate template)
    {
        List<PerfTemplateItem> items = checkTemplateItems(template.getItems());
        int rows = templateMapper.insertTemplate(template);
        insertTemplateItems(template.getTemplateId(), items);
        return rows;
    }

    /**
     * 修改考核模板
     * 
     * @param template 考核模板
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTemplate(PerfTemplate template)
    {
        List<PerfTemplateItem> items = checkTemplateItems(template.getItems());
        templateMapper.deleteTemplateItemByTemplateIds(new Long[] { template.getTemplateId() });
        insertTemplateItems(template.getTemplateId(), items);
        return templateMapper.updateTemplate(template);
    }

    /**
     * 批量删除考核模板
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTemplateByIds(String ids)
    {
        Long[] templateIds = Convert.toLongArray(ids);
        if (templateMapper.countPlanByTemplateIds(templateIds) > 0)
        {
            throw new ServiceException("模板已被考核计划使用，不能删除，可将其停用");
        }
        templateMapper.deleteTemplateItemByTemplateIds(templateIds);
        return templateMapper.deleteTemplateByIds(templateIds);
    }

    /**
     * 校验模板指标：至少一项、指标存在且不重复、权重为1-100的整数且合计为100
     * 
     * @param items 模板指标
     * @return 过滤空行后的模板指标
     */
    private List<PerfTemplateItem> checkTemplateItems(List<PerfTemplateItem> items)
    {
        List<PerfTemplateItem> result = new ArrayList<PerfTemplateItem>();
        if (items != null)
        {
            for (PerfTemplateItem item : items)
            {
                if (item != null && item.getIndicatorId() != null)
                {
                    result.add(item);
                }
            }
        }
        if (result.isEmpty())
        {
            throw new ServiceException("请至少添加一个考核指标");
        }
        Set<Long> indicatorIds = new HashSet<Long>();
        int totalWeight = 0;
        for (PerfTemplateItem item : result)
        {
            PerfIndicator indicator = indicatorMapper.selectIndicatorById(item.getIndicatorId());
            if (indicator == null)
            {
                throw new ServiceException("考核指标不存在或已被删除");
            }
            if (!indicatorIds.add(item.getIndicatorId()))
            {
                throw new ServiceException("考核指标「" + indicator.getIndicatorName() + "」重复添加");
            }
            if (item.getWeight() == null || item.getWeight() <= 0 || item.getWeight() > PerfConstants.TOTAL_WEIGHT)
            {
                throw new ServiceException("考核指标「" + indicator.getIndicatorName() + "」的权重必须是1到100之间的整数");
            }
            totalWeight += item.getWeight();
        }
        if (totalWeight != PerfConstants.TOTAL_WEIGHT)
        {
            throw new ServiceException("指标权重合计必须为100%，当前为" + totalWeight + "%");
        }
        return result;
    }

    /**
     * 保存模板指标
     * 
     * @param templateId 模板ID
     * @param items 模板指标
     */
    private void insertTemplateItems(Long templateId, List<PerfTemplateItem> items)
    {
        int orderNum = 1;
        for (PerfTemplateItem item : items)
        {
            item.setTemplateId(templateId);
            item.setOrderNum(orderNum++);
        }
        templateMapper.batchTemplateItem(items);
    }
}
