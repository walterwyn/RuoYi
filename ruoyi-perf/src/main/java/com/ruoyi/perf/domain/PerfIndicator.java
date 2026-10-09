package com.ruoyi.perf.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 考核指标表 perf_indicator
 * 
 * @author ruoyi
 */
public class PerfIndicator extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 指标ID */
    @Excel(name = "指标编号", cellType = ColumnType.NUMERIC)
    private Long indicatorId;

    /** 指标名称 */
    @Excel(name = "指标名称")
    private String indicatorName;

    /** 指标类型（1业绩指标 2能力指标 3态度指标） */
    @Excel(name = "指标类型", dictType = "perf_indicator_type")
    private String indicatorType;

    /** 评分标准 */
    @Excel(name = "评分标准")
    private String scoringStandard;

    /** 显示顺序 */
    @Excel(name = "显示顺序", cellType = ColumnType.NUMERIC)
    private Integer orderNum;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public Long getIndicatorId()
    {
        return indicatorId;
    }

    public void setIndicatorId(Long indicatorId)
    {
        this.indicatorId = indicatorId;
    }

    @NotBlank(message = "指标名称不能为空")
    @Size(min = 0, max = 100, message = "指标名称长度不能超过100个字符")
    public String getIndicatorName()
    {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName)
    {
        this.indicatorName = indicatorName;
    }

    @NotBlank(message = "指标类型不能为空")
    public String getIndicatorType()
    {
        return indicatorType;
    }

    public void setIndicatorType(String indicatorType)
    {
        this.indicatorType = indicatorType;
    }

    @Size(min = 0, max = 500, message = "评分标准长度不能超过500个字符")
    public String getScoringStandard()
    {
        return scoringStandard;
    }

    public void setScoringStandard(String scoringStandard)
    {
        this.scoringStandard = scoringStandard;
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("indicatorId", getIndicatorId())
            .append("indicatorName", getIndicatorName())
            .append("indicatorType", getIndicatorType())
            .append("scoringStandard", getScoringStandard())
            .append("orderNum", getOrderNum())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
