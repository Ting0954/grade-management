package com.grade.mapper;

import com.grade.entity.ClassInfo;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ClassInfoMapper {
    List<ClassInfo> selectAll();
    ClassInfo selectById(Integer id);
    int insert(ClassInfo c);
    int update(ClassInfo c);
    int deleteById(Integer id);
}
