package com.grade.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 成绩
 */
@Data
public class Score {
    private Integer id;
    private Integer studentId;
    private Integer courseId;
    private Integer classId;
    private Integer termId;
    private Double usualScore;
    private Double midScore;
    private Double finalScore;
    private Double totalScore;
    private Integer rankInClass;
    private Integer isPass;
    private String makeupStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
