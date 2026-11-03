package com.grade.mapper;

import com.grade.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> selectAll();
    Student selectById(Integer id);
    Student selectByUserId(Integer userId);
    List<Student> selectByClassId(Integer classId);
    int insert(Student s);
    int update(Student s);
    int deleteById(Integer id);
}
