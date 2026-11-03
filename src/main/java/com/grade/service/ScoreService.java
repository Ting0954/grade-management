package com.grade.service;

import com.grade.dto.MakeupItem;
import com.grade.dto.ScoreEntryItem;
import com.grade.dto.ScoreViewItem;
import com.grade.entity.ClassInfo;
import com.grade.entity.Course;
import com.grade.entity.Score;
import com.grade.entity.Student;
import com.grade.entity.TeacherCourse;
import com.grade.entity.Term;
import com.grade.mapper.ClassInfoMapper;
import com.grade.mapper.CourseMapper;
import com.grade.mapper.ScoreMapper;
import com.grade.mapper.StudentMapper;
import com.grade.mapper.TeacherCourseMapper;
import com.grade.mapper.TermMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 成绩业务：教师选课选班、录入、保存
 */
@Service
public class ScoreService {

    private final ScoreMapper scoreMapper;
    private final StudentMapper studentMapper;
    private final TeacherCourseMapper teacherCourseMapper;
    private final CourseMapper courseMapper;
    private final ClassInfoMapper classInfoMapper;
    private final TermMapper termMapper;
    private final StatsService statsService;

    public ScoreService(ScoreMapper scoreMapper, StudentMapper studentMapper,
                        TeacherCourseMapper teacherCourseMapper, CourseMapper courseMapper,
                        ClassInfoMapper classInfoMapper, TermMapper termMapper,
                        StatsService statsService) {
        this.scoreMapper = scoreMapper;
        this.studentMapper = studentMapper;
        this.teacherCourseMapper = teacherCourseMapper;
        this.courseMapper = courseMapper;
        this.classInfoMapper = classInfoMapper;
        this.termMapper = termMapper;
        this.statsService = statsService;
    }

    /** 当前教师教的课程（去重） */
    public List<Course> getTeacherCourses(Integer teacherId) {
        List<TeacherCourse> tcs = teacherCourseMapper.selectByTeacherId(teacherId);
        Map<Integer, Course> map = new LinkedHashMap<>();
        for (TeacherCourse tc : tcs) {
            Course c = courseMapper.selectById(tc.getCourseId());
            if (c != null) map.put(c.getId(), c);
        }
        return new ArrayList<>(map.values());
    }

    /** 当前教师教的班级（去重） */
    public List<ClassInfo> getTeacherClasses(Integer teacherId) {
        List<TeacherCourse> tcs = teacherCourseMapper.selectByTeacherId(teacherId);
        Map<Integer, ClassInfo> map = new LinkedHashMap<>();
        for (TeacherCourse tc : tcs) {
            ClassInfo ci = classInfoMapper.selectById(tc.getClassId());
            if (ci != null) map.put(ci.getId(), ci);
        }
        return new ArrayList<>(map.values());
    }

    public List<Term> getAllTerms() {
        return termMapper.selectAll();
    }

    /** 某课程某班级某学期下所有学生的录入数据（含已录成绩） */
    public List<ScoreEntryItem> getEntryList(Integer classId, Integer courseId, Integer termId) {
        List<Student> students = studentMapper.selectByClassId(classId);
        List<ScoreEntryItem> list = new ArrayList<>();
        for (Student st : students) {
            ScoreEntryItem item = new ScoreEntryItem();
            item.setStudentId(st.getId());
            item.setStudentNo(st.getStudentNo());
            item.setName(st.getName());
            Score sc = scoreMapper.selectByStudentCourseTerm(st.getId(), courseId, termId);
            if (sc != null) {
                item.setUsualScore(sc.getUsualScore());
                item.setMidScore(sc.getMidScore());
                item.setFinalScore(sc.getFinalScore());
            }
            list.add(item);
        }
        return list;
    }

    /** 保存本班本课程本学期全部学生的成绩（有则更新，无则插入） */
    public void saveScores(Integer classId, Integer courseId, Integer termId,
                           List<Integer> studentIds, Map<String, String> params) {
        for (Integer sid : studentIds) {
            Double usual = parse(params.get("usual_" + sid));
            Double mid = parse(params.get("mid_" + sid));
            Double fin = parse(params.get("final_" + sid));

            Score existing = scoreMapper.selectByStudentCourseTerm(sid, courseId, termId);
            if (existing != null) {
                existing.setUsualScore(usual);
                existing.setMidScore(mid);
                existing.setFinalScore(fin);
                scoreMapper.update(existing);
            } else {
                Score s = new Score();
                s.setStudentId(sid);
                s.setCourseId(courseId);
                s.setClassId(classId);
                s.setTermId(termId);
                s.setUsualScore(usual);
                s.setMidScore(mid);
                s.setFinalScore(fin);
                scoreMapper.insert(s);
            }
        }
        // 保存后重新计算总评、及格、班级排名并固化到数据库
        statsService.refreshRankAndPass(courseId, classId, termId);
    }

