package com.grade.entity;

import lombok.Data;

/**
 * 任课关系（教师-课程-班级）
 */
@Data
public class TeacherCourse {
    private Integer id;
    private Integer teacherId;
    private Integer courseId;
    private Integer classId;
}
