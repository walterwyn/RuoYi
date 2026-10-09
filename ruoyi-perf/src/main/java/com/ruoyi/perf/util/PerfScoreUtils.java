package com.ruoyi.perf.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.perf.domain.PerfAppraisalItem;

/**
 * 绩效计分工具
 * 
 * @author ruoyi
 */
public class PerfScoreUtils
{
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    /**
     * 按指标权重计算加权得分：Σ(分数 × 权重%)
     * 
     * @param items 考核明细
     * @param scoreGetter 取分方法（自评分或上级评分）
     * @return 加权得分，保留两位小数
     */
    public static BigDecimal weightedScore(List<PerfAppraisalItem> items, Function<PerfAppraisalItem, BigDecimal> scoreGetter)
    {
        BigDecimal total = BigDecimal.ZERO;
        for (PerfAppraisalItem item : items)
        {
            BigDecimal score = scoreGetter.apply(item);
            if (score == null)
            {
                continue;
            }
            total = total.add(score.multiply(BigDecimal.valueOf(item.getWeight())));
        }
        return total.divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    /**
     * 计算最终得分：自评得分 × 自评权重% + 上级评分 × (100 - 自评权重)%
     * 
     * @param selfScore 自评得分
     * @param leaderScore 上级评分
     * @param selfWeight 自评权重（%）
     * @return 最终得分，保留两位小数
     */
    public static BigDecimal finalScore(BigDecimal selfScore, BigDecimal leaderScore, int selfWeight)
    {
        BigDecimal self = selfScore == null ? BigDecimal.ZERO : selfScore;
        BigDecimal leader = leaderScore == null ? BigDecimal.ZERO : leaderScore;
        return self.multiply(BigDecimal.valueOf(selfWeight))
                .add(leader.multiply(BigDecimal.valueOf(PerfConstants.TOTAL_WEIGHT - selfWeight)))
                .divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    /**
     * 根据最终得分计算绩效等级
     * 
     * @param score 最终得分
     * @return S(≥90) A(≥80) B(≥70) C(≥60) D(<60)
     */
    public static String grade(BigDecimal score)
    {
        if (score == null)
        {
            return null;
        }
        if (score.compareTo(BigDecimal.valueOf(90)) >= 0)
        {
            return "S";
        }
        if (score.compareTo(BigDecimal.valueOf(80)) >= 0)
        {
            return "A";
        }
        if (score.compareTo(BigDecimal.valueOf(70)) >= 0)
        {
            return "B";
        }
        if (score.compareTo(BigDecimal.valueOf(60)) >= 0)
        {
            return "C";
        }
        return "D";
    }

    /**
     * 校验单项分数必须填写且在 0-100 之间
     * 
     * @param score 分数
     * @param indicatorName 指标名称
     */
    public static void checkScore(BigDecimal score, String indicatorName)
    {
        if (score == null)
        {
            throw new ServiceException("请填写指标「" + indicatorName + "」的分数");
        }
        if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(BigDecimal.valueOf(PerfConstants.MAX_SCORE)) > 0)
        {
            throw new ServiceException("指标「" + indicatorName + "」的分数必须在0到" + PerfConstants.MAX_SCORE + "之间");
        }
        if (score.scale() > 2 && score.stripTrailingZeros().scale() > 2)
        {
            throw new ServiceException("指标「" + indicatorName + "」的分数最多保留两位小数");
        }
    }
}
