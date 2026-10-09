package com.ruoyi.perf.service;

import java.util.List;
import com.ruoyi.perf.domain.PerfIndicator;

/**
 * 考核指标 服务层
 * 
 * @author ruoyi
 */
public interface IPerfIndicatorService
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
     * @param indicator 考核指标
     * @return 结果
     */
    public boolean checkIndicatorNameUnique(PerfIndicator indicator);

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
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteIndicatorByIds(String ids);
}
