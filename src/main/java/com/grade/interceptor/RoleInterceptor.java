package com.grade.interceptor;

import com.grade.entity.SysUser;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 角色拦截器：按 URL 前缀控制访问权限
 * /admin/**   仅管理员
 * /teacher/** 教师或管理员
 * /student/** 学生或管理员
 */
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        SysUser user = session == null ? null : (SysUser) session.getAttribute("loginUser");
        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        String role = user.getRole();
        String path = request.getRequestURI();

        if (path.startsWith("/admin") && !"admin".equals(role)) {
            response.sendRedirect("/home");
            return false;
        }
        if (path.startsWith("/teacher") && !"teacher".equals(role) && !"admin".equals(role)) {
            response.sendRedirect("/home");
            return false;
        }
        if (path.startsWith("/student") && !"student".equals(role) && !"admin".equals(role)) {
            response.sendRedirect("/home");
            return false;
        }
        return true;
    }
}
