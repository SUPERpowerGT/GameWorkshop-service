# 开发日志

## 0913-项目初始化

1.注意包名称要小写，不要用段横杠，如果重命名，请务必要全部重构



2.如果项目没有gravel或者maven可以从spring initialer网站初始化一个项目包，并将包的内容复制到本地，不要忘记重新更新gitignore，具体操作如下：

- 打开网址https://start.spring.io/

- Project选择需要的版本管理工具

- Language选择需要的语言

- SpringBoot选择推荐版本

- ProjectMetadata

  - Group使用项目名称
  - Artifact使用项目名称注意小写不要有短横，这是文件夹命名规范
  - Java版本注意匹配
  - 其他默认

- Dependencies点击添加然后选择spring web，MyBatis以及PostgreSQL Driver

- generate 并打开压缩包，赋值项目内容到本地

- gitignore重新配置

  ```bash
  git rm -r --cached .
  #移除暂存区所有内容
  git add .
  #重新添加到暂存区
  git commit -m "####"
  git push
  ```



3.mvnw以及mvnw.cmd以及.mvn是用来帮助配置环境保持一致性，建议上传到git上方便配置同步管理



4.为了更好的方便服务拆分，具有良好的拓展性，选择DDD（领域驱动设计）和微服务架构设计，方便后续拓展



5.DDD是一种软件设计开发思路，传统从技术出发，DDD从业务出发，具体应用到开发中文件架构更新如下：

