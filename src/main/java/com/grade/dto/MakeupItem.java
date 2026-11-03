package com.grade.dto;

import lombok.Data;

/**
 * 补考 / 预警名单条目
 */
@Data
public class MakeupItem {
    private Integer studentId;
    private String studentNo;
    private String name;
    private Double totalScore;
    private Integer rankInClass;
    private String makeupStatus;
}
