package com.grade.controller;

import com.grade.dto.ScoreEntryItem;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 教师端：成绩录入
 */
@Controller
@RequestMapping("/teacher/score")
public class TeacherScoreController {

    private final ScoreService scoreService;

    public TeacherScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    /** 选择课程/班级/学期 */
    @GetMapping("/select")
    public String select(HttpSession session, Model model) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        List<Course> courses = scoreService.getTeacherCourses(user.getId());
        List<ClassInfo> classes = scoreService.getTeacherClasses(user.getId());
        List<Term> terms = scoreService.getAllTerms();
        model.addAttribute("courses", courses);
        model.addAttribute("classes", classes);
        model.addAttribute("terms", terms);
        return "teacher/scoreSelect";
    }

    /** 学生名单 + 成绩录入表单 */
    @GetMapping("/list")
    public String list(@RequestParam Integer courseId,
                       @RequestParam Integer classId,
                       @RequestParam Integer termId,
                       Model model) {
        model.addAttribute("items", scoreService.getEntryList(classId, courseId, termId));
        model.addAttribute("courseId", courseId);
        model.addAttribute("classId", classId);
        model.addAttribute("termId", termId);
        return "teacher/scoreList";
    }

    /** 保存成绩 */
    @PostMapping("/save")
    public String save(@RequestParam Integer courseId,
                       @RequestParam Integer classId,
                       @RequestParam Integer termId,
                       @RequestParam(required = false) List<Integer> studentId,
                       @RequestParam Map<String, String> params) {
        if (studentId != null) {
            scoreService.saveScores(classId, courseId, termId, studentId, params);
        }
        return "redirect:/teacher/score/list?courseId=" + courseId + "&classId=" + classId + "&termId=" + termId;
    }

    /** 导出成绩 CSV（用 Excel 打开即可） */
    @GetMapping("/export")
    public void export(@RequestParam Integer courseId,
                       @RequestParam Integer classId,
                       @RequestParam Integer termId,
                       HttpServletResponse response) throws IOException {
        response.setContentType("text/csv;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String fileName = "成绩_" + courseId + "_" + classId + "_" + termId + ".csv";
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + fileName + "\"; filename*=UTF-8''"
                        + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()));

        Writer w = response.getWriter();
        w.write("\uFEFF"); // BOM，保证 Excel 正确识别中文
        w.write("学号,姓名,平时成绩,期中成绩,期末成绩\r\n");
        List<ScoreEntryItem> items = scoreService.getEntryList(classId, courseId, termId);
        for (ScoreEntryItem it : items) {
            w.write(it.getStudentNo() + "," + it.getName() + ","
                    + n(it.getUsualScore()) + "," + n(it.getMidScore()) + "," + n(it.getFinalScore()) + "\r\n");
        }
        w.flush();
    }

    /** 导入页面：选择课程/班级/学期 + 上传 CSV */
    @GetMapping("/importSelect")
    public String importSelect(HttpSession session, Model model) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        model.addAttribute("courses", scoreService.getTeacherCourses(user.getId()));
        model.addAttribute("classes", scoreService.getTeacherClasses(user.getId()));
        model.addAttribute("terms", scoreService.getAllTerms());
        return "teacher/scoreImport";
    }

    /** 导入成绩 CSV */
    @PostMapping("/import")
    public String importCsv(@RequestParam Integer courseId,
                            @RequestParam Integer classId,
                            @RequestParam Integer termId,
                            @RequestParam("file") MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        // 去掉首行表头与可能的 BOM
        String[] lines = content.replace("\uFEFF", "").split("\\r?\\n");
        List<String> dataLines = new java.util.ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            if (i == 0) continue; // 跳过表头
            dataLines.add(lines[i]);
        }
        scoreService.importScores(classId, courseId, termId, dataLines);
        return "redirect:/teacher/score/list?courseId=" + courseId + "&classId=" + classId + "&termId=" + termId;
    }

    private String n(Double v) {
        return v == null ? "" : String.valueOf(v);
    }
}
