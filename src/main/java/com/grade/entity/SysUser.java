package com.grade.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户（管理员/教师/学生统一）
 */
@Data
public class SysUser {
    private Integer id;
    private String username;
    private String password;
    private String realName;
    private String role;
    private Integer classId;
    private LocalDateTime createdAt;
}
