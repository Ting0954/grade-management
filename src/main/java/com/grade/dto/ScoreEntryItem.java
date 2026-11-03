package com.grade.dto;

import lombok.Data;

/**
 * 成绩录入行：一名学生的录入数据
 */
@Data
public class ScoreEntryItem {
    private Integer studentId;
    private String studentNo;
    private String name;
    private Double usualScore;
    private Double midScore;
    private Double finalScore;
}
