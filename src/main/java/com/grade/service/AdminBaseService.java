package com.grade.service;

import com.grade.entity.ClassInfo;
import com.grade.entity.Course;
import com.grade.entity.Term;
import com.grade.mapper.ClassInfoMapper;
import com.grade.mapper.CourseMapper;
import com.grade.mapper.TermMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理员基础数据维护（班级/课程/学期）
 */
@Service
public class AdminBaseService {

    private final ClassInfoMapper classInfoMapper;
    private final CourseMapper courseMapper;
    private final TermMapper termMapper;

    public AdminBaseService(ClassInfoMapper classInfoMapper, CourseMapper courseMapper, TermMapper termMapper) {
        this.classInfoMapper = classInfoMapper;
        this.courseMapper = courseMapper;
        this.termMapper = termMapper;
    }

    // ---------- 班级 ----------
    public List<ClassInfo> listClasses() {
        return classInfoMapper.selectAll();
    }

    public void addClass(String className, String grade, String major) {
        ClassInfo c = new ClassInfo();
        c.setClassName(className);
        c.setGrade(grade);
        c.setMajor(major);
        classInfoMapper.insert(c);
    }

    public void updateClass(Integer id, String className, String grade, String major) {
        ClassInfo c = classInfoMapper.selectById(id);
        if (c == null) return;
        c.setClassName(className);
        c.setGrade(grade);
        c.setMajor(major);
        classInfoMapper.update(c);
    }

    public void deleteClass(Integer id) {
        classInfoMapper.deleteById(id);
    }

    // ---------- 课程 ----------
    public List<Course> listCourses() {
        return courseMapper.selectAll();
    }

    public void addCourse(String courseName, Double credit) {
        Course c = new Course();
        c.setCourseName(courseName);
        c.setCredit(credit);
        courseMapper.insert(c);
    }

    public void updateCourse(Integer id, String courseName, Double credit) {
        Course c = courseMapper.selectById(id);
        if (c == null) return;
        c.setCourseName(courseName);
        c.setCredit(credit);
        courseMapper.update(c);
    }

    public void deleteCourse(Integer id) {
        courseMapper.deleteById(id);
    }

    // ---------- 学期 ----------
    public List<Term> listTerms() {
        return termMapper.selectAll();
    }

    public void addTerm(String termName, String startYear, Integer semester) {
        Term t = new Term();
        t.setTermName(termName);
        t.setStartYear(startYear);
        t.setSemester(semester);
        termMapper.insert(t);
    }

    public void updateTerm(Integer id, String termName, String startYear, Integer semester) {
        Term t = termMapper.selectById(id);
        if (t == null) return;
        t.setTermName(termName);
        t.setStartYear(startYear);
        t.setSemester(semester);
        termMapper.update(t);
    }

    public void deleteTerm(Integer id) {
        termMapper.deleteById(id);
    }
}
