-- =====================================================
-- 班级学生成绩管理系统 建库建表脚本
-- 适用于 MySQL 5.6 / 8.x
-- =====================================================

-- 创建数据库（utf8mb4 支持中文）
CREATE DATABASE IF NOT EXISTS grade_management
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE grade_management;

-- ---------- 1. 班级表 ----------
DROP TABLE IF EXISTS class_info;
CREATE TABLE class_info (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  class_name  VARCHAR(50)  NOT NULL COMMENT '班级名称，如：软件工程2班',
  grade       VARCHAR(20)  DEFAULT NULL COMMENT '年级，如：2024级',
  major       VARCHAR(50)  DEFAULT NULL COMMENT '专业',
  created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- 2. 课程表 ----------
DROP TABLE IF EXISTS course;
CREATE TABLE course (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  course_name VARCHAR(50)  NOT NULL COMMENT '课程名称',
  credit      DECIMAL(3,1) DEFAULT 0 COMMENT '学分',
  created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- 3. 学期表 ----------
DROP TABLE IF EXISTS term;
CREATE TABLE term (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  term_name  VARCHAR(30) NOT NULL COMMENT '学期名称，如：2026-2027-1',
  start_year VARCHAR(10) DEFAULT NULL COMMENT '起始学年，如：2026',
  semester   TINYINT     DEFAULT NULL COMMENT '学期：1或2'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- 4. 用户表（管理员/教师/学生统一） ----------
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  username   VARCHAR(50)  NOT NULL UNIQUE COMMENT '登录名',
  password   VARCHAR(100) NOT NULL COMMENT '密码（第4步起为BCrypt加密存储）',
  real_name  VARCHAR(50)  NOT NULL COMMENT '真实姓名',
  role       VARCHAR(20)  NOT NULL COMMENT '角色：admin/teacher/student',
  class_id   INT DEFAULT NULL COMMENT '学生所属班级ID',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- 5. 学生表 ----------
DROP TABLE IF EXISTS student;
CREATE TABLE student (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  user_id    INT NOT NULL COMMENT '关联 sys_user.id',
  student_no VARCHAR(30) NOT NULL UNIQUE COMMENT '学号',
  name       VARCHAR(50) NOT NULL COMMENT '姓名',
  class_id   INT DEFAULT NULL COMMENT '所属班级',
  CONSTRAINT fk_stu_user  FOREIGN KEY (user_id)  REFERENCES sys_user(id),
  CONSTRAINT fk_stu_class FOREIGN KEY (class_id) REFERENCES class_info(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- 6. 任课关系表（教师-课程-班级） ----------
DROP TABLE IF EXISTS teacher_course;
CREATE TABLE teacher_course (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  teacher_id INT NOT NULL COMMENT '教师 sys_user.id',
  course_id  INT NOT NULL,
  class_id   INT NOT NULL,
  CONSTRAINT fk_tc_course FOREIGN KEY (course_id) REFERENCES course(id),
  CONSTRAINT fk_tc_class  FOREIGN KEY (class_id)  REFERENCES class_info(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- 7. 成绩表 ----------
DROP TABLE IF EXISTS score;
CREATE TABLE score (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  student_id    INT NOT NULL COMMENT '关联 student.id',
  course_id     INT NOT NULL,
  class_id      INT NOT NULL,
  term_id       INT NOT NULL,
  usual_score   DECIMAL(5,1) DEFAULT NULL COMMENT '平时成绩 0-100',
  mid_score     DECIMAL(5,1) DEFAULT NULL COMMENT '期中成绩 0-100',
  final_score   DECIMAL(5,1) DEFAULT NULL COMMENT '期末成绩 0-100',
  total_score   DECIMAL(5,1) DEFAULT NULL COMMENT '总评成绩（按权重计算）',
  rank_in_class INT DEFAULT NULL COMMENT '班级内排名',
  is_pass       TINYINT DEFAULT NULL COMMENT '是否及格：1及格 0不及格',
  makeup_status VARCHAR(20) DEFAULT '无' COMMENT '无/补考中/已通过补考/重修',
  created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT uk_stu_course_term UNIQUE (student_id, course_id, term_id),
  CONSTRAINT fk_sc_student FOREIGN KEY (student_id) REFERENCES student(id),
  CONSTRAINT fk_sc_course  FOREIGN KEY (course_id)  REFERENCES course(id),
  CONSTRAINT fk_sc_class   FOREIGN KEY (class_id)   REFERENCES class_info(id),
  CONSTRAINT fk_sc_term    FOREIGN KEY (term_id)    REFERENCES term(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------- 8. 系统参数表（权重等可配置项） ----------
DROP TABLE IF EXISTS sys_config;
CREATE TABLE sys_config (
  id           INT AUTO_INCREMENT PRIMARY KEY,
  config_key   VARCHAR(50)  NOT NULL UNIQUE,
  config_value VARCHAR(200) DEFAULT NULL,
  remark       VARCHAR(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- 初始数据
-- =====================================================

-- 班级
INSERT INTO class_info (class_name, grade, major) VALUES
('软件工程2班', '2024级', '软件工程');

-- 学期
INSERT INTO term (term_name, start_year, semester) VALUES
('2026-2027-1', '2026', 1);

-- 课程
INSERT INTO course (course_name, credit) VALUES
('数据结构', 4),
('Java Web开发', 3),
('数据库原理', 3);

-- 用户（admin / 教师 / 学生）
-- 注意：密码当前为明文占位，第4步实现登录时会在启动时转为 BCrypt 加密
INSERT INTO sys_user (username, password, real_name, role, class_id) VALUES
('admin',  'admin123', '系统管理员', 'admin',   NULL),
('t001',   '123456',   '王老师',     'teacher', NULL),
('t002',   '123456',   '李老师',     'teacher', NULL),
('s001',   '123456',   '张伟',       'student', 1),
('s002',   '123456',   '李娜',       'student', 1),
('s003',   '123456',   '王芳',       'student', 1),
('s004',   '123456',   '刘洋',       'student', 1),
('s005',   '123456',   '陈静',       'student', 1);

-- 学生明细（学号）
INSERT INTO student (user_id, student_no, name, class_id) VALUES
(4, '20241110101', '张伟', 1),
(5, '20241110102', '李娜', 1),
(6, '20241110103', '王芳', 1),
(7, '20241110104', '刘洋', 1),
(8, '20241110105', '陈静', 1);

-- 任课关系：王老师教 数据结构(2班)、Java Web(2班)；李老师教 数据库原理(2班)
INSERT INTO teacher_course (teacher_id, course_id, class_id) VALUES
(2, 1, 1),
(2, 2, 1),
(3, 3, 1);

-- 系统参数：成绩权重（平时:期中:期末 = 0.3:0.3:0.4）、及格线
INSERT INTO sys_config (config_key, config_value, remark) VALUES
('usual_weight',  '0.3', '平时成绩权重'),
('mid_weight',    '0.3', '期中成绩权重'),
('final_weight',  '0.4', '期末成绩权重'),
('pass_line',     '60',  '及格分数线');
