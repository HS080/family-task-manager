# 项目功能清单与交接说明

本文档基于当前工作区代码整理，覆盖前端 `vue-demo` 与后端 `demo` 两个项目，目的是让后续接手的开发者或 agent 能快速理解：

- 这个项目目前做了什么
- 前后端各自负责什么
- 当前实现方式是什么
- 还有哪些地方适合继续优化

> 说明：本文档是“现状说明 + 交接说明 + 改进建议”合并版，优先描述当前真实代码状态，而不是最初设计稿。

---

## 1. 工作区结构概览

当前工作区根目录下主要包含两个项目：

- `vue-demo`：前端项目，基于 `Vue 3 + Vite + Element Plus + Pinia + Vue Router`
- `demo`：后端项目，基于 `Spring Boot` + `MyBatis` 的接口服务

另有一份较早的部署说明：

- `毕业设计部署文档.md`

本文档的定位是**现状交接说明**，用于后续开发者快速接手。

---

## 2. 项目概述

这是一个家庭任务管理系统，核心场景包括：

- 用户注册和登录
- 创建或加入家庭
- 管理家庭成员
- 创建、分配、流转、评论、删除任务
- 查看统计分析与提醒中心
- 展示个人中心与家庭空间
- 管理员查看全局用户、家庭、统计数据

整体上是一个围绕“家庭协作 + 系统管理”的轻量级任务平台。

---

## 3. 前端项目 `vue-demo` 现状

### 3.1 技术栈

- Vue 3
- Vite
- Element Plus
- Pinia
- Vue Router
- Axios
- Day.js
- ECharts（管理员仪表盘已接入）

### 3.2 前端目录结构

当前前端主要目录职责如下：

- `src/views`：页面级视图
- `src/components`：可复用组件
- `src/layouts`：主布局与管理员布局
- `src/router`：路由配置
- `src/stores`：Pinia 全局状态
- `src/composables`：组合式逻辑复用
- `src/utils`：请求封装与工具

### 3.3 已实现页面

#### 登录与注册页

文件：`src/views/LoginView.vue`

能力包括：

- 登录
- 注册
- 重置密码
- 记住账号
- 密码强度提示
- 登录页主题切换
- 更完整的交互动画与视觉样式
- 登录成功后按 `role` 自动分流到前台或后台

#### 主布局

文件：`src/layouts/MainLayout.vue`

能力包括：

- 左侧菜单导航
- 顶部标题栏
- 当前用户信息展示
- 折叠菜单
- 刷新按钮
- 子路由容器
- 头像下拉菜单
- 管理员入口仅对 `ADMIN` 显示，且放在头像下拉中

#### 管理员布局

文件：`src/layouts/AdminLayout.vue`

能力包括：

- 管理员专属侧边栏
- 后台顶部信息栏
- 后台仪表盘 / 用户管理 / 家庭管理 导航
- 返回前台入口
- 退出登录入口

#### 工作台

文件：`src/views/DashboardView.vue`

能力包括：

- 任务统计卡片
- 最近任务展示
- 快速新建任务入口
- 空状态提示
- 统计数字动效

#### 任务中心

文件：`src/views/TaskCenterView.vue`

能力包括：

- 查看任务列表
- “我的任务 / 全部任务”切换
- 状态修改
- 删除任务
- 进入任务详情
- 打开新建任务弹窗
- 负责人展示

#### 任务详情

文件：`src/views/TaskDetailView.vue`

能力包括：

- 任务基础信息
- 状态更新
- 评论发布
- 评论列表
- 操作日志展示

#### 家庭空间

文件：`src/views/FamilySpaceView.vue`

能力包括：

- 查看家庭信息
- 查看家庭邀请码
- 查看家庭成员
- 修改家庭名称
- 修改成员角色
- 修改成员身份
- 删除成员
- 退出家庭

#### 提醒中心

文件：`src/views/ReminderView.vue`

