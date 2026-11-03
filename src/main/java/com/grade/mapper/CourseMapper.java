package com.grade.mapper;

import com.grade.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> selectAll();
    Course selectById(Integer id);
    int insert(Course c);
    int update(Course c);
    int deleteById(Integer id);
}
