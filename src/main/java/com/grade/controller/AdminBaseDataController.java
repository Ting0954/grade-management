package com.grade.controller;

import com.grade.service.AdminBaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 管理员端：班级/课程/学期基础数据维护
 */
@Controller
@RequestMapping("/admin")
public class AdminBaseDataController {

    private final AdminBaseService service;

    public AdminBaseDataController(AdminBaseService service) {
        this.service = service;
    }

    // ---------- 班级 ----------
    @GetMapping("/class/list")
    public String classList(Model model) {
        model.addAttribute("classes", service.listClasses());
        return "admin/classList";
    }

    @PostMapping("/class/add")
    public String classAdd(@RequestParam String className,
                           @RequestParam(required = false) String grade,
                           @RequestParam(required = false) String major) {
        service.addClass(className, grade, major);
        return "redirect:/admin/class/list";
    }

    @PostMapping("/class/edit")
    public String classEdit(@RequestParam Integer id,
                            @RequestParam String className,
                            @RequestParam(required = false) String grade,
                            @RequestParam(required = false) String major) {
        service.updateClass(id, className, grade, major);
        return "redirect:/admin/class/list";
    }

    @GetMapping("/class/delete")
    public String classDelete(@RequestParam Integer id) {
        service.deleteClass(id);
        return "redirect:/admin/class/list";
    }

    // ---------- 课程 ----------
    @GetMapping("/course/list")
    public String courseList(Model model) {
        model.addAttribute("courses", service.listCourses());
        return "admin/courseList";
    }

    @PostMapping("/course/add")
    public String courseAdd(@RequestParam String courseName,
                            @RequestParam(required = false) Double credit) {
        service.addCourse(courseName, credit);
        return "redirect:/admin/course/list";
    }

    @PostMapping("/course/edit")
    public String courseEdit(@RequestParam Integer id,
                             @RequestParam String courseName,
                             @RequestParam(required = false) Double credit) {
        service.updateCourse(id, courseName, credit);
        return "redirect:/admin/course/list";
    }

    @GetMapping("/course/delete")
    public String courseDelete(@RequestParam Integer id) {
        service.deleteCourse(id);
        return "redirect:/admin/course/list";
    }

    // ---------- 学期 ----------
    @GetMapping("/term/list")
    public String termList(Model model) {
        model.addAttribute("terms", service.listTerms());
        return "admin/termList";
    }

    @PostMapping("/term/add")
    public String termAdd(@RequestParam String termName,
                          @RequestParam(required = false) String startYear,
                          @RequestParam(required = false) Integer semester) {
        service.addTerm(termName, startYear, semester);
        return "redirect:/admin/term/list";
    }

    @PostMapping("/term/edit")
    public String termEdit(@RequestParam Integer id,
                           @RequestParam String termName,
                           @RequestParam(required = false) String startYear,
                           @RequestParam(required = false) Integer semester) {
        service.updateTerm(id, termName, startYear, semester);
        return "redirect:/admin/term/list";
    }

    @GetMapping("/term/delete")
    public String termDelete(@RequestParam Integer id) {
        service.deleteTerm(id);
        return "redirect:/admin/term/list";
    }
}
