package com.grade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动类
 */
@SpringBootApplication
public class GradeManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(GradeManagementApplication.class, args);
        System.out.println("==========================================");
        System.out.println("  成绩管理系统启动成功！");
        System.out.println("  浏览器访问: http://localhost:8080");
        System.out.println("==========================================");
    }
}
