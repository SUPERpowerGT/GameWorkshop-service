# 开发日志

## 0913-项目初始化

1.注意包名称要小写，不要用段横杠，如果重命名，请务必要全部重构



2.如果项目没有gravel或者maven可以从spring init网站初始化一个项目包，并将包的内容复制到本地，不要忘记重新更新gitignore，具体操作如下：

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



## 0925

### 日志

### 学习摘要

上传游戏功能流程图

```
前端 (React/Android)
   |
   V
[Controller] DevGameUploadController
   |
   V
[Application Service] DevGameApplicationService
   |
   +--> 调用 [Domain Repository] DeveloperProfileRepository (查开发者)
   |
   +--> 调用 [Domain Repository] DevGameRepository (保存游戏)
   |
   +--> 调用 [Domain Repository] DevGameAssetRepository (保存资源)
   |
   V
[Infrastructure]
   ├── DeveloperProfileRepositoryImpl  -> DeveloperProfileMapper -> developer_profile 表
   ├── DevGameRepositoryImpl           -> DevGameMapper          -> dev_game 表
   └── DevGameAssetRepositoryImpl      -> DevGameAssetMapper     -> dev_game_asset 表
   |
   V
数据库 (MySQL) + 文件系统 (D:/Project/GameWorkshop/game-assets/)

```

上传接口测试：

![image-20250926212245440](C:\Users\12912\AppData\Roaming\Typora\typora-user-images\image-20250926212245440.png)

### 下次任务



## 1006

| 🧱 阶段 1（当前） | 两个 Spring Boot + 前端写死 URL | 简单易调试，开发方便 |
| ---------------- | ------------------------------- | -------------------- |
|                  |                                 |                      |

| 🚪 阶段 2 | ✅ 加一个 **API Gateway（Spring Cloud Gateway 或 Nginx）** | 统一路由入口 |
| -------- | --------------------------------------------------------- | ------------ |
|          |                                                           |              |

| 🧭 阶段 3 | ✅ 加上 **Eureka / Nacos 服务注册中心** | 动态发现服务，不再写死端口 |
| -------- | -------------------------------------- | -------------------------- |
|          |                                        |                            |

| 🔐 阶段 4 | ✅ 加上 **Spring Cloud Config + JWT Auth Service** | 统一配置 & 认证授权 |
| -------- | ------------------------------------------------- | ------------------- |
|          |                                                   |                     |

| ☁️ 阶段 5 | ✅ Docker Compose 或 Kubernetes | 容器化部署，服务独立伸缩 |
| -------- | ------------------------------ | ------------------------ |
|          |                                |                          |



## 1009

### 计划

完善前端界面设计 完成

完成部分前端功能开发 完成

### 日志

使用Figma设计前端界面

https://www.figma.com/design/sSbGlIYwOBLfjIQ0sC9y75/devloper?node-id=1-1045

更新前端静态界面



## 1010

### 计划

合并后端

验证前后端功能



## 1011

### 计划

实现微服务架构设计

开发异步功能（kafuka）完成

实现切片功能（上传下载）



## 1012

### 计划

计划下一步（接口设计）可拓展性 

### 学习摘要

JWT 的实现逻辑

| 步骤         | 参与者                   | 操作                                         | 使用的密钥 |
| ------------ | ------------------------ | -------------------------------------------- | ---------- |
| ① 用户登录   | 前端 → Auth 服务         | 提交账号密码                                 | —          |
| ② 签发 JWT   | Auth 服务                | ✅ 用 **私钥** 签名生成 JWT                   | 🔐 私钥     |
| ③ 保存 Token | 前端                     | 把 JWT 存入 localStorage / cookie            | —          |
| ④ 发请求     | 前端 → Developer-Service | 在 Header 加上 `Authorization: Bearer <JWT>` | —          |
| ⑤ 验证 JWT   | Developer-Service        | ✅ 用 **公钥** 验证签名真伪                   | 🔓 公钥     |
| ⑥ 通过验证   | Developer-Service        | 提取 `sub`（userId）、`role` 信息            | —          |

1. 如何第一次创建公钥和私钥？

使用 openssl 一次性生成即可：

```
openssl genrsa -out rsa-private.pem 2048
openssl rsa -in rsa-private.pem -pubout -out rsa-public.pem
```

私钥放在 Auth 服务（认证服务）内部，公钥可以复制到其他服务。



2. 登录时为什么没有 JWT？

第一次登录确实没有 JWT。
 因为用户还没认证，`/auth/login` 接口不需要验证。
 只有登录成功后，服务端才会生成 JWT。



3. 后端登录成功后如何返回 JWT？

认证服务验证账号密码正确后，用私钥签发 JWT 并返回给前端：

```
@PostMapping("/login")
public Map<String, String> login(@RequestBody LoginRequest req) {
    String token = jwtProvider.generateToken(req.getUsername());
    return Map.of("token", token);
}

```



4. 前端如何存储 JWT？

登录成功后，前端将 JWT 保存到浏览器的 localStorage：

```
localStorage.setItem("auth_token", token);
```

之后每次请求自动从 localStorage 取出：

```
fetch("/api/devgames", {
  headers: { Authorization: `Bearer ${localStorage.getItem("auth_token")}` },
});
```



5. JWT 是如何自动附加在请求里的？

在 `apiClient` 封装中添加了逻辑：
 如果存在 token，则自动在请求头中加上 `Authorization: Bearer <token>`。

```
headers: {
  ...(token && { Authorization: `Bearer ${token}` }),
}
```



6. 其他微服务如何验证 JWT？

其他服务（如 Developer-Service）只需要公钥验证真伪：

```
Claims claims = Jwts.parserBuilder()
    .setSigningKey(publicKey)
    .build()
    .parseClaimsJws(token)
    .getBody();
```

验证通过后就能获取 `userId`、`role`。



7. 公钥如何分发给其他服务？

有两种方式：

