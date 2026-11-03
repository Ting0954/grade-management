package com.grade.service;

import com.grade.dto.StatsResult;
import com.grade.entity.Score;
import com.grade.entity.SysConfig;
import com.grade.mapper.ScoreMapper;
import com.grade.mapper.SysConfigMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 统计业务：平均分、及格率、优秀率、分数段分布、排名
 */
@Service
public class StatsService {

    private final ScoreMapper scoreMapper;
    private final SysConfigMapper sysConfigMapper;

    public StatsService(ScoreMapper scoreMapper, SysConfigMapper sysConfigMapper) {
        this.scoreMapper = scoreMapper;
        this.sysConfigMapper = sysConfigMapper;
    }

    /** 读取权重，默认 平时0.3/期中0.3/期末0.4 */
    private double weight(String key, double def) {
        SysConfig c = sysConfigMapper.selectByKey(key);
        if (c == null || c.getConfigValue() == null) return def;
        try {
            return Double.parseDouble(c.getConfigValue());
        } catch (Exception e) {
            return def;
        }
    }

    /** 及格线，默认 60 */
    private double passLine() {
        SysConfig c = sysConfigMapper.selectByKey("pass_line");
        if (c == null || c.getConfigValue() == null) return 60;
        try {
            return Double.parseDouble(c.getConfigValue());
        } catch (Exception e) {
            return 60;
        }
    }

    /** 按权重计算总评成绩；无任何成绩返回 null */
    private Double computeTotal(Score s) {
        if (s.getUsualScore() == null && s.getMidScore() == null && s.getFinalScore() == null) {
            return null;
        }
        double u = s.getUsualScore() == null ? 0 : s.getUsualScore();
        double m = s.getMidScore() == null ? 0 : s.getMidScore();
        double f = s.getFinalScore() == null ? 0 : s.getFinalScore();
        double uw = weight("usual_weight", 0.3);
        double mw = weight("mid_weight", 0.3);
        double fw = weight("final_weight", 0.4);
        double total = u * uw + m * mw + f * fw;
        return Math.round(total * 10.0) / 10.0;
    }

    /** 计算某课程某班级某学期的统计 */
    public StatsResult compute(Integer courseId, Integer classId, Integer termId) {
        List<Score> scores = scoreMapper.selectByCourseClassTerm(courseId, classId, termId);
        double pl = passLine();

        StatsResult r = new StatsResult();
        r.setCount(0);
        r.setPassCount(0);
        r.setExcellentCount(0);
        r.setBelow60(0);
        r.setG60(0);
        r.setG70(0);
        r.setG80(0);
        r.setG90(0);

        int count = 0;
        double sum = 0;
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        int pass = 0, excellent = 0;
        int b60 = 0, g60 = 0, g70 = 0, g80 = 0, g90 = 0;

        for (Score s : scores) {
            Double t = computeTotal(s);
            if (t == null) continue;
            count++;
            sum += t;
            max = Math.max(max, t);
            min = Math.min(min, t);
            if (t >= pl) pass++;
            if (t >= 90) excellent++;
            if (t < 60) b60++;
            else if (t < 70) g60++;
            else if (t < 80) g70++;
            else if (t < 90) g80++;
            else g90++;
        }

        r.setCount(count);
        if (count > 0) {
            r.setAvg(Math.round(sum / count * 10.0) / 10.0);
            r.setMax(max);
            r.setMin(min);
            r.setPassRate(Math.round(pass * 1000.0 / count) / 10.0);
            r.setExcellentRate(Math.round(excellent * 1000.0 / count) / 10.0);
        } else {
            r.setAvg(0.0);
            r.setMax(0.0);
            r.setMin(0.0);
            r.setPassRate(0.0);
            r.setExcellentRate(0.0);
        }
        r.setPassCount(pass);
        r.setExcellentCount(excellent);
        r.setBelow60(b60);
        r.setG60(g60);
        r.setG70(g70);
        r.setG80(g80);
        r.setG90(g90);
        return r;
    }

    /**
     * 重新计算并固化某课程某班级某学期的：总评成绩、是否及格、班级内排名
     * （教师在保存成绩后调用）
     */
    public void refreshRankAndPass(Integer courseId, Integer classId, Integer termId) {
        List<Score> scores = scoreMapper.selectByCourseClassTerm(courseId, classId, termId);
        double pl = passLine();

        // 计算每个学生的总评与及格标记
        for (Score s : scores) {
            Double t = computeTotal(s);
            s.setTotalScore(t);
            s.setIsPass(t == null ? null : (t >= pl ? 1 : 0));
        }

        // 按总评从高到低排序，空总评排最后
        scores.sort((a, b) -> {
            if (a.getTotalScore() == null && b.getTotalScore() == null) return 0;
            if (a.getTotalScore() == null) return 1;
            if (b.getTotalScore() == null) return -1;
            return Double.compare(b.getTotalScore(), a.getTotalScore());
        });

        int rank = 0;
        for (Score s : scores) {
            if (s.getTotalScore() != null) {
                rank++;
                s.setRankInClass(rank);
            } else {
                s.setRankInClass(null);
            }
            scoreMapper.update(s);
        }
    }
}
