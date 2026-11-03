package com.grade.entity;

import lombok.Data;

/**
 * 学期
 */
@Data
public class Term {
    private Integer id;
    private String termName;
    private String startYear;
    private Integer semester;
}