**方式一（简单）**：手动拷贝公钥文件到每个服务的 `resources/keys/`。

**方式二（动态）**：认证服务提供 `/auth/public-key` 接口，其他服务启动时拉取。



8. 为什么要用公钥 + 私钥，而不是同一个密钥？

因为公私钥是非对称加密：
 只有认证服务能“签发”，其他服务只能“验证”。
 就算其他服务泄露，也无法伪造 JWT。



9. JWT 在系统中的整体流程

```
1️⃣ 用户登录 → Auth 服务验证账号
2️⃣ Auth 服务用私钥生成 JWT → 返回前端
3️⃣ 前端保存 JWT 到 localStorage
4️⃣ 前端调用任意接口时带上 Authorization: Bearer <token>
5️⃣ 各微服务用公钥验证 JWT 签名真伪
6️⃣ 验证成功 → 提取 userId / role → 执行业务逻辑
```



#### 重构upload方法

| 模块              | 功能                                                         | 当前状态                                                     |
| ----------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Auth-Service      | 负责登录注册、签发 JWT（用私钥签名）                         | ✅ 已有逻辑                                                   |
| Developer-Service | 负责开发者上传游戏、管理游戏                                 | ✅ 现在增加了 JWT 验证                                        |
| JWT 验证逻辑      | 使用公钥校验签名，确保请求来自合法用户                       | ✅ 新增 `KeyConfig`, `JwtTokenProvider`, `JwtAuthFilter`, `SecurityConfig` |
| 数据库层          | 通过 `developerProfileRepository.findByUserId()` 获取开发者信息 | ✅ 已验证逻辑                                                 |
| Controller 层     | 不再接收 `developerId` 参数，而是根据 JWT 自动解析           | ✅ 逻辑安全、简洁                                             |
| 前端部分          | 登录时拿到 token 存在 `localStorage`，并自动附加到请求头     | ✅ 已集成 `AuthProvider` + `apiClient`                        |
| 安全边界          | 只有携带合法 JWT 的请求才能访问 `/api/developer/**`          | ✅ Spring Security 已控制                                     |



你这几天其实已经完成了一整套非常完整的 **JWT 鉴权体系 + DevGame 上传流程重构**。
 我帮你从「整体架构 → 后端逻辑 → 前端配合 → 安全机制」四个维度给你总结清晰，方便你写入报告或项目文档。

🧱 一、总体架构成果（GameWorkshop-service 模块）

你现在的 **Developer 微服务** 已经完成了一个完整的认证 + 授权闭环：

```
┌──────────────────────────────┐
│        Next.js 前端（3000）       │
│  ↳ 登录后存储 JWT(Token)         │
│  ↳ 上传游戏 / 查看游戏            │
│  ↳ 请求头加 Authorization: Bearer │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│  Spring Boot 后端（8081）      │
│  - JwtAuthFilter 校验 Token   │
│  - 提取 userId 注入上下文      │
│  - DevGameUploadController   │
│  - 自动为 user 创建 DeveloperProfile │
└──────────────────────────────┘
               │
               ▼
         PostgreSQL + Redis
```

🔐 二、JWT 验证体系（Security 模块）

你新增并整合了一整套标准的 JWT 安全体系 👇：

| 类名                   | 作用                       | 说明                                               |
| ---------------------- | -------------------------- | -------------------------------------------------- |
| **`KeyConfig`**        | 加载 `rsa-public.pem` 公钥 | 负责解析公钥字符串生成 `PublicKey`                 |
| **`JwtTokenProvider`** | 校验和解析 JWT             | 验证签名、提取 userId（从 `sub` 字段）             |
| **`JwtAuthFilter`**    | Spring Security 过滤器     | 拦截所有请求 → 校验 token → 注入 `SecurityContext` |
| **`SecurityConfig`**   | 安全策略配置               | 关闭 CSRF、启用无状态会话、注册过滤器链            |

💡 效果：

- 所有 `/api/**` 请求都会被 `JwtAuthFilter` 检查；
- 通过后，`SecurityContextHolder` 中自动保存当前 `userId`；
- Controller 可直接用 `@AuthenticationPrincipal String userId` 拿到用户身份。

🧩 三、DevGame 上传控制器重构成果

你彻底重构了 `DevGameUploadController`：
 从「手动传 developerId」→ 「自动从 JWT 获取 userId」。

新版本关键逻辑：

```
@PostMapping("/upload")
public ResponseEntity<DevGameResponse> uploadGame(
        @AuthenticationPrincipal String userId,
        ...
) {
    // ✅ 如果该用户还没有 DeveloperProfile，则自动创建
    String developerId = developerProfileRepository.findByUserId(userId)
            .map(DeveloperProfile::getId)
            .orElseGet(() -> {
                DeveloperProfile profile = new DeveloperProfile(UUID.randomUUID().toString(), userId, 0);
                developerProfileRepository.save(profile);
                return profile.getId();
            });

    // ✅ 继续正常上传流程
    ...
}
```

💡 优化点：

- 再也不用前端传 `developerId`
- 用户首次上传游戏时会**自动注册开发者档案**
- 完全避免「跨服务依赖 user-service」的问题

🧠 四、数据库交互与日志验证

你通过 MyBatis-Plus 成功执行了以下 SQL 交互：

✅ 上传游戏插入日志：

```
INSERT INTO dev_game (id, developer_profile_id, name, description, ...)
INSERT INTO dev_game_asset (...)
UPDATE developer_profile SET project_count = ?
```

✅ 调用链完整：

```
Controller → ApplicationService → Repository → MyBatis Mapper → PostgreSQL
```

✅ 日志中能看到：

```
successfully loaded public key
Token validated successfully
INSERT INTO dev_game_asset ...
```

💪 意味着整个数据库事务流、认证流都在正常工作。

💻 五、前端整合与调用

前端部分也已经实现完美联动：

