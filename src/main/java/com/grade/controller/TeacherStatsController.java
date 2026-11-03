package com.grade.controller;

import com.grade.dto.StatsResult;
import com.grade.entity.ClassInfo;
import com.grade.entity.Course;
import com.grade.entity.SysUser;
import com.grade.entity.Term;
import com.grade.service.ScoreService;
import com.grade.service.StatsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 教师端：成绩统计
 */
@Controller
@RequestMapping("/teacher/stats")
public class TeacherStatsController {

    private final ScoreService scoreService;
    private final StatsService statsService;

    public TeacherStatsController(ScoreService scoreService, StatsService statsService) {
        this.scoreService = scoreService;
        this.statsService = statsService;
    }

    @GetMapping("/select")
    public String select(HttpSession session, Model model) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        List<Course> courses = scoreService.getTeacherCourses(user.getId());
        List<ClassInfo> classes = scoreService.getTeacherClasses(user.getId());
        List<Term> terms = scoreService.getAllTerms();
        model.addAttribute("courses", courses);
        model.addAttribute("classes", classes);
        model.addAttribute("terms", terms);
        return "teacher/statsSelect";
    }

    @GetMapping("/view")
    public String view(@RequestParam Integer courseId,
                       @RequestParam Integer classId,
                       @RequestParam Integer termId,
                       Model model) {
        StatsResult r = statsService.compute(courseId, classId, termId);
        model.addAttribute("s", r);
        return "teacher/statsView";
    }
}
