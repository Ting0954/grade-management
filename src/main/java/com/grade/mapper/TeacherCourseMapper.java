package com.grade.mapper;

import com.grade.entity.TeacherCourse;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface TeacherCourseMapper {
    List<TeacherCourse> selectAll();
    List<TeacherCourse> selectByTeacherId(Integer teacherId);
    List<TeacherCourse> selectByCourseId(Integer courseId);
    int insert(TeacherCourse tc);
    int deleteById(Integer id);
}
