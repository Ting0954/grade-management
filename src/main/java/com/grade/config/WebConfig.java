package com.grade.config;

import com.grade.interceptor.LoginInterceptor;
import com.grade.interceptor.RoleInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置：注册拦截器与密码加密器
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截：除登录/登出/静态资源/健康检查外，全部需要登录
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login", "/logout", "/error",
                        "/db/check",
                        "/css/**", "/js/**", "/images/**",
                        "/favicon.ico");

        // 角色拦截：按角色控制管理端/教师端/学生端路径
        registry.addInterceptor(new RoleInterceptor())
                .addPathPatterns("/admin/**", "/teacher/**", "/student/**");
    }
}
