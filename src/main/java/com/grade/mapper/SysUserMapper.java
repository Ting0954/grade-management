package com.grade.mapper;

import com.grade.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SysUserMapper {
    List<SysUser> selectAll();
    SysUser selectById(Integer id);
    SysUser selectByUsername(String username);
    int insert(SysUser u);
    int update(SysUser u);
    int deleteById(Integer id);
}