| 文件                 | 功能          | 关键实现                                           |
| -------------------- | ------------- | -------------------------------------------------- |
| **`useDevGames.ts`** | 上传逻辑 Hook | 调用 `devgamesApi.upload()` 进行游戏上传           |
| **`devgamesApi.ts`** | 封装 API 调用 | 发送 FormData（包含 image / video / zip）          |
| **`client.ts`**      | 通用请求封装  | 自动在 header 加上 `Authorization: Bearer <token>` |

👉 登录成功后，前端会在 localStorage 存 `auth_token`
 👉 后续上传请求自动带 JWT
 👉 后端识别 `userId` 并授权操作

🧩 六、跨域 & 调试问题解决

你正确配置了 CORS 支持：

```
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

✅ 允许前端跨域调用
 ✅ 允许携带 JWT Header
 ✅ 配合 SecurityFilterChain 完美兼容

🔒 七、安全机制总结

| 环节         | 内容                                                 |
| ------------ | ---------------------------------------------------- |
| **认证**     | 用户登录时由 `auth-service` 签发 JWT（RSA 私钥签名） |
| **授权**     | Developer-service 使用 `JwtAuthFilter` 验证公钥      |
| **会话**     | 无状态（Stateless）机制，无需 Session                |
| **失败处理** | Token 无效/过期 → 返回 401，由前端处理               |
| **未来优化** | 可添加 Refresh Token 自动刷新机制                    |

🚀 八、目前系统状态总结

| 模块                        | 状态       | 说明                     |
| --------------------------- | ---------- | ------------------------ |
| 🧩 JWT 校验链                | ✅ 完成     | RSA 公钥验证通过         |
| 🔐 Security 过滤器           | ✅ 完成     | 每次请求自动鉴权         |
| 💾 DevGame 上传              | ✅ 完成     | 正常写入数据库和文件系统 |
| 👤 DeveloperProfile 自动创建 | ✅ 完成     | 自动建档                 |
| 🌐 CORS & 请求链             | ✅ 完成     | 前后端可直连             |
| ⏱ Token 过期刷新            | 🚧 可选优化 | 可后期加入 Refresh Token |

📘 九、可写入报告/展示的总结语句

> 本阶段实现了基于 **JWT + RSA 公钥** 的用户认证机制，并完成了与游戏上传功能的深度整合。
>  系统支持用户在前端登录后，通过携带 JWT 自动完成身份识别与开发者档案的动态创建。
>  整个流程无状态、可扩展，为后续微服务拆分与统一网关鉴权打下了基础。





## 1014

### 计划

完成所有前后端代码，实现所有功能

- my game 展示功能  完成
- my game game 删除功能 完成
- game hub 展示功能
- game hub 详情界面下载功能
- game hub 热门下载展示功能
- 
- dashboard 查看游戏下载次数功能

### 学习摘要

#### my game展示功能

##### 后端开发

```
+---------------------------+           +-------------------------------+
| 🎮 前端（Next.js）        |           | 🧱 后端（Spring Boot）         |
+---------------------------+           +-------------------------------+
          │                                            │
          │ ① 用户点击 "My Games"                     │
          │──────────────────────────────────────────▶│
          │                                            │
          │ ② 发送请求 (带JWT)                        │
          │   GET /api/developer/devgame/my           │
          │   Authorization: Bearer <token>           │
          │──────────────────────────────────────────▶│
          │                                            │
          │                                            ▼
          │                          [JwtAuthFilter] 验证JWT → 获取 userId
          │                                            │
          │                                            ▼
          │                  [DeveloperProfileRepository.findByUserId()]
          │                                            │
          │                                            ▼
          │              [DevGameRepository.findByDeveloperProfileId()]
          │                                            │
          │                                            ▼
          │             [DevGameAssetRepository.findFirstByGameIdAndType("image")]
          │                                            │
          │                                            ▼
          │            拼接 imageUrl = /api/assets/download/{assetId}
          │                                            │
          │                                            ▼
          │        ③ 返回 JSON 列表 (List<DevGameSummaryResponse>)
          │──────────────────────────────────────────◀│
          │
          │ ④ 前端渲染：
          │    - 游戏封面 imageUrl
          │    - 游戏名 name
          │    - 描述 description
          │    - 上传时间 createdAt
          │
          │ 用户看到「我的游戏列表」                    │
          │                                            │
          │                                            ▼
          │    ⑤ 当浏览器加载封面图时：                │
          │      访问 /api/assets/download/{assetId}   │
          │──────────────────────────────────────────▶│
          │                                            │
          │                      [DevGameAssetDownloadController]
          │                       从数据库查 asset → 从磁盘读取文件
          │                                            │
          │                                            ▼
          │               返回文件流 (Content-Type: image/jpeg)
          │──────────────────────────────────────────◀│
          │                                            │
          │ ⑥ 浏览器显示封面图                        │
          ▼                                            ▼
     🎨 My Games 页面加载完成 ✅                 🗃️ 数据一致

