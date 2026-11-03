package com.grade.controller;

import com.grade.entity.ClassInfo;
import com.grade.entity.Course;
import com.grade.entity.SysUser;
import com.grade.entity.Term;
import com.grade.service.ScoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 教师端：成绩预警与补考管理
 */
@Controller
@RequestMapping("/teacher/makeup")
public class TeacherMakeupController {

    private final ScoreService scoreService;

    public TeacherMakeupController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/select")
    public String select(HttpSession session, Model model) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        model.addAttribute("courses", scoreService.getTeacherCourses(user.getId()));
        model.addAttribute("classes", scoreService.getTeacherClasses(user.getId()));
        model.addAttribute("terms", scoreService.getAllTerms());
        return "teacher/makeupSelect";
    }

    @GetMapping("/list")
    public String list(@RequestParam Integer courseId,
                       @RequestParam Integer classId,
                       @RequestParam Integer termId,
                       Model model) {
        model.addAttribute("items", scoreService.getMakeupList(classId, courseId, termId));
        model.addAttribute("courseId", courseId);
        model.addAttribute("classId", classId);
        model.addAttribute("termId", termId);
        return "teacher/makeupList";
    }

    @PostMapping("/update")
    public String update(@RequestParam Integer courseId,
                         @RequestParam Integer classId,
                         @RequestParam Integer termId,
                         @RequestParam Integer studentId,
                         @RequestParam String status) {
        scoreService.updateMakeupStatus(studentId, courseId, termId, status);
        return "redirect:/teacher/makeup/list?courseId=" + courseId + "&classId=" + classId + "&termId=" + termId;
    }
}
