package com.grade.mapper;

import com.grade.entity.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ScoreMapper {
    List<Score> selectAll();
    Score selectById(Integer id);
    Score selectByStudentCourseTerm(@Param("studentId") Integer studentId,
                                    @Param("courseId") Integer courseId,
                                    @Param("termId") Integer termId);
    List<Score> selectByCourseTerm(Integer courseId, Integer termId);
    List<Score> selectByCourseClassTerm(@Param("courseId") Integer courseId,
                                        @Param("classId") Integer classId,
                                        @Param("termId") Integer termId);
    List<Score> selectByStudentId(Integer studentId);
    int insert(Score s);
    int update(Score s);
    int deleteById(Integer id);
}
