package com.grade.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 课程
 */
@Data
public class Course {
    private Integer id;
    private String courseName;
    private Double credit;
    private LocalDateTime createdAt;
}
