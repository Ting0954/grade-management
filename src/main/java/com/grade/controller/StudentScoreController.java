package com.grade.controller;

import com.grade.entity.Student;
import com.grade.entity.SysUser;
import com.grade.mapper.StudentMapper;
import com.grade.service.ScoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * 学生端：查询自己的成绩
 */
@Controller
@RequestMapping("/student/score")
public class StudentScoreController {

    private final StudentMapper studentMapper;
    private final ScoreService scoreService;

    public StudentScoreController(StudentMapper studentMapper, ScoreService scoreService) {
        this.studentMapper = studentMapper;
        this.scoreService = scoreService;
    }

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        Student student = studentMapper.selectByUserId(user.getId());
        if (student == null) {
            model.addAttribute("msg", "未找到你的学生档案，请联系管理员");
            return "student/scoreList";
        }
        model.addAttribute("items", scoreService.getStudentScoreViews(student.getId()));
        return "student/scoreList";
    }
}
