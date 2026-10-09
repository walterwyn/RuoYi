package com.ruoyi.perf.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 考核单明细表 perf_appraisal_item
 * 
 * @author ruoyi
 */
public class PerfAppraisalItem implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long itemId;

    /** 考核单ID */
    private Long appraisalId;

    /** 指标ID */
    private Long indicatorId;

    /** 指标名称 */
    private String indicatorName;

    /** 指标类型 */
    private String indicatorType;

    /** 评分标准 */
    private String scoringStandard;

    /** 权重（%） */
    private Integer weight;

    /** 自评分 */
    private BigDecimal selfScore;

    /** 上级评分 */
    private BigDecimal leaderScore;

    /** 显示顺序 */
    private Integer orderNum;

    public Long getItemId()
    {
        return itemId;
    }

    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    public Long getAppraisalId()
    {
        return appraisalId;
    }

    public void setAppraisalId(Long appraisalId)
    {
        this.appraisalId = appraisalId;
    }

    public Long getIndicatorId()
    {
        return indicatorId;
    }

    public void setIndicatorId(Long indicatorId)
    {
        this.indicatorId = indicatorId;
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

    public Integer getWeight()
    {
        return weight;
    }

    public void setWeight(Integer weight)
    {
        this.weight = weight;
    }

    public BigDecimal getSelfScore()
    {
        return selfScore;
    }

    public void setSelfScore(BigDecimal selfScore)
    {
        this.selfScore = selfScore;
    }

    public BigDecimal getLeaderScore()
    {
        return leaderScore;
    }

    public void setLeaderScore(BigDecimal leaderScore)
    {
        this.leaderScore = leaderScore;
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("itemId", getItemId())
            .append("appraisalId", getAppraisalId())
            .append("indicatorId", getIndicatorId())
            .append("indicatorName", getIndicatorName())
            .append("weight", getWeight())
            .append("selfScore", getSelfScore())
            .append("leaderScore", getLeaderScore())
            .toString();
    }
}
