package com.ruoyi.perf.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.perf.domain.PerfIndicator;
import com.ruoyi.perf.mapper.PerfIndicatorMapper;
import com.ruoyi.perf.service.IPerfIndicatorService;

/**
 * 考核指标 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class PerfIndicatorServiceImpl implements IPerfIndicatorService
{
    @Autowired
    private PerfIndicatorMapper indicatorMapper;

    /**
     * 查询考核指标列表
     * 
     * @param indicator 考核指标
     * @return 考核指标集合
     */
    @Override
    public List<PerfIndicator> selectIndicatorList(PerfIndicator indicator)
    {
        return indicatorMapper.selectIndicatorList(indicator);
    }

    /**
     * 通过指标ID查询考核指标
     * 
     * @param indicatorId 指标ID
     * @return 考核指标
     */
    @Override
    public PerfIndicator selectIndicatorById(Long indicatorId)
    {
        return indicatorMapper.selectIndicatorById(indicatorId);
    }

    /**
     * 校验指标名称是否唯一
     * 
     * @param indicator 考核指标
     * @return 结果
     */
    @Override
    public boolean checkIndicatorNameUnique(PerfIndicator indicator)
    {
        Long indicatorId = StringUtils.isNull(indicator.getIndicatorId()) ? -1L : indicator.getIndicatorId();
        PerfIndicator info = indicatorMapper.checkIndicatorNameUnique(indicator.getIndicatorName());
        if (StringUtils.isNotNull(info) && info.getIndicatorId().longValue() != indicatorId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增考核指标
     * 
     * @param indicator 考核指标
     * @return 结果
     */
    @Override
    public int insertIndicator(PerfIndicator indicator)
    {
        return indicatorMapper.insertIndicator(indicator);
    }

    /**
     * 修改考核指标
     * 
     * @param indicator 考核指标
     * @return 结果
     */
    @Override
    public int updateIndicator(PerfIndicator indicator)
    {
        return indicatorMapper.updateIndicator(indicator);
    }

    /**
     * 批量删除考核指标
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteIndicatorByIds(String ids)
    {
        Long[] indicatorIds = Convert.toLongArray(ids);
        if (indicatorMapper.countTemplateItemByIndicatorIds(indicatorIds) > 0)
        {
            throw new ServiceException("指标已被考核模板引用，不能删除，可将其停用");
        }
        return indicatorMapper.deleteIndicatorByIds(indicatorIds);
    }
}
