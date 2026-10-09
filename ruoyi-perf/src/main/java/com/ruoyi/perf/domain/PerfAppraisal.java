package com.ruoyi.perf.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 员工考核单表 perf_appraisal
 * 
 * @author ruoyi
 */
public class PerfAppraisal extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 考核单ID */
    private Long appraisalId;

    /** 计划ID */
    private Long planId;

    /** 计划名称 */
    @Excel(name = "考核计划")
    private String planName;

    /** 考核周期 */
    @Excel(name = "考核周期", dictType = "perf_cycle_type")
    private String cycleType;

    /** 计划状态 */
    private String planStatus;

    /** 自评权重（%） */
    private Integer selfWeight;

    /** 被考核人ID */
    private Long userId;

    /** 登录账号 */
    @Excel(name = "登录账号")
    private String loginName;

    /** 员工姓名 */
    @Excel(name = "员工姓名")
    private String userName;

    /** 部门ID */
    private Long deptId;

    /** 部门名称 */
    @Excel(name = "部门")
    private String deptName;

    /** 评分人ID */
    private Long reviewerId;

    /** 评分人姓名 */
    @Excel(name = "评分人")
    private String reviewerName;

    /** 自评得分 */
    @Excel(name = "自评得分", cellType = ColumnType.NUMERIC, scale = 2)
    private BigDecimal selfScore;

    /** 上级评分 */
    @Excel(name = "上级评分", cellType = ColumnType.NUMERIC, scale = 2)
    private BigDecimal leaderScore;

    /** 最终得分 */
    @Excel(name = "最终得分", cellType = ColumnType.NUMERIC, scale = 2)
    private BigDecimal finalScore;

    /** 绩效等级（S A B C D） */
    @Excel(name = "绩效等级", dictType = "perf_grade")
    private String grade;

    /** 计划内排名 */
    @Excel(name = "计划内排名", cellType = ColumnType.NUMERIC)
    private Integer planRank;

    /** 自评总结 */
    @Excel(name = "自评总结", width = 40)
    private String selfComment;

    /** 上级评语 */
    @Excel(name = "上级评语", width = 40)
    private String leaderComment;

    /** 考核状态（0待自评 1待评分 2已完成） */
    @Excel(name = "考核状态", dictType = "perf_appraisal_status")
    private String appraisalStatus;

    /** 自评提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date selfTime;

    /** 评分时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "评分时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    /** 考核明细 */
    private List<PerfAppraisalItem> items;

    public Long getAppraisalId()
    {
        return appraisalId;
    }

    public void setAppraisalId(Long appraisalId)
    {
        this.appraisalId = appraisalId;
    }

    public Long getPlanId()
    {
        return planId;
    }

    public void setPlanId(Long planId)
    {
        this.planId = planId;
    }

    public String getPlanName()
    {
        return planName;
    }

    public void setPlanName(String planName)
    {
        this.planName = planName;
    }

    public String getCycleType()
    {
        return cycleType;
    }

    public void setCycleType(String cycleType)
    {
        this.cycleType = cycleType;
    }

    public String getPlanStatus()
    {
        return planStatus;
    }

    public void setPlanStatus(String planStatus)
    {
        this.planStatus = planStatus;
    }

    public Integer getSelfWeight()
    {
        return selfWeight;
    }

    public void setSelfWeight(Integer selfWeight)
    {
        this.selfWeight = selfWeight;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getLoginName()
    {
        return loginName;
    }

    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public Long getReviewerId()
    {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId)
    {
        this.reviewerId = reviewerId;
    }

    public String getReviewerName()
    {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName)
    {
        this.reviewerName = reviewerName;
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

    public BigDecimal getFinalScore()
    {
        return finalScore;
    }

    public void setFinalScore(BigDecimal finalScore)
    {
        this.finalScore = finalScore;
    }

    public String getGrade()
    {
        return grade;
    }

    public void setGrade(String grade)
    {
        this.grade = grade;
    }

    public Integer getPlanRank()
    {
        return planRank;
    }

    public void setPlanRank(Integer planRank)
    {
        this.planRank = planRank;
    }

    public String getSelfComment()
    {
        return selfComment;
    }

    public void setSelfComment(String selfComment)
    {
        this.selfComment = selfComment;
    }

    public String getLeaderComment()
    {
        return leaderComment;
    }

    public void setLeaderComment(String leaderComment)
    {
        this.leaderComment = leaderComment;
    }

    public String getAppraisalStatus()
    {
        return appraisalStatus;
    }

    public void setAppraisalStatus(String appraisalStatus)
    {
        this.appraisalStatus = appraisalStatus;
    }

    public Date getSelfTime()
    {
        return selfTime;
    }

    public void setSelfTime(Date selfTime)
    {
        this.selfTime = selfTime;
    }

    public Date getReviewTime()
    {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime)
    {
        this.reviewTime = reviewTime;
    }

    public List<PerfAppraisalItem> getItems()
    {
        return items;
    }

    public void setItems(List<PerfAppraisalItem> items)
    {
        this.items = items;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("appraisalId", getAppraisalId())
            .append("planId", getPlanId())
            .append("planName", getPlanName())
            .append("cycleType", getCycleType())
            .append("planStatus", getPlanStatus())
            .append("selfWeight", getSelfWeight())
            .append("userId", getUserId())
            .append("loginName", getLoginName())
            .append("userName", getUserName())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("reviewerId", getReviewerId())
            .append("reviewerName", getReviewerName())
            .append("selfScore", getSelfScore())
            .append("leaderScore", getLeaderScore())
            .append("finalScore", getFinalScore())
            .append("grade", getGrade())
            .append("planRank", getPlanRank())
            .append("selfComment", getSelfComment())
            .append("leaderComment", getLeaderComment())
            .append("appraisalStatus", getAppraisalStatus())
            .append("selfTime", getSelfTime())
            .append("reviewTime", getReviewTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
