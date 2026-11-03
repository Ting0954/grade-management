package com.grade.dto;

import lombok.Data;

/**
 * 学生端成绩展示项
 */
@Data
public class ScoreViewItem {
    private String courseName;
    private String termName;
    private Double usualScore;
    private Double midScore;
    private Double finalScore;
    private Double totalScore;
    private Integer rankInClass;
    private Integer isPass;
}
