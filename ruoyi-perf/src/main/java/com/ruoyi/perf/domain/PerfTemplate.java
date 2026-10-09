package com.ruoyi.perf.domain;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 考核模板表 perf_template
 * 
 * @author ruoyi
 */
public class PerfTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 模板ID */
    private Long templateId;

    /** 模板名称 */
    private String templateName;

    /** 状态（0正常 1停用） */
    private String status;

    /** 指标数量 */
    private Integer itemCount;

    /** 模板指标 */
    private List<PerfTemplateItem> items;

    public Long getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(Long templateId)
    {
        this.templateId = templateId;
    }

    @NotBlank(message = "模板名称不能为空")
    @Size(min = 0, max = 100, message = "模板名称长度不能超过100个字符")
    public String getTemplateName()
    {
        return templateName;
    }

    public void setTemplateName(String templateName)
    {
        this.templateName = templateName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Integer getItemCount()
    {
        return itemCount;
    }

    public void setItemCount(Integer itemCount)
    {
        this.itemCount = itemCount;
    }

    public List<PerfTemplateItem> getItems()
    {
        return items;
    }

    public void setItems(List<PerfTemplateItem> items)
    {
        this.items = items;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("templateId", getTemplateId())
            .append("templateName", getTemplateName())
            .append("status", getStatus())
            .append("items", getItems())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