```

这里注意，需要在security配置下载图片权限，因为一般静态资源是不会发送jwt的



分离式加载会更快更方便拓展 对比一次性返回json文件

| 对比维度                     | **单次返回（Base64嵌入）**                       | **分离式加载（当前GameVault实现）**                     |
| ---------------------------- | ------------------------------------------------ | ------------------------------------------------------- |
| **请求数量**                 | 1 次（一次性返回所有数据 + 图片）                | 多次（JSON一次 + 多个图片独立请求）                     |
| **数据体积**                 | 非常大（JSON 内嵌 Base64 图片，通常放大 30–40%） | 小得多（JSON 轻量，图片独立传输）                       |
| **加载方式**                 | 顺序加载（必须等所有数据到齐才能渲染）           | 并发加载（JSON 与图片可同时加载）                       |
| **首屏渲染速度**             | 慢（阻塞渲染）                                   | 快（JSON 到即可渲染框架，图片渐进加载）                 |
| **浏览器缓存**               | ❌ 无法缓存单张图片（每次都重新下载）             | ✅ 每张图片独立缓存，可命中 CDN 或浏览器缓存             |
| **带宽利用率**               | 差（浪费重复传输）                               | 高（仅传变化部分）                                      |
| **可扩展性**                 | 差（难以迁移至 CDN 或对象存储）                  | 优（图片可托管至 OSS / S3 / CDN）                       |
| **前后端解耦**               | 弱（前端需解码图片数据）                         | 强（图片走标准 HTTP 流，后端职责单一）                  |
| **错误恢复能力**             | 差（图片损坏需重传整个 JSON）                    | 好（单张图加载失败不会影响整体页面）                    |
| **SEO 与可访问性**           | 弱（图片不可索引）                               | 强（图片 URL 可独立被索引）                             |
| **总体性能评分（企业推荐）** | ❌ 仅适合小型内嵌内容                             | ✅ 企业级架构通用模式（Bilibili / Steam / YouTube 同款） |

功能验证

使用 Postman 进行 JWT 鉴权与接口测试

一、实验目标

通过 Postman 验证 **GameVault 平台后端（Auth-Service + Developer-Service）** 的 JWT 登录与鉴权机制，确保：

- 用户可在 Auth-Service 登录并获取合法 Token。
- Developer-Service 能正确识别并校验 JWT。
- 未携带或无效 Token 的请求被拒绝访问。

二、测试工具与环境

| 项目                   | 配置                                   |
| ---------------------- | -------------------------------------- |
| 工具                   | Postman 10.x                           |
| Auth-Service 端口      | `8080`                                 |
| Developer-Service 端口 | `8081`                                 |
| 用户示例               | `username: 123456`, `password: 123456` |
| Token 类型             | JWT（RS256 非对称加密）                |

三、测试步骤

1️⃣ 登录并获取 JWT

**请求：**

```
POST http://localhost:8080/api/auth/login
```

**请求体：**
 在 Postman 中选择 **Body → raw → JSON**，填写：

```
{
  "username": "123456",
  "password": "123456"
}
```

**请求头：**

```
Content-Type: application/json
```

**成功响应示例：**

```
{
  "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": 2,
  "username": "123456",
  "email": "123456@qq.com",
  "message": "Login successful"
}
```

> ✅ 表示 Auth-Service 登录逻辑和 JWT 签发成功。
>  生成的 Token 会用于后续请求。

2️⃣ 使用 Token 访问受保护接口

**请求：**

```
GET http://localhost:8081/api/developer/devgame/my
```

**在 Postman 中配置 Authorization：**

1. 切换到 **Authorization** 标签页
2. Type 选择 **Bearer Token**
3. 在 **Token** 一栏中粘贴登录接口返回的完整 JWT

示例：

```
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
```

**成功响应：**

```
[
  {
    "id": "a6421fa5-5eb1-4e4b-bc70-92b975bec656",
    "name": "game1012test",
    "description": "test",
    "releaseDate": "2025-10-21T16:00:00",
    "createdAt": "2025-10-12T09:38:20.384772",
    "imageUrl": "http://localhost:8081/api/assets/download/95feba1c-0a52-42f9-be20-74fffa8b4213"
  },
  {
    "id": "0de81851-c54b-4448-b6ad-18bfd02a9d8c",
    "name": "game1012",
    "description": "test",
    "releaseDate": "2025-10-27T16:00:00",
    "createdAt": "2025-10-12T08:23:48.359629",
    "imageUrl": "http://localhost:8081/api/assets/download/4032b2d0-c6bc-4c6f-93f1-ef50073a312e"
  }
]
```

> ✅ 表示 Developer-Service 成功从 JWT 中解析出 `userId`，
>  并正确返回当前开发者的游戏数据。

3️⃣ 异常验证

| 场景         | 操作                  | 预期结果                |
| ------------ | --------------------- | ----------------------- |
| ❌ 无 Token   | 删除 Authorization 头 | 返回 `401 Unauthorized` |
| ❌ Token 错误 | 修改 Token 任意字符   | 返回 `403 Forbidden`    |
| ✅ 正常 Token | 保持完整 Token        | 返回 `200 OK` 与数据    |

四、调用流程示意图

```
sequenceDiagram
    participant User as 前端 / Postman
    participant AuthService as Auth-Service (8080)
    participant DevService as Developer-Service (8081)
    participant DB as Database

    User->>AuthService: POST /api/auth/login (username, password)
    AuthService-->>User: 返回 JWT Token
    User->>DevService: GET /api/developer/devgame/my<br>Authorization: Bearer {token}
    DevService->>DevService: ✅ 公钥验证签名
    DevService->>DB: 根据 userId 查询 DeveloperProfile
    DevService-->>User: 返回游戏列表与封面链接
