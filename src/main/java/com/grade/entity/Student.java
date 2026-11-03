package com.grade.entity;

import lombok.Data;

/**
 * 学生
 */
@Data
public class Student {
    private Integer id;
    private Integer userId;
    private String studentNo;
    private String name;
    private Integer classId;
}
