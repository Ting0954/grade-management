package com.grade.config;

import com.grade.entity.SysUser;
import com.grade.mapper.SysUserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 启动时把初始明文密码加密为 BCrypt 存储
 * （init.sql 里的密码是明文占位，登录前必须转成加密格式）
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final SysUserMapper sysUserMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(SysUserMapper sysUserMapper, BCryptPasswordEncoder passwordEncoder) {
        this.sysUserMapper = sysUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        List<SysUser> users = sysUserMapper.selectAll();
        for (SysUser u : users) {
            String pwd = u.getPassword();
            // 若密码不是 BCrypt 格式（即明文），则加密并更新
            if (pwd == null || !pwd.startsWith("$2")) {
                u.setPassword(passwordEncoder.encode(pwd));
                sysUserMapper.update(u);
                System.out.println("已加密用户密码: " + u.getUsername());
            }
        }
    }
}