```markdown
gameworkshop-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/gameworkshop/
│   │   │       ├── GameWorkshopApplication.java       // 应用启动类
│   │   │       ├── infrastructure/                    // 基础设施层（企业级核心）
│   │   │       │   ├── config/                        // 全局配置
│   │   │       │   │   ├── MyBatisPlusConfig.java     // ORM框架配置
│   │   │       │   │   ├── RedisConfig.java           // 缓存配置
│   │   │       │   │   ├── SecurityConfig.java        // 安全框架配置（Spring Security）
│   │   │       │   │   ├── WebMvcConfig.java          // MVC配置（跨域、拦截器）
│   │   │       │   │   └── SwaggerConfig.java         // 接口文档配置
│   │   │       │   ├── exception/                     // 全局异常处理
│   │   │       │   │   ├── GlobalExceptionHandler.java// 统一异常处理器
│   │   │       │   │   ├── BusinessException.java     // 业务异常基类
│   │   │       │   │   └── ErrorCode.java             // 错误码枚举
│   │   │       │   ├── security/                      // 安全组件
│   │   │       │   │   ├── JwtTokenProvider.java      // JWT令牌生成/验证
│   │   │       │   │   ├── UserDetailsServiceImpl.java// 用户认证实现
│   │   │       │   │   └── JwtAuthenticationFilter.java// 认证过滤器
│   │   │       │   └── util/                          // 通用工具
│   │   │       │       ├── ResultUtil.java            // 响应结果封装
│   │   │       │       ├── ValidationUtil.java        // 参数校验工具
│   │   │       │       └── FileStorageUtil.java       // 文件存储工具（兼容云存储）
│   │   │       ├── application/                       // 应用服务层（协调领域层与基础设施）
│   │   │       │   ├── dto/                           // 数据传输对象（跨模块交互）
│   │   │       │   │   ├── request/                   // 入参DTO
│   │   │       │   │   │   ├── ProjectCreateRequest.java
│   │   │       │   │   │   └── FileUploadRequest.java
│   │   │       │   │   └── response/                  // 出参DTO
│   │   │       │   │       ├── ProjectResponse.java
│   │   │       │   │       └── FileDownloadResponse.java
│   │   │       │   └── service/                       // 应用服务（编排领域服务）
│   │   │       │       ├── ProjectApplicationService.java
│   │   │       │       └── FileApplicationService.java
│   │   │       ├── domain/                            // 领域层（核心业务逻辑）
│   │   │       │   ├── project/                       // 项目领域
│   │   │       │   │   ├── model/                     // 领域模型（充血模型）
│   │   │       │   │   │   └── Project.java           // 包含领域行为（如项目状态流转）
│   │   │       │   │   ├── repository/                // 领域仓库接口（定义数据操作契约）
│   │   │       │   │   │   └── ProjectRepository.java
│   │   │       │   │   └── service/                   // 领域服务（领域内复杂逻辑）
│   │   │       │   │       └── ProjectDomainService.java
│   │   │       │   └── file/                          // 文件领域
│   │   │       │       ├── model/
│   │   │       │       │   └── FileInfo.java          // 包含文件校验等领域行为
│   │   │       │       ├── repository/
│   │   │       │       │   └── FileRepository.java
│   │   │       │       └── service/
│   │   │       │           └── FileDomainService.java
│   │   │       ├── interfaces/                        // 接口层（对外暴露接口）
│   │   │       │   ├── rest/                          // REST接口
│   │   │       │   │   ├── ProjectController.java     // 项目相关接口
│   │   │       │   │   ├── FileController.java        // 文件相关接口
│   │   │       │   │   └── UserController.java        // 用户相关接口
│   │   │       │   └── dto/                           // 接口层专用DTO（适配前端）
│   │   │       │       ├── ProjectVO.java
│   │   │       │       └── FileVO.java
│   │   │       └── infrastructure/persistence/        // 持久化层（实现领域仓库）
│   │   │           ├── mybatis/                       // MyBatis实现
│   │   │           │   ├── mapper/                    // Mapper接口
│   │   │           │   │   ├── ProjectMapper.java
│   │   │           │   │   └── FileInfoMapper.java
│   │   │           │   └── repository/                // 仓库实现
│   │   │           │       ├── ProjectRepositoryImpl.java
│   │   │           │       └── FileRepositoryImpl.java
│   │   │           └── redis/                         // Redis缓存实现
│   │   │               └── ProjectCacheRepository.java
│   │   └── resources/
│   │       ├── application.yml                        // 主配置
│   │       ├── application-dev.yml                    // 开发环境配置
│   │       ├── application-prod.yml                   // 生产环境配置
│   │       ├── mybatis/                               // MyBatis映射文件
│   │       │   ├── ProjectMapper.xml
│   │       │   └── FileInfoMapper.xml
│   │       ├── db/                                    // 数据库脚本
│   │       │   ├── init.sql                           // 初始化脚本
│   │       │   └── upgrade/                           // 升级脚本
│   │       └── static/                                // 静态资源
│   │           └── uploads/                           // 本地文件存储（生产用云存储）
│   └── test/                                          // 测试目录（结构同main）
│       └── java/com/gameworkshop/
│           ├── unit/                                  // 单元测试
│           ├── integration/                           // 集成测试
│           └── mock/                                  // 模拟测试
├── pom.xml                                            // 依赖管理
├── README.md                                          // 项目说明（含架构图）
├── docs/                                              // 文档
│   ├── architecture/                                  // 架构设计文档
│   ├── api/                                           // API文档
│   └── database/                                      // 数据库设计文档
├── .gitignore                                         // Git忽略配置
└── .gitattributes                                     // Git属性配置（换行符等）
```



6.SpringBoot的启动注解是@SpringBootApplication，点开发现里面是主要有另外三个注解来实现，分别用来指定配置以及扫描注解，帮助我们可以通过简单的注解来让spring明白我们的代码架构！



### 7.Spring 的核心是 **IoC（控制反转）容器**（需要复习！！！）



8.git合并分支流程如下：

**执行交互式变基命令**

指定要合并的提交范围：`HEAD~3` 表示 “从当前提交往前数 3 个提交”（包含这 3 个）。

```bash
git rebase -i HEAD~3
```

执行后会弹出一个编辑器（通常是 Vim 或 VS Code），显示类似以下内容

```plaintext
pick 6413cb2 update work log
pick def6425 update document
pick 616d4d5 update the gitattribute
```

**修改提交指令**

