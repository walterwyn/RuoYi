package com.ruoyi.perf.mapper;

import java.util.List;
import com.ruoyi.perf.domain.PerfIndicator;

/**
 * 考核指标 数据层
 * 
 * @author ruoyi
 */
public interface PerfIndicatorMapper
{
    /**
     * 查询考核指标列表
     * 
     * @param indicator 考核指标
     * @return 考核指标集合
     */
    public List<PerfIndicator> selectIndicatorList(PerfIndicator indicator);

    /**
     * 通过指标ID查询考核指标
     * 
     * @param indicatorId 指标ID
     * @return 考核指标
     */
    public PerfIndicator selectIndicatorById(Long indicatorId);

    /**
     * 校验指标名称是否唯一
     * 
     * @param indicatorName 指标名称
     * @return 结果
     */
    public PerfIndicator checkIndicatorNameUnique(String indicatorName);

    /**
     * 统计引用指标的模板数量
     * 
     * @param indicatorIds 指标ID
     * @return 结果
     */
    public int countTemplateItemByIndicatorIds(Long[] indicatorIds);

    /**
     * 新增考核指标
     * 
     * @param indicator 考核指标
     * @return 结果
     */
    public int insertIndicator(PerfIndicator indicator);

    /**
     * 修改考核指标
     * 
     * @param indicator 考核指标
     * @return 结果
     */
    public int updateIndicator(PerfIndicator indicator);

    /**
     * 批量删除考核指标
     * 
     * @param indicatorIds 需要删除的指标ID
     * @return 结果
     */
    public int deleteIndicatorByIds(Long[] indicatorIds);
}
