package com.grade.controller;

import com.grade.mapper.ClassInfoMapper;
import com.grade.mapper.SysUserMapper;
import com.grade.entity.ClassInfo;
import com.grade.entity.SysUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 验证数据库连接是否正常
 */
@RestController
public class DbCheckController {

    private final ClassInfoMapper classInfoMapper;
    private final SysUserMapper sysUserMapper;

    public DbCheckController(ClassInfoMapper classInfoMapper, SysUserMapper sysUserMapper) {
        this.classInfoMapper = classInfoMapper;
        this.sysUserMapper = sysUserMapper;
    }

    @GetMapping("/db/check")
    public String check() {
        List<ClassInfo> classes = classInfoMapper.selectAll();
        List<SysUser> users = sysUserMapper.selectAll();
        return "数据库连接正常！当前班级数 = " + classes.size() + "，用户数 = " + users.size();
    }
}
