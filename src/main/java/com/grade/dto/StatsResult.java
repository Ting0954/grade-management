package com.grade.dto;

import lombok.Data;

/**
 * 成绩统计结果
 */
@Data
public class StatsResult {
    private Integer count;        // 已录成绩人数
    private Double avg;           // 平均分
    private Double max;           // 最高分
    private Double min;           // 最低分
    private Integer passCount;    // 及格人数
    private Double passRate;      // 及格率（%）
    private Integer excellentCount; // 优秀人数（>=90）
    private Double excellentRate;   // 优秀率（%）
    private Integer below60;      // <60 人数
    private Integer g60;          // 60-69
    private Integer g70;          // 70-79
    private Integer g80;          // 80-89
    private Integer g90;          // >=90
}