将需要合并的提交前的 `pick` 改为 `squash`（或简写 `s`），表示 “将该提交合并到前一个提交”。
例如，保留第一个提交的 `pick`，后面的改为 `squash`

```plaintext
pick 6413cb2 update work log
s def6425 update document
s 616d4d5 update the gitattribute
```

- `pick`：保留该提交
- `squash`：将该提交合并到上一个提交

**保存并退出编辑器**

- 如果是 Vim 编辑器：按 `Esc` 后输入 `:wq` 保存退出。
- 如果是 VS Code：直接编辑后保存关闭窗口。

**编辑合并后的提交信息**

保存后会自动弹出第二个编辑器，显示所有被合并提交的信息，例如：

```plaintext
# This is a combination of 3 commits.
# The first commit's message is:
update work log

# This is the 2nd commit message:
update document

# This is the 3rd commit message:
update the gitattribute
```

删除多余内容，编写一个合并后的清晰信息，例如：

```plaintext
refactor: update documents and git attributes
```

保存退出编辑器。

**推送合并后的提交（如果已推送到远程）**

由于修改了历史提交，需要强制推送（仅在个人分支或确认无他人协作时使用）：

bash

```bash
git push -f origin main
```

注意事项

1. **不要合并已公开的、多人协作的提交**：强制推送会覆盖远程历史，影响其他开发者。
2. **精确选择范围**：如果想合并更早的提交，可将 `HEAD~3` 改为具体的提交哈希（如 `git rebase -i e08f24f`，合并从 `e08f24f` 之后的所有提交）。
3. **撤销操作**：如果中途出错，可执行 `git rebase --abort` 放弃合并，回到操作前的状态。





### 下次任务 0913：

跑通后端，数据库链接，使用新流程方便后续迁移k8s等企业管理，同时预留接口给中间件

后端架构完善，同时根据类图以及功能需求开始开发部分功能

完善和前端的接口，使用postman来测试是否正确访问





------





## 0914-建立后端和数据库链接

### 日志

1.下载Postgresql并优化数据库表架构，sql创建表单

​	管理员名称：postgres 密码：123 port：5432 池监听端口：6432

2.配置developerprofile的基于mybatis架构的后端查询逻辑以及后端和数据库建立链接对应



### 学习摘要

1.复习数据库配置操作以及sql语句

2.复习后端和数据库建立配置

3.了解学习响应式编程以及应用场景，了解Elasticsearch概念，了解druid概念

4.如果使用mybatis，那么需要使用sql来创建表然后构建链接，还有就是关于mapper架构还需要好好学习



### 下次任务

建立前端和后端的链接

实现前后端响应

补充所有数据库表单以及基础方法

实现文件上传下载功能



## 0915-建立后端和数据库链接

### 日志

1.重构了后端代码框架

2.编写单元测试给mapper接口 成功

3.重构数据库属性避免局限性

4.梳理DDD开发设计模式下的后端接口流程

5.完成后端的所有表单以及对应的方法设计和接口实现



### 学习摘要

数据库层面：PostgreSQL 枚举类型的特性与局限

1. **枚举类型的本质**
   - 了解了 PostgreSQL 自定义枚举（如 `game_asset_type`）是**强类型**，即使字符串值匹配（如 `"IMAGES"`），直接传普通字符串（VARCHAR）也会触发「类型不匹配」错误，必须显式转换或通过 ORM 框架适配。
   - 掌握了枚举类型的创建语法（`create type ... as enum (...)`），以及修改字段类型的 SQL（如 `ALTER TABLE ... ALTER COLUMN ... TYPE VARCHAR USING ...`）。
2. **枚举 vs 字符串的取舍逻辑**
   - 明确了枚举的优势（强制值合法性、避免无效数据）和劣势（强绑定、修改需删改枚举类型、跨框架适配复杂）；
   - 知道了字符串（VARCHAR）的灵活性 —— 虽然失去了数据库层的类型约束，但能快速解决 ORM 适配问题，且可通过应用层校验（如常量集合 `VALID_ASSET_TYPES`）弥补合法性检查。