```

五、测试结果总结

| 测试项       | 结果   | 说明                                  |
| ------------ | ------ | ------------------------------------- |
| 登录认证     | ✅ 通过 | 用户凭证正确返回 JWT                  |
| JWT 签发     | ✅ 通过 | 使用私钥签名成功                      |
| Token 校验   | ✅ 通过 | Developer-Service 使用公钥验证        |
| 访问保护资源 | ✅ 通过 | 能正确返回开发者的游戏数据            |
| 未授权访问   | ✅ 拒绝 | 返回 401/403                          |
| 安全边界     | ✅ 有效 | 仅合法 JWT 可访问 `/api/developer/**` |

六、结论与心得

通过 Postman 实验验证，系统的 **JWT 单点登录 + 微服务鉴权机制** 工作正常：

- 登录接口能正确签发带用户标识的 Token；
- Developer-Service 能通过公钥验证 Token，有效区分合法用户；
- Spring Security 配置实现了对 `/api/developer/**` 的访问控制；
- Postman 可作为快速验证登录与授权逻辑的开发辅助工具。

> ✅ **结论：**
>  GameVault 的分布式认证与授权体系已闭环完成。
>  用户登录一次即可安全访问开发者模块的受保护资源。

![image-20251014043815058](C:\Users\12912\AppData\Roaming\Typora\typora-user-images\image-20251014043815058.png)

![image-20251014043838629](C:\Users\12912\AppData\Roaming\Typora\typora-user-images\image-20251014043838629.png)

##### 前端开发

g老师帮助hhh

##### 测试



#### my game 删除功能

##### 后端开发

```
+---------------------------+           +-------------------------------+
| 🎮 前端（Next.js）        |           | 🧱 后端（Spring Boot）         |
+---------------------------+           +-------------------------------+
          │                                            │
          │ ① 用户点击「Delete」按钮                   │
          │──────────────────────────────────────────▶│
          │                                            │
          │ ② 弹出确认框 Modal.confirm("Confirm?")    │
          │                                            │
          │ ③ 用户确认后发送请求                      │
          │   DELETE /api/developer/devgame/{gameId}   │
          │   Authorization: Bearer <token>            │
          │──────────────────────────────────────────▶│
          │                                            │
          │                                            ▼
          │                       [JwtAuthFilter] 验证JWT → 获取 userId
          │                                            │
          │                                            ▼
          │                       [DevGameDeleteController.deleteGame()]
          │                                            │
          │                                            ▼
          │                       [DevGameApplicationService.deleteGame()]
          │                                            │
          │                                            ▼
          │                     检查游戏是否存在 existsById(gameId)
          │──────────────────────────────────────────▶│
          │                                            │
          │                          SELECT COUNT(*) FROM dev_game WHERE id=?
          │                                            │
          │                                            ▼
          │                             存在 → 返回 true
          │──────────────────────────────────────────◀│
          │                                            │
          │                     删除关联资源 deleteByGameId(gameId)
          │──────────────────────────────────────────▶│
          │                                            │
          │             DELETE FROM dev_game_asset WHERE dev_game_id=?
          │──────────────────────────────────────────▶│
          │                                            │
          │                     删除游戏主体 deleteById(gameId)
          │──────────────────────────────────────────▶│
          │                                            │
          │             DELETE FROM dev_game WHERE id=?
          │──────────────────────────────────────────▶│
          │                                            │
          │ 返回 OperationResult(success=true, message="Game deleted successfully")
          │──────────────────────────────────────────◀│
          │                                            │
          │ ④ 后端响应                                │
          │   HTTP 200 + JSON                         │
          │   {"success": true, "message": "Game deleted successfully"}  
          │──────────────────────────────────────────◀│
          │                                            │
          │ ⑤ 前端处理响应                            │
          │    message.success("Deleted successfully") │
          │    fetchMyGames() 刷新列表                 │
          │                                            │
          │ ⑥ 页面重新渲染：被删游戏从列表中消失 ✅     │
          ▼                                            ▼
     🎨 My Games 页面更新完成 ✅                🗃️ 数据库已同步删除 ✅

```

![image-20251014062123402](C:\Users\12912\AppData\Roaming\Typora\typora-user-images\image-20251014062123402.png)



## 1015

### 计划

完成所有前后端代码，实现所有功能

- my game game 编辑功能 完成
- game hub 展示功能
- game hub 详情界面下载功能
- game hub 热门下载展示功能
- dashboard 查看游戏下载次数功能

### 学习摘要

#### my game game 编辑功能

```
+--------------------------------------+           +----------------------------------------+
| 🎮 前端（Next.js / React + AntD）     |           | 🧱 后端（Spring Boot / DDD 架构）        |
+--------------------------------------+           +----------------------------------------+
             │                                            │
             │ ① 用户进入 MyGames 页面                    │
             │──────────────────────────────────────────▶│
             │                                            │
             │ ② 调用接口：                               │
             │   GET /api/developer/devgame/my            │
             │   Header: Authorization: Bearer <JWT>      │
             │──────────────────────────────────────────▶│
             │                                            ▼
             │                          [JwtAuthFilter] 验证JWT → 获取 userId
             │                                            │
             │                                            ▼
             │                [DeveloperProfileRepository.findByUserId()]
             │                                            │
             │                                            ▼
             │             [DevGameRepository.findByDeveloperProfileId()]
             │                                            │
             │                                            ▼
             │  查询封面： [DevGameAssetRepository.findFirstByGameIdAndType("image")]
             │                                            │
             │                                            ▼
             │     拼接 imageUrl = /api/developer/devgameasset/download/{assetId}
             │                                            │
             │                                            ▼
             │   ③ 返回 List<DevGameSummaryResponse> (JSON)
             │──────────────────────────────────────────◀│
             │                                            │
             │ ④ 前端渲染游戏卡片 GameCard：              │
             │    - 封面 imageUrl                         │
             │    - 名称 name                             │
             │    - 描述 description                      │
             │    - 创建时间 createdAt                    │
             │                                            │
             │                                            ▼
             │ 用户点击 “Edit”                            │
             │──────────────────────────────────────────▶│
             │                                            │
             │ ⑤ 跳转至 /dashboard/developer/edit/{id}   │
             │                                            │
             │                                            ▼
             │ useEffect → GET /api/developer/devgame/{id}│
             │──────────────────────────────────────────▶│
             │                                            ▼
             │                   [DevGameQueryApplicationService.queryDevGameDetails()]
             │                       ├─ DevGameRepository.findById()
             │                       ├─ DevGameAssetRepository.findByGameIdAndType("image"/"video"/"zip")
             │                       └─ AssetUrlBuilder.buildDownloadUrl(assetId)
             │                                            │
             │                                            ▼
             │   ⑥ 返回 DevGameResponse (完整详情 JSON)    │
             │──────────────────────────────────────────◀│
             │                                            │
             │ ⑦ 前端自动填充表单：                       │
             │     name, description, releaseDate, assets │
             │                                            │
             │                                            ▼
             │ 用户修改后点击 “Update Game”                │
             │──────────────────────────────────────────▶│
             │                                            │
             │ ⑧ PUT /api/developer/devgame/{id}          │
             │    FormData: name, description, image, ... │
             │──────────────────────────────────────────▶│
             │                                            ▼
             │                   [DevGameApplicationService.updateGame()]
             │                     ├─ 更新 DevGame 基本信息
             │                     ├─ 如果上传新文件 → 保存至磁盘 / S3
             │                     ├─ 更新 DevGameAsset 表记录
             │                     └─ 返回新的 DevGameResponse
             │                                            │
             │                                            ▼
             │ ⑨ 返回更新后的游戏详情 JSON                │
             │──────────────────────────────────────────◀│
             │                                            │
             │ ⑩ 前端 message.success() + 跳转回 MyGames │
             │──────────────────────────────────────────▶│
             │                                            │
             ▼                                            ▼
       🎨 MyGames 页面刷新 ✅                   🗃️ 数据与文件更新 ✅

```

这里需要注意一些设计点

- DDD命名规范
- download asset url拼接工具卸载interface util外面，同时把相关配置放到yml里面
- 上传存储的url和下载的url是不一样的oh
- 注意更新的两种方式一种是覆盖一种是新增，建议新增（注意查询要查询最新的），更高拓展性（注意定期清理数据库以及文件存储）
- 这里注意upload和edit存储位置是一样的，都用userid作为存储文件目录管理
- 注意前后端需要针对文件类型进行限制以及大小限制（后续需要拓展大型文件切片功能）

#### 后续优化

| 分类         | 优化点                                     | 描述                                 |
| ------------ | ------------------------------------------ | ------------------------------------ |
| 💾 存储层优化 | 使用 S3 / MinIO 替代本地存储               | 支持大文件分块上传，自动 CDN 加速    |
| 🧹 资源清理   | 删除游戏时同步删除磁盘文件                 | 当前只删除 DB 记录，可加文件清理任务 |
| ⚡ 前端优化   | 编辑页预览旧资源                           | 在 Edit 页中显示当前图片、视频缩略图 |
| 🧑‍💼 权限校验  | 验证 `userId` 与 `game.developerProfileId` | 防止越权修改他人游戏                 |
| 📈 Dashboard  | 增加游戏浏览量、下载量统计                 | 可配合 Redis 缓存或分析模块          |



## 1016

### 计划

- game hub 展示功能 完成
- game hub 详情界面下载功能
- game hub 热门下载展示功能
- dashboard 查看游戏下载次数功能

### 学习摘要

game hub 展示功能

后端一定要使用分页查询诺，不然太多会堵塞w

```
+--------------------------------------+           +----------------------------------------+
| 🎮 前端（Next.js / React + AntD）     |           | 🧱 后端（Spring Boot / DDD 架构）        |
+--------------------------------------+           +----------------------------------------+
             │                                            │
             │ ① 用户进入 GameHub 页面                   │
             │──────────────────────────────────────────▶│
             │                                            │
             │ ② 初始化加载第一页                        │
             │   GET /api/developer/devgame/public/all?page=1&pageSize=12
             │──────────────────────────────────────────▶│
             │                                            ▼
             │        [DevGamePublicController.listAllGames()]
             │             └─ 调用 DevGameQueryApplicationService.listAllGames(page, pageSize)
             │                                            │
             │                                            ▼
             │        [DevGameRepository.findAllPaged(offset, pageSize)]
             │                 offset = (page - 1) * pageSize
             │                                            │
             │                                            ▼
             │        [DevGameRepository.countAll()] → 获取总数 totalCount
             │                                            │
             │                                            ▼
             │        [DevGameAssetRepository.findFirstByGameIdAndType("image")]
             │             └─ 拼接封面图 URL（/assets/{userId}/{gameId}/{file}）
             │                                            │
             │                                            ▼
             │   ③ 组装分页响应 DevGameListResponse：     │
             │     {
             │        games: [...],
             │        currentPage: 1,
             │        pageSize: 12,
             │        totalCount: 48,
             │        totalPages: 4
             │     }
             │──────────────────────────────────────────◀│
             │                                            │
             │ ④ 前端接收数据：更新 UI                   │
             │    - 渲染第一页游戏卡片                    │
             │    - 显示分页控件 (Page 1 / 4)            │
             │──────────────────────────────────────────▶│
             │                                            │
             │ ⑤ 用户点击 “下一页”                       │
             │──────────────────────────────────────────▶│
             │                                            │
             │ ⑥ 发起下一页请求                          │
             │   GET /api/developer/devgame/public/all?page=2&pageSize=12
             │──────────────────────────────────────────▶│
             │                                            ▼
             │        [listAllGames(page=2)]              │
             │             ├─ findAllPaged(offset=12, size=12)
             │             ├─ countAll()
             │             └─ 拼接封面URL                 │
             │                                            │
             │                                            ▼
             │   ⑦ 返回 Page 2 JSON 响应                 │
             │──────────────────────────────────────────◀│
             │                                            │
             │ ⑧ 前端合并数据 or 替换页面                │
             │    - 追加到已有列表（无限滚动模式）        │
             │    - 或直接替换成新一页（分页模式）        │
             │                                            │
             │                                            ▼
             │ 用户继续浏览 / 下拉加载更多                │
             │──────────────────────────────────────────▶│
             │                                            │
             ▼                                            ▼
     🎨 GameHub 页面动态渲染 ✅              🗃️ 分页查询稳定返回 ✅

```

![image-20251016024638290](C:\Users\12912\AppData\Roaming\Typora\typora-user-images\image-20251016024638290.png)



## 1019

### 计划

- game hub 热门下载展示功能
- dashboard 查看游戏下载次数功能

### 学习摘要

#### 数据库重构

🧩 一、目标

我们要的是 👇

| 项目       | 值                                      |
| ---------- | --------------------------------------- |
| 容器名     | `gamevault_developer_postgres`          |
| 镜像       | `postgres:16`                           |
| 数据库名   | `gamevault_developer_db`                |
| 用户名     | `gamevault_developer_user`              |
| 密码       | `gamevault_developer_pass`              |
| 宿主机端口 | **12010**（Spring Boot、IDEA 都用这个） |

🧱 二、文件结构

请确保你的项目结构中有：

```
GameWorkshop-service/
 ├─ docker/
 │   └─ postgres/
 │        └─ data/       ← 持久化目录
 ├─ docker-compose.yml
 └─ src/...
```

如果有旧的 data 目录，**务必删除**：

```
rm -rf ./docker/postgres/data
```

> Windows PowerShell:
>
> ```
> Remove-Item -Recurse -Force .\docker\postgres\data
> ```

🧩 三、docker-compose.yml（请直接用这个版本）

把你当前的文件替换成以下完整内容 👇：

```
version: "3.9"

services:
  # === PostgreSQL (Developer DB) ===
  postgres:
    image: postgres:16
    container_name: gamevault_developer_postgres
    restart: unless-stopped
    ports:
      - "12010:5432"
    environment:
      POSTGRES_DB: gamevault_developer_db
      POSTGRES_USER: gamevault_developer_user
      POSTGRES_PASSWORD: gamevault_developer_pass
      TZ: Asia/Singapore
    volumes:
      - ./docker/postgres/data:/var/lib/postgresql/data
      - /etc/localtime:/etc/localtime:ro
    networks:
      - gamevault_network

  # === Redis (Developer Cache) ===
  redis:
    image: redis:alpine
    container_name: gamevault_developer_redis
    restart: always
    ports:
      - "12013:6379"
    volumes:
      - ./docker/redis/data:/data
    networks:
      - gamevault_network

networks:
  gamevault_network:
    driver: bridge
```

🧩 四、启动容器

运行以下命令：

```
docker compose down -v
docker compose up -d
```

然后检查状态：

```
docker ps
```

应看到类似：

```
CONTAINER ID   IMAGE          PORTS                   NAMES
a1b2c3d4e5f6   postgres:16    0.0.0.0:12010->5432/tcp gamevault_developer_postgres
b2c3d4e5f6a7   redis:alpine   0.0.0.0:12013->6379/tcp gamevault_developer_redis
```

🧩 五、验证数据库是否创建成功

执行：

```
docker exec -it gamevault_developer_postgres psql -U gamevault_developer_user -d gamevault_developer_db
```

成功后，你应看到：

```
gamevault_developer_db=>
```

输入以下命令验证：

```
\l        -- 列出所有数据库
\c gamevault_developer_db   -- 进入当前数据库
\dt       -- 查看是否有表
```

如果是空数据库，说明一切正常 ✅
 （Spring Boot 启动后它会自动建表）

🧩 六、配置 Spring Boot 连接

在 `application.yml` 中：

```
spring:
  datasource:
    url: jdbc:postgresql://localhost:12010/gamevault_developer_db
    username: gamevault_developer_user
    password: gamevault_developer_pass
    driver-class-name: org.postgresql.Driver
```

启动项目时看到类似：

```
HikariPool-1 - Start completed.
Connected to PostgreSQL...
```

就说明连接成功 ✅。

🧩 七、在 IDEA / pgAdmin 里连接

| 字段     | 值                       |
| -------- | ------------------------ |
| Host     | localhost                |
| Port     | 12010                    |
| User     | gamevault_developer_user |
| Password | gamevault_developer_pass |
| Database | gamevault_developer_db   |

点击「Test Connection」，应显示绿色 ✅。

🧩 八、Redis 同理

| Host | localhost |
 | Port | 12013 |

可用命令验证：

```
docker exec -it gamevault_developer_redis redis-cli
PING
```

输出：

```
PONG
```

✅ 最终结果检查清单

| 项目                  | 状态 |
| --------------------- | ---- |
| Docker 容器都启动     | ✅    |
| 数据库能 `psql` 进入  | ✅    |
| Spring Boot 连接正常  | ✅    |
| IDEA / pgAdmin 能访问 | ✅    |



#### Redis缓存持久化

```
+--------------------------------------+           +-------------------------------------------+
| 🎮 前端（Next.js / React + AntD）     |           | 🧱 后端（Spring Boot / DDD 架构）           |
+--------------------------------------+           +-------------------------------------------+
             │                                            │
             │ ① 用户访问游戏详情页                      │
             │   GET /api/developer/devgame/public/{id}   │
             │──────────────────────────────────────────▶│
             │                                            ▼
             │        [DevGamePublicController.getPublicGameDetail()]
             │             ├─ 查询游戏详情（Game + Assets）
             │             └─ 调用 DevGameStatisticsApplicationService.recordGameView()
             │                                            │
             │                                            ▼
             │        [DevGameStatisticsApplicationService]
             │             └─ 调用 Redis 缓存层           │
             │                 cache.incrementView(gameId)│
             │                                            │
             │                                            ▼
             │        [DevGameStatisticsCache]
             │             └─ Redis INCR devgame:view:{gameId}
             │                                            │
             │──────────────────────────────────────────◀│
             │                                            │
             │ ② 用户点击 “下载游戏”                      │
             │   GET /api/developer/devgameasset/download/{assetId}
             │──────────────────────────────────────────▶│
             │                                            ▼
             │        [DevGameAssetDownloadController.downloadAsset()]
             │             ├─ 查找 DevGameAsset by assetId
             │             ├─ 如果 assetType == "zip" →
             │             │     devGameStatisticsApplicationService.recordGameDownload(gameId)
             │             ├─ Redis INCR devgame:download:{gameId}
             │             └─ 返回文件流 (FileSystemResource)
             │                                            │
             │──────────────────────────────────────────◀│
             │                                            │
             │ ③ Redis 缓存结构（实时）                  │
             │──────────────────────────────────────────▶│
             │     🔹 devgame:view:{gameId}      → 浏览数累积
             │     🔹 devgame:download:{gameId}  → 下载数累积
             │──────────────────────────────────────────◀│
             │                                            │
             │ ④ 后台定时任务（每5分钟）                 │
             │──────────────────────────────────────────▶│
             │                                            ▼
             │        [DevGameStatisticsSyncService.syncStatisticsFromRedis()]
             │             ├─ cache.getKeysByPrefix("devgame:view:")
             │             ├─ 遍历每个 gameId:
             │             │     ├─ viewCount = getViewCount(gameId)
             │             │     ├─ downloadCount = getDownloadCount(gameId)
             │             │     └─ repository.insert / updateCounts()
             │             └─ 日志输出同步完成 ✅
             │                                            │
             │──────────────────────────────────────────◀│
             │                                            │
             │ ⑤ PostgreSQL 表：dev_game_statistics       │
             │──────────────────────────────────────────▶│
             │     id (UUID)                              │
             │     game_id                                │
             │     view_count                             │
             │     download_count                         │
             │     rating                                 │
             │     updated_at                             │
             │──────────────────────────────────────────◀│
             │                                            │
             ▼                                            ▼
     📊 Redis 实时缓存（快速响应） ✅       🗃 PostgreSQL 周期性同步（持久化） ✅

```

| 模块                                    | 作用                                                      | 主要方法                                            |
| --------------------------------------- | --------------------------------------------------------- | --------------------------------------------------- |
| **DevGameStatisticsApplicationService** | 负责业务层统计操作（recordGameView / recordGameDownload） | `incrementView()` / `incrementDownload()`           |
| **DevGameStatisticsCache**              | 负责与 Redis 通信                                         | `opsForValue().increment("devgame:view:" + gameId)` |
| **DevGameStatisticsSyncService**        | 定时任务，从 Redis 拉数据写入数据库                       | `@Scheduled(cron="0 */5 * * * *")`                  |
| **DevGameAssetDownloadController**      | 文件下载接口，统计下载数                                  | `recordGameDownload()`                              |
| **Redis**                               | 实时缓存计数（高性能）                                    | `devgame:view:xxx` / `devgame:download:xxx`         |
| **PostgreSQL**                          | 最终持久化存储                                            | `dev_game_statistics` 表                            |

