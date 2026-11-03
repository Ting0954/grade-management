# 班级学生成绩管理系统（Grade Management）

一套基于 **Spring Boot + MyBatis + MySQL + Thymeleaf** 的 Java EE 课程作业系统，支持教师录入成绩、学生查询、统计分析、图表可视化、CSV 批量导入导出、成绩预警与补考管理。

---

## 一、技术栈

| 类型 | 选型 |
|---|---|
| 后端 | Spring Boot 2.7.18（JDK 1.8） |
| 持久层 | MyBatis 2.3.2 |
| 数据库 | MySQL 5.7+（8.0 也可） |
| 前端 | Thymeleaf + Bootstrap 风格 CSS + ECharts + Font Awesome |
| 构建 | Maven |

> ⚠️ 必须用 **JDK 1.8**（项目 SDK 选 1.8），JDK 17/21 会导致 Lombok 编译报错。

---

## 二、功能清单

- **多角色登录**：管理员 / 教师 / 学生，三种不同首页与权限
- **教师**：按课程/班级/学期录入或修改成绩
- **学生**：查询本人各科成绩、总评、班级排名、是否及格
- **成绩统计**：平均分、最高/最低分、及格率、优秀率、分数段分布（柱状图+饼图）
- **权重折算**：总评 = 平时30% + 期中30% + 期末40%（权重在数据库参数表可调）
- **自动排名**：保存成绩后自动计算总评、是否及格、班级内排名并入库
- **CSV 批量导入导出**：教师可导出成绩表（Excel 可直接打开）、批量上传成绩
- **成绩预警 / 补考管理**：列出不及格学生，登记补考状态
- **管理员**：班级、课程、学期的增删改查

---

## 三、运行环境要求

1. JDK **1.8**（已配好 JAVA_HOME）
2. MySQL **5.7 或 8.0**（本地，端口 3306）
3. IntelliJ IDEA（自带 Maven）或独立 Maven
4. 浏览器

---

## 四、运行步骤（照着做即可）

### 第 1 步：下载代码
```bash
# 方式一：git clone
git clone https://github.com/Ting0954/grade-management.git

# 方式二：直接在 GitHub 页面点 Code → Download ZIP，解压
```

### 第 2 步：创建数据库
打开 MySQL（命令行或 Navicat / Workbench 都行），执行项目里的：
```
sql/init.sql
```
这个脚本会自动：
- 创建数据库 `grade_management`
- 创建 8 张表（班级、课程、学期、用户、学生、任课、成绩、系统参数）
- 写入初始测试数据（班级、课程、学期、8 个用户、5 个学生、权重参数）

> 注意：脚本开头有 `DROP DATABASE IF EXISTS grade_management;`，重复执行会清空重建。

### 第 3 步：修改数据库账号密码
打开 `src/main/resources/application.yml`，把第 12~13 行改成你自己 MySQL 的账号密码：
```yaml
    username: root          # 改成你的 MySQL 用户名
    password: 123456        # 改成你的 MySQL 密码
```
> 其他不用动。端口默认 8080，如果被占用就改 `server.port`。

### 第 4 步：用 IDEA 打开项目
1. IDEA → Open → 选中 `grade-management` 文件夹（带 pom.xml 的那层）
2. 等 Maven 自动下载依赖（第一次需要联网，约 2~5 分钟）
3. 确认项目 SDK 是 **JDK 1.8**（File → Project Structure → Project）
4. 右键 `src/main/java` → Mark Directory as → Sources Root（如果不是）

### 第 5 步：启动
运行主类：
```
src/main/java/com/grade/GradeManagementApplication.java
```
控制台看到 `Started GradeManagementApplication` 即启动成功。

### 第 6 步：浏览器访问
打开：**http://localhost:8080**

---

## 五、测试账号

| 角色 | 用户名 | 密码 |
|---|---|---|
| 管理员 | admin | admin123 |
| 教师 | t001 | 123456 |
| 教师 | t002 | 123456 |
| 学生 | s001 ~ s005 | 123456 |

> 密码首次启动时会被系统自动加密（BCrypt），不用管。

---

## 六、目录结构

```
grade-management/
├── pom.xml                    # Maven 依赖
├── sql/init.sql                # 建库建表 + 初始数据
└── src/main/
    ├── java/com/grade/
    │   ├── GradeManagementApplication.java   # 启动类
    │   ├── config/            # 配置（拦截器、Web配置、密码初始化）
    │   ├── controller/        # 控制器
    │   ├── service/           # 业务逻辑
    │   ├── mapper/            # MyBatis 接口
    │   ├── entity/            # 实体类
    │   └── dto/               # 数据传输对象
    └── resources/
        ├── application.yml   # 配置文件
        ├── mapper/*.xml      # MyBatis SQL
        ├── static/css/style.css
        └── templates/         # 页面（login/home/teacher/student/admin）
```

---

## 七、常见问题

| 问题 | 解决 |
|---|---|
| 启动报 `Communications link failure` / `Access denied` | 检查 MySQL 是否启动、application.yml 里账号密码是否对、端口是不是 3306 |
| 报 `找不到主类` / `ClassNotFoundException` | 右键 src/main/java 标记为 Sources Root；Project SDK 切到 JDK 1.8 |
| Lombok 编译报错 | 项目 SDK 必须是 JDK 1.8，不能用 JDK 21 |
| 8080 端口被占 | application.yml 里改 `server.port: 8081` |
| 中文乱码 | 数据库必须是 utf8mb4（init.sql 已设好） |
| 登录页能开但数据为空 | 确认跑过 `sql/init.sql`、数据库里有 grade_management 库 |

---

## 八、互评说明（本课程要求）

本项目已在多台机器上验证可运行：
- 环境：JDK 1.8 + MySQL 5.7/8.0 + IDEA 2025
- 步骤：clone 代码 → 执行 init.sql → 改 application.yml 密码 → 运行启动类 → 访问 localhost:8080

如在你的虚拟机上遇到问题，先看「第七节 常见问题」，仍不行请联系作者。
