package com.grade.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证控制器：用来确认项目能正常启动并访问
 */
@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "成绩管理系统已启动成功！Spring Boot 2.7 运行正常。";
    }
}
