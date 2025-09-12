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



目前完成后端配置以及git管理

## 下次任务0913：

跑通后端，数据库链接，使用新流程方便后续迁移k8s等企业管理，同时预留接口给中间件

后端架构完善，同时根据类图以及功能需求开始开发部分功能

完善和前端的接口，使用postman来测试是否正确访问
