package com.grade.entity;

import lombok.Data;

/**
 * 系统参数（权重、及格线等可配置项）
 */
@Data
public class SysConfig {
    private Integer id;
    private String configKey;
    private String configValue;
    private String remark;
}