| 时间点             | Redis 状态          | PostgreSQL 状态           |
| ------------------ | ------------------- | ------------------------- |
| T0（访问详情页）   | view:1              | —                         |
| T1（用户下载）     | download:1          | —                         |
| T5（定时任务执行） | view:1 / download:1 | ✅ 同步入库                |
| T6（下一次访问）   | view:2 / download:1 | PostgreSQL 等待下一次同步 |

前端访问游戏详情界面会把数据缓存到redis计数（key value），每隔5min会自动持久化到数据库中方便后面dashboard

#### game hub 热门下载展示功能

后端架构流程

```
+--------------------------------------+           +-------------------------------------------+
| 🎮 前端（Next.js / React + AntD）     |           | 🧱 后端（Spring Boot / DDD 架构）           |
+--------------------------------------+           +-------------------------------------------+
             │                                            │
             │ ① 用户打开开发者 Dashboard 页面             │
             │   GET /api/dev/statistics/dashboard/{devId}│
             │──────────────────────────────────────────▶│
             │                                            ▼
             │       [DevGameStatisticsController.getDashboardSummary()]
             │             └─ 调用 DevGameStatisticsDashboardService.getDashboardDetails()
             │                                            │
             │                                            ▼
             │       [DevGameStatisticsDashboardService]
             │             ├─ devGameRepository.findByDeveloperProfileId(developerId)
             │             ├─ 遍历每个 DevGame
             │             │     └─ devGameStatisticsRepository.findByGameId(gameId)
             │             ├─ 聚合总览 Summary (totalViews, downloads, rating)
             │             └─ 封装 DevDashboardDetailedResponse
             │                                            │
             │──────────────────────────────────────────◀│
             │                                            │
             │ ② 前端渲染数据：Dashboard 展示              │
             │   ├─ SummaryCard(总览)                     │
             │   ├─ Table(每个游戏统计行)                  │
             │   └─ Chart(下载量/浏览量柱状图)             │
             │                                            │
             │──────────────────────────────────────────▶│
             │                                            ▼
             │ ③ 用户点击单个游戏详情                     │
             │   GET /api/developer/devgame/public/{gameId}│
             │──────────────────────────────────────────▶│
             │                                            ▼
             │       [DevGamePublicController.getPublicGameDetail()]
             │             ├─ 查询 DevGame + Assets
             │             └─ devGameStatisticsAppService.recordGameView(gameId)
             │                                            │
             │                                            ▼
             │       [DevGameStatisticsAppService]
             │             └─ Redis INCR devgame:view:{gameId}
             │                                            │
             │──────────────────────────────────────────◀│
             │                                            │
             │ ④ 用户点击“下载游戏”按钮                    │
             │   GET /api/developer/devgameasset/download/{assetId}
             │──────────────────────────────────────────▶│
             │                                            ▼
             │       [DevGameAssetDownloadController.downloadAsset()]
             │             ├─ 查找 asset
             │             ├─ 判断 zip 类型
             │             └─ Redis INCR devgame:download:{gameId}
             │                                            │
             │──────────────────────────────────────────◀│
             │                                            │
             │ ⑤ 后台定时任务（每5分钟同步 Redis→DB）      │
             │──────────────────────────────────────────▶│
             │                                            ▼
             │       [DevGameStatisticsSyncService.syncStatisticsFromRedis()]
             │             ├─ 获取 devgame:view:* / download:* keys
             │             ├─ 聚合统计增量
             │             └─ 调用 devGameStatisticsRepository.updateCounts()
             │                                            │
             │──────────────────────────────────────────◀│
             │                                            │
             ▼                                            ▼
     📊 Redis 实时缓存（快速响应） ✅       🗃 PostgreSQL 周期性同步（持久化） ✅

```

![image-20251020024343187](C:\Users\12912\AppData\Roaming\Typora\typora-user-images\image-20251020024343187.png)
