package com.ruoyi.perf.domain;

import java.util.Date;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 考核计划表 perf_plan
 * 
 * @author ruoyi
 */
public class PerfPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 计划ID */
    @Excel(name = "计划编号", cellType = ColumnType.NUMERIC)
    private Long planId;

    /** 计划名称 */
    @Excel(name = "计划名称")
    private String planName;

    /** 周期类型（1月度 2季度 3半年度 4年度） */
    @Excel(name = "考核周期", dictType = "perf_cycle_type")
    private String cycleType;

    /** 考核开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "开始日期", width = 20, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 考核结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "结束日期", width = 20, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 考核模板ID */
    private Long templateId;

    /** 考核模板名称 */
    @Excel(name = "考核模板")
    private String templateName;

    /** 自评权重（%） */
    @Excel(name = "自评权重", suffix = "%", cellType = ColumnType.NUMERIC)
    private Integer selfWeight;

    /** 计划状态（0未发布 1进行中 2已结束） */
    @Excel(name = "计划状态", dictType = "perf_plan_status")
    private String planStatus;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;

    /** 参与人数 */
    @Excel(name = "参与人数", cellType = ColumnType.NUMERIC)
    private Integer totalCount;

    /** 已完成人数 */
    @Excel(name = "已完成人数", cellType = ColumnType.NUMERIC)
    private Integer finishedCount;

    public Long getPlanId()
    {
        return planId;
    }

    public void setPlanId(Long planId)
    {
        this.planId = planId;
    }

    @NotBlank(message = "计划名称不能为空")
    @Size(min = 0, max = 100, message = "计划名称长度不能超过100个字符")
    public String getPlanName()
    {
        return planName;
    }

    public void setPlanName(String planName)
    {
        this.planName = planName;
    }

    @NotBlank(message = "考核周期不能为空")
    public String getCycleType()
    {
        return cycleType;
    }

    public void setCycleType(String cycleType)
    {
        this.cycleType = cycleType;
    }

    @NotNull(message = "开始日期不能为空")
    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    @NotNull(message = "结束日期不能为空")
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    @NotNull(message = "考核模板不能为空")
    public Long getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(Long templateId)
    {
        this.templateId = templateId;
    }

    public String getTemplateName()
    {
        return templateName;
    }

    public void setTemplateName(String templateName)
    {
        this.templateName = templateName;
    }

    @NotNull(message = "自评权重不能为空")
    @Min(value = 0, message = "自评权重不能小于0")
    @Max(value = 100, message = "自评权重不能大于100")
    public Integer getSelfWeight()
    {
        return selfWeight;
    }

    public void setSelfWeight(Integer selfWeight)
    {
        this.selfWeight = selfWeight;
    }

    public String getPlanStatus()
    {
        return planStatus;
    }

    public void setPlanStatus(String planStatus)
    {
        this.planStatus = planStatus;
    }

    public Date getPublishTime()
    {
        return publishTime;
    }

    public void setPublishTime(Date publishTime)
    {
        this.publishTime = publishTime;
    }

    public Date getFinishTime()
    {
        return finishTime;
    }

    public void setFinishTime(Date finishTime)
    {
        this.finishTime = finishTime;
    }

    public Integer getTotalCount()
    {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount)
    {
        this.totalCount = totalCount;
    }

    public Integer getFinishedCount()
    {
        return finishedCount;
    }

    public void setFinishedCount(Integer finishedCount)
    {
        this.finishedCount = finishedCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("planId", getPlanId())
            .append("planName", getPlanName())
            .append("cycleType", getCycleType())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("templateId", getTemplateId())
            .append("selfWeight", getSelfWeight())
            .append("planStatus", getPlanStatus())
            .append("publishTime", getPublishTime())
            .append("finishTime", getFinishTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