- 已纳入路由和布局
- 作为任务提醒展示入口

#### 统计分析

文件：`src/views/StatsView.vue`

- 已纳入路由和布局
- 作为统计图表/数据分析入口

#### 个人中心

文件：`src/views/ProfileView.vue`

- 已纳入路由和布局
- 作为用户信息展示入口

#### 管理员后台页面

文件：

- `src/views/admin/AdminDashboardView.vue`
- `src/views/admin/AdminUsersView.vue`
- `src/views/admin/AdminFamiliesView.vue`

能力包括：

- 全局统计数据仪表盘
- 全站用户列表分页
- 用户禁用 / 解禁
- 全站家庭列表分页
- 管理员专属访问入口

### 3.4 前端核心组件

#### 新建任务弹窗

文件：`src/components/TaskCreateDialog.vue`

当前能力：

- 输入任务名称
- 选择负责人
- 选择截止时间
- 选择优先级
- 输入描述
- 提交创建任务

关键点：

- 负责人来源于当前家庭成员接口
- 创建前会校验负责人必须属于当前家庭
- 不再使用早期写死的固定负责人选项

### 3.5 前端全局状态

文件：`src/stores/app.js`

当前保存：

- `token`
- `user`
- `family`
- `tasks`

其中 `user` 中已包含：

- `role`

以便刷新后继续识别管理员身份。

### 3.6 前端请求封装

文件：`src/utils/request.js`

现状：

- baseURL 指向 `http://localhost:8081/api`
- 请求自动带 token
- 响应统一检查 `code !== 200` 的错误
- 错误时统一通过 Element Plus 提示

### 3.7 前端路由与权限拦截

文件：`src/router/index.js`

当前已经配置：

- `/app` 主业务路由
- `/admin` 管理员路由
- `beforeEach` 中校验：
  - 未登录跳登录页
  - 非 `ADMIN` 禁止访问 `/admin`
  - 已登录用户访问 `/login` 时，按角色重定向到对应首页

权限判断依赖 `localStorage` 中保存的用户角色。

---

## 4. 后端项目 `demo` 现状

### 4.1 技术栈和结构风格

当前后端是典型的 Spring Boot 风格，包含：

- controller
- service
- mapper
- entity
- dto
- common
- config
- exception

数据库访问层使用的是 **MyBatis 注解方式**，不是 MyBatis-Plus，也不是 Spring Data JPA。

### 4.2 主要后端接口能力

#### 认证相关

- 登录
- 注册
- 重置密码

#### 家庭相关

- 获取当前家庭
- 获取当前家庭成员
- 修改家庭名称
- 修改成员角色
- 修改成员身份
- 删除成员
- 退出家庭

#### 任务相关

- 新建任务
- 查询任务列表
- 查询任务详情
- 修改任务状态
- 删除任务
- 新增评论
- 获取评论列表
- 获取操作日志

#### 管理员相关

- 获取全局统计
- 分页获取全站用户
- 禁用 / 解禁用户
- 分页获取全站家庭
- 获取管理员仪表盘图表数据

### 4.3 关键后端文件

#### 任务控制器

文件：`demo/src/main/java/com/example/demo/controller/TaskController.java`

负责：

- 接收任务相关请求
- 调用 service 层执行业务
- 写入任务活动日志

#### 任务服务

文件：`demo/src/main/java/com/example/demo/service/TaskService.java`

负责：

- 新建任务
- 获取任务详情
- 获取任务列表
- 修改任务状态
- 删除任务
- 校验家庭成员权限

#### 权限服务

文件：`demo/src/main/java/com/example/demo/service/PermissionService.java`

负责：

- 判断用户是否属于某个家庭
- 判断用户是否为 OWNER

注意：

- 这个服务里仍然保留 `requireOwner()`
- 但任务状态更新和任务删除已经不再依赖 OWNER 权限

#### 管理员控制器