「领域驱动设计（DDD）分层架构」的落地能力

你不仅理解了 DDD 的分层思想，更通过代码实现了各层的职责边界，这是后端架构设计的核心：

1. **领域层（domain）**：聚焦核心业务逻辑，是系统的 “灵魂”
   - 定义领域模型（如`Project`、`FileInfo`），封装业务属性与行为（如项目状态流转、文件校验）；
   - 通过「领域仓库接口」（如`ProjectRepository`）定义数据操作契约，隔离业务逻辑与数据访问；
   - 用「领域服务」（如`ProjectDomainService`）处理跨实体的复杂业务规则（如 “同一开发者不能创建同名项目”）。
2. **应用服务层（application）**：做 “业务编排”，不包含核心逻辑
   - 接收接口层的 DTO，转换为领域模型后调用领域层；
   - 协调外部接口调用（如调用用户服务分配权限）与内部业务（如创建项目），处理事务一致性（如`@Transactional`保证 “创建项目 + 分配权限” 要么都成功，要么都回滚）；
   - 定义跨层交互的 DTO（`request`/`response`），避免领域模型暴露到外部。
3. **基础设施层（infrastructure）**：做 “能力支撑”，不侵入业务
   - 实现领域仓库接口（如`ProjectRepositoryImpl`用 MyBatis 操作数据库），将数据访问逻辑与领域层解耦；
   - 封装通用工具（如 JWT、文件存储、外部接口客户端），提供无业务侵入的基础能力（如 Feign 客户端、OSS 工具类）；
   - 管理全局配置（如 MyBatis、Redis、Security）和异常处理（全局异常处理器），保证系统稳定性。
4. **接口层（interfaces）**：做 “对外适配”，是系统的 “窗口”
   - 暴露 REST 接口（如`ProjectController`），接收外部请求（前端 / 其他服务）；
   - 负责参数校验（如`@NotBlank`）、DTO 转换（外部请求→应用服务入参）、响应封装（如用`ResultUtil`统一返回格式）；
   - 通过 Swagger 生成接口文档，降低外部对接成本。





「数据库交互与测试」的严谨实践

你掌握了从 “表设计” 到 “单元测试” 的全流程，确保数据操作的正确性：

1. **表结构设计**：遵循业务约束
   - 用外键保证数据完整性（如`dev_game.developer_profile_id`关联`developer_profile.id`）；
   - 合理设置非空约束（`NOT NULL`）、默认值（如`created_at DEFAULT NOW()`）、唯一索引（如 “同一开发者不能有同名游戏”）。
2. **MyBatis 实操**：规避常见坑点
   - 编写 XML 映射文件时，避免更新非空字段为`null`（如`updateById`不包含`created_at`）；
   - 通过`@Param`处理多参数查询，用索引优化查询性能（如`idx_dev_game_developer`）。
3. **单元测试设计**：保证测试独立性与覆盖度
   - 遵循 “先插父表、再插子表” 的顺序准备测试数据，避免外键约束错误；
   - 用`@BeforeEach`/`@AfterEach`实现测试数据的自动准备与清理，确保用例间无干扰；
   - 覆盖 CRUD 全场景（插入、查询、更新、删除），用断言验证结果（如`assertEquals`、`assertNotNull`）

### 下次任务

实现前端接口的设计

测试前端端口是否正常响应

测试微服务架构下前端是否能更新数据上传数据

接口文档自动生成



## 0922

### 日志

开发前端和后端接口

完成前后端下载文件功能

### 学习摘要

1.基于restful的前后端响应流程

 **Controller → Application Service → Domain → Repository → Mapper → MySQL**

2.xml和使用注解不能同时定义会冲突

3.访问http://localhost:8080/api/assets/download/asset-001可以正常下载文件内容！成功捏

4.最好在application配置@MapperScan("com.gameworkshop.infrastructure.persistence.mybatis.mapper") 这样可以读取到mabtis配置文件

### 下次任务

更新用户上传功能
