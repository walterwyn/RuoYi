package com.ruoyi.perf.domain;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 考核模板指标表 perf_template_item
 * 
 * @author ruoyi
 */
public class PerfTemplateItem implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long itemId;

    /** 模板ID */
    private Long templateId;

    /** 指标ID */
    private Long indicatorId;

    /** 权重（%） */
    private Integer weight;

    /** 显示顺序 */
    private Integer orderNum;

    /** 指标名称 */
    private String indicatorName;

    /** 指标类型 */
    private String indicatorType;

    /** 评分标准 */
    private String scoringStandard;

    public Long getItemId()
    {
        return itemId;
    }

    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    public Long getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(Long templateId)
    {
        this.templateId = templateId;
    }

    public Long getIndicatorId()
    {
        return indicatorId;
    }

    public void setIndicatorId(Long indicatorId)
    {
        this.indicatorId = indicatorId;
    }

    public Integer getWeight()
    {
        return weight;
    }

    public void setWeight(Integer weight)
    {
        this.weight = weight;
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public String getIndicatorName()
    {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName)
    {
        this.indicatorName = indicatorName;
    }

    public String getIndicatorType()
    {
        return indicatorType;
    }

    public void setIndicatorType(String indicatorType)
    {
        this.indicatorType = indicatorType;
    }

    public String getScoringStandard()
    {
        return scoringStandard;
    }

    public void setScoringStandard(String scoringStandard)
    {
        this.scoringStandard = scoringStandard;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("itemId", getItemId())
            .append("templateId", getTemplateId())
            .append("indicatorId", getIndicatorId())
            .append("weight", getWeight())
            .append("orderNum", getOrderNum())
            .toString();
    }
}