文件：`demo/src/main/java/com/example/demo/controller/AdminController.java`

负责：

- 管理员统计接口
- 用户分页接口
- 用户禁用 / 解禁接口
- 家庭分页接口
- 仪表盘图表接口

#### 管理员服务

文件：`demo/src/main/java/com/example/demo/service/AdminService.java`

负责：

- `ADMIN` 角色校验
- 全局统计
- 用户分页
- 用户状态更新
- 家庭分页
- 图表数据聚合

#### token 过滤

文件：`demo/src/main/java/com/example/demo/config/JwtAuthFilter.java`

负责：

- 从 `Authorization: Bearer ...` 中解析 token
- 解析出用户 ID
- 放入 `AuthContext`

### 4.4 当前后端权限现状

当前后端任务权限逻辑已经调整为：

- 只要是当前家庭成员，就可以修改任务状态
- 只要是当前家庭成员，就可以删除任务
- 创建任务时，负责人必须是当前家庭成员

管理员权限逻辑已经调整为：

- 只有 `User.role == 'ADMIN'` 才能访问 `/api/admin/*`

### 4.5 用户模型现状

当前 `t_user` 和 `User` 实体已经包含：

- `role`：系统角色，默认 `USER`，管理员为 `ADMIN`
- `status`：账号状态，支持禁用 / 解禁

登录接口返回值也已经包含：

- `role`

### 4.6 数据表结构

当前主表结构已包含：

- `t_user`
  - `role`
  - `status`
- `t_family_group`
- `t_task`
- `t_permission`
- `t_task_comment`
- `t_task_activity`
- `t_reminder`

---

## 5. 当前功能如何串起来工作

### 5.1 登录链路

1. 用户在前端登录页输入账号密码
2. 前端调用后端登录接口
3. 后端返回 token、用户信息、家庭信息、role
4. 前端写入 Pinia 和 localStorage
5. 前端路由跳转到对应首页

### 5.2 家庭数据链路

1. 前端进入任务中心或家庭空间
2. 调用家庭成员接口
3. 拉取当前家庭和成员列表
4. 用于负责人选择、成员展示、角色显示等

### 5.3 任务创建链路

1. 前端打开新建任务弹窗
2. 获取当前家庭成员列表
3. 用户选择负责人
4. 提交到后端 `/api/tasks`
5. 后端校验负责人是否属于当前家庭
6. 创建成功后返回任务数据

### 5.4 任务状态链路

1. 前端点击“进行中”或“已完成”
2. 调用 `/api/tasks/{id}/status`
3. 后端验证任务属于当前家庭
4. 更新状态并返回最新任务数据

### 5.5 任务删除链路

1. 前端点击删除
2. 调用 `/api/tasks/{id}` 的 DELETE 请求
3. 后端验证任务属于当前家庭
4. 删除任务并写入操作日志

### 5.6 管理员访问链路

1. 前端登录后保存 `role`
2. 头像下拉里仅 `ADMIN` 显示“系统管理后台”
3. 路由进入 `/admin/dashboard`
4. `beforeEach` 校验 token 与角色
5. 请求 `/api/admin/*` 时由后端再校验 `ADMIN`

---

## 6. 当前与早期文档的主要差异

### 6.1 早期文档更多是部署说明

`毕业设计部署文档.md` 更偏向：

- 如何启动
- 如何配置
- 如何部署

而现在这份文档更偏向：

- 当前项目做了什么
- 前后端各自有哪些功能
- 哪些地方已经变更
- 后续如何继续开发

### 6.2 权限逻辑已经变化

早期一些说明里可能会出现：

- 只有 OWNER 才能操作任务

但现在后端已经改成：

- 家庭成员均可操作任务
- 管理员后台由 `ADMIN` 角色独立控制

### 6.3 负责人选择方式已经变化

早期任务负责人可能是固定项或简化映射，当前则已经变为：

- 直接来自家庭成员接口
- 更贴近真实业务数据

