package com.grade.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 班级
 */
@Data
public class ClassInfo {
    private Integer id;
    private String className;
    private String grade;
    private String major;
    private LocalDateTime createdAt;
}