    private Double parse(String v) {
        if (v == null || v.trim().isEmpty()) return null;
        try {
            return Double.parseDouble(v.trim());
        } catch (Exception e) {
            return null;
        }
    }

    /** 某学生的成绩展示列表（含课程名/学期名） */
    public List<ScoreViewItem> getStudentScoreViews(Integer studentId) {
        List<Score> scores = scoreMapper.selectByStudentId(studentId);
        List<ScoreViewItem> list = new ArrayList<>();
        for (Score s : scores) {
            ScoreViewItem it = new ScoreViewItem();
            Course c = courseMapper.selectById(s.getCourseId());
            Term t = termMapper.selectById(s.getTermId());
            it.setCourseName(c != null ? c.getCourseName() : "未知课程");
            it.setTermName(t != null ? t.getTermName() : "未知学期");
            it.setUsualScore(s.getUsualScore());
            it.setMidScore(s.getMidScore());
            it.setFinalScore(s.getFinalScore());
            it.setTotalScore(s.getTotalScore());
            it.setRankInClass(s.getRankInClass());
            it.setIsPass(s.getIsPass());
            list.add(it);
        }
        return list;
    }

    /**
     * 从 CSV 行导入成绩：格式为 学号,姓名,平时,期中,期末
     * 按学号匹配本班学生，有则更新、无则插入，最后重新计算总评/排名
     * 返回成功导入的条数
     */
    public int importScores(Integer classId, Integer courseId, Integer termId, List<String> lines) {
        List<Student> students = studentMapper.selectByClassId(classId);
        Map<String, Student> byNo = new HashMap<>();
        for (Student st : students) {
            byNo.put(st.getStudentNo(), st);
        }

        int count = 0;
        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length < 5) continue; // 至少 学号,姓名,平时,期中,期末
            String no = parts[0].trim();
            Student st = byNo.get(no);
            if (st == null) continue;

            Double usual = parse(parts[2]);
            Double mid = parse(parts[3]);
            Double fin = parse(parts[4]);

            Score existing = scoreMapper.selectByStudentCourseTerm(st.getId(), courseId, termId);
            if (existing != null) {
                existing.setUsualScore(usual);
                existing.setMidScore(mid);
                existing.setFinalScore(fin);
                scoreMapper.update(existing);
            } else {
                Score s = new Score();
                s.setStudentId(st.getId());
                s.setCourseId(courseId);
                s.setClassId(classId);
                s.setTermId(termId);
                s.setUsualScore(usual);
                s.setMidScore(mid);
                s.setFinalScore(fin);
                scoreMapper.insert(s);
            }
            count++;
        }

        if (count > 0) {
            statsService.refreshRankAndPass(courseId, classId, termId);
        }
        return count;
    }

    /** 预警/补考名单：列出该课程班级学期中总评不及格的学生 */
    public List<MakeupItem> getMakeupList(Integer classId, Integer courseId, Integer termId) {
        List<Score> scores = scoreMapper.selectByCourseClassTerm(courseId, classId, termId);
        List<MakeupItem> list = new ArrayList<>();
        for (Score s : scores) {
            if (s.getIsPass() != null && s.getIsPass() == 0) {
                Student st = studentMapper.selectById(s.getStudentId());
                MakeupItem it = new MakeupItem();
                it.setStudentId(s.getStudentId());
                it.setStudentNo(st != null ? st.getStudentNo() : "");
                it.setName(st != null ? st.getName() : "");
                it.setTotalScore(s.getTotalScore());
                it.setRankInClass(s.getRankInClass());
                it.setMakeupStatus(s.getMakeupStatus() == null ? "无" : s.getMakeupStatus());
                list.add(it);
            }
        }
        return list;
    }

    /** 更新某生某课程某学期的补考状态 */
    public void updateMakeupStatus(Integer studentId, Integer courseId, Integer termId, String status) {
        Score s = scoreMapper.selectByStudentCourseTerm(studentId, courseId, termId);
        if (s == null) return;
        s.setMakeupStatus(status == null || status.trim().isEmpty() ? "无" : status);
        scoreMapper.update(s);
    }
}