### 6.4 当前项目页面更完整

前端已经不只是模板首页，而是具备：

- 登录注册
- 工作台
- 任务中心
- 任务详情
- 家庭空间
- 提醒中心
- 统计分析
- 个人中心
- 管理员后台

---

## 7. 当前项目中需要继续改进的点

### 7.1 请求地址环境化

当前前端还写死了：

- `http://localhost:8081/api`

建议改成：

- 本地开发环境变量
- 生产环境通过反向代理使用 `/api`

### 7.2 接口返回结构统一

当前前端有少量接口取值兼容写法，建议统一后端响应结构，减少前端分支处理。

### 7.3 图表数据进一步实化

管理员首页图表已经接了后台接口，但后续仍可继续补更丰富的统计，例如：

- 近 7 天新增任务趋势
- 近 7 天新增家庭趋势
- 用户角色 / 状态更细的分布

### 7.4 数据映射统一

建议统一以下映射：

- `userId -> nickname`
- `familyIdentity -> 中文身份`
- `roleCode -> 中文角色`

避免不同页面展示不一致。

### 7.5 空状态和异常状态

建议继续完善：

- 无家庭
- 无成员
- 无任务
- 接口失败
- 权限不足

等场景下的提示与引导。

### 7.6 结构抽离

前端可以进一步抽离：

- 成员映射逻辑
- 任务状态逻辑
- 表单公共逻辑
- 请求错误处理

---

## 8. 给后续 agent 的工作提示

如果后续 agent 继续在这个项目上工作，建议优先了解以下内容：

### 8.1 前端优先看这些文件

- `vue-demo/src/views/LoginView.vue`
- `vue-demo/src/layouts/MainLayout.vue`
- `vue-demo/src/layouts/AdminLayout.vue`
- `vue-demo/src/views/TaskCenterView.vue`
- `vue-demo/src/views/TaskDetailView.vue`
- `vue-demo/src/views/FamilySpaceView.vue`
- `vue-demo/src/views/admin/AdminDashboardView.vue`
- `vue-demo/src/views/admin/AdminUsersView.vue`
- `vue-demo/src/views/admin/AdminFamiliesView.vue`
- `vue-demo/src/components/TaskCreateDialog.vue`
- `vue-demo/src/utils/request.js`
- `vue-demo/src/stores/app.js`
- `vue-demo/src/router/index.js`

### 8.2 后端优先看这些文件

- `demo/src/main/java/com/example/demo/controller/TaskController.java`
- `demo/src/main/java/com/example/demo/service/TaskService.java`
- `demo/src/main/java/com/example/demo/service/PermissionService.java`
- `demo/src/main/java/com/example/demo/controller/AdminController.java`
- `demo/src/main/java/com/example/demo/service/AdminService.java`
- `demo/src/main/java/com/example/demo/config/JwtAuthFilter.java`
- `demo/src/main/java/com/example/demo/controller/AuthController.java`
- `demo/src/main/java/com/example/demo/service/AuthService.java`

### 8.3 当前约束

- 任务必须绑定家庭
- 任务负责人必须属于当前家庭
- 家庭成员可以操作任务状态和删除
- 管理员后台只允许 `ADMIN` 访问
- 管理员用户状态可被禁用 / 解禁

---

## 9. 结论

当前工作区里的两个项目已经形成了一套比较完整的家庭协作系统：

- 前端负责交互、展示与路由
- 后端负责认证、家庭、任务和权限
- 管理员后台负责全局统计、用户管理和家庭管理

这份文档覆盖了前后端的主要现状、核心差异和后续改进建议，适合作为：

- 项目交接说明
- 后续 agent 工作基线
- 研发现状记录

如果后续继续开发，建议优先同步：

1. 请求环境配置
2. 权限矩阵
3. 接口返回规范
4. 数据映射统一
5. 管理员后台的搜索与审核能力
