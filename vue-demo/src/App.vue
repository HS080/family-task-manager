<template>
  <div v-if="isLogin" class="home-container">
    <!-- 顶部导航栏 -->
    <header class="navbar">
      <h1>🏠 智能家庭任务管理与共享平台</h1>
      <div class="user-info">
        <span>欢迎回来，{{ username }}!</span>
        <button @click="logout" class="logout-btn">退出登录</button>
      </div>
    </header>

    <!-- 主内容区域 -->
    <div class="main-content">
      <!-- 左侧：家庭成员 -->
      <aside class="family-sidebar">
        <h3>👨‍👩‍👧‍👦 家庭成员</h3>
        <div class="member-list">
          <div class="member-item active">爸爸 (管理员)</div>
          <div class="member-item">妈妈</div>
          <div class="member-item">孩子</div>
          <div class="member-item add-member">+ 添加成员</div>
        </div>
      </aside>

      <!-- 右侧：任务区域 -->
      <section class="task-area">
        <div class="task-header">
          <h2>🏠 家庭任务看板</h2>
          <button @click="showAddTask = true" class="add-task-btn">+ 新增任务</button>
        </div>

        <!-- 任务列表 -->
        <div class="task-list">
          <div class="task-card" v-for="item in taskList" :key="item.id">
            <div class="task-title">{{ item.title }}</div>
            <div class="task-desc">{{ item.desc }}</div>
            <div class="task-footer">
              <span :class="['task-status', item.status]">{{ item.statusText }}</span>
              <span>截止日期：{{ item.deadline }}</span>
            </div>
          </div>
        </div>
      </section>
    </div>

    <!-- 新增任务弹窗 (简单模拟) -->
    <div v-if="showAddTask" class="modal-mask">
      <div class="modal-wrapper">
        <div class="modal-container">
          <h3>新增家庭任务</h3>
          <div class="form-group">
            <label>任务名称：</label>
            <input v-model="newTaskTitle" type="text" placeholder="请输入任务名称">
          </div>
          <div class="form-group">
            <label>任务描述：</label>
            <textarea v-model="newTaskDesc" placeholder="请输入任务描述"></textarea>
          </div>
          <div class="modal-actions">
            <button @click="showAddTask = false" class="btn-cancel">取消</button>
            <button @click="addTask" class="btn-confirm">确认添加</button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 未登录状态 -->
  <div v-else class="login-container">
    <!-- 这里复用你之前的登录样式，为了方便我直接写死在代码里，你也可以单独抽离 -->
    <div class="login-box">
      <div class="title-area">
        <h2>智能家庭任务管理与共享平台</h2>
        <p>Family Task Management & Sharing Platform</p>
      </div>
      <div class="form-item">
        <label>账号</label>
        <input v-model="username" type="text" placeholder="请输入家庭账号/用户名" />
      </div>
      <div class="form-item">
        <label>密码</label>
        <input v-model="password" type="password" placeholder="请输入密码" />
      </div>
      <button class="login-btn" @click="handleLogin">登录进入系统</button>
      <div class="tip" :class="resultCode === 200 ? 'success' : 'error'">
        {{ resultMsg }}
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios'
import { ref, reactive } from 'vue'

// 登录状态
const isLogin = ref(false)
const username = ref('admin')
const password = ref('123456')
const resultCode = ref('')
const resultMsg = ref('')

// 首页数据
const taskList = ref([
  { id: 1, title: "打扫客厅", desc: "周末下午3点前完成", status: "doing", statusText: "进行中", deadline: "2026-04-10" },
  { id: 2, title: "购买蔬菜", desc: "西红柿、鸡蛋、牛奶", status: "todo", statusText: "待完成", deadline: "2026-04-08" },
  { id: 3, title: "辅导孩子作业", desc: "检查数学试卷", status: "done", statusText: "已完成", deadline: "2026-04-05" }
])
const showAddTask = ref(false)
const newTaskTitle = ref('')
const newTaskDesc = ref('')

// 登录逻辑
const handleLogin = async () => {
  try {
    const res = await axios.post('/api/login', { username: username.value, password: password.value })
    resultCode.value = res.data.code
    resultMsg.value = res.data.msg
    if (res.data.code === 200) {
      isLogin.value = true
    }
  } catch (err) {
    resultMsg.value = '网络异常或账号密码错误'
    resultCode.value = 500
  }
}

// 退出登录
const logout = () => {
  isLogin.value = false
  username.value = ''
  password.value = ''
}

// 新增任务
const addTask = () => {
  if (!newTaskTitle.value) return
  taskList.value.unshift({
    id: Date.now(),
    title: newTaskTitle.value,
    desc: newTaskDesc.value,
    status: "todo",
    statusText: "待完成",
    deadline: "2026-04-15"
  })
  showAddTask.value = false
  newTaskTitle.value = ''
  newTaskDesc.value = ''
  alert('任务添加成功！')
}
</script>

<style scoped>
/* 通用样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Microsoft YaHei", sans-serif;
}

/* 首页容器 */
.home-container {
  width: 100%;
  height: 100vh;
  background-color: #f5f7fa;
}

/* 导航栏 */
.navbar {
  background-color: #42b983;
  color: white;
  padding: 15px 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.navbar h1 {
  font-size: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.logout-btn {
  padding: 6px 12px;
  background-color: rgba(255,255,255,0.2);
  border: 1px solid white;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}

/* 主内容布局 */
.main-content {
  display: flex;
  height: calc(100vh - 60px);
  padding: 20px;
  gap: 20px;
}

/* 左侧边栏 */
.family-sidebar {
  width: 250px;
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.family-sidebar h3 {
  margin-bottom: 15px;
  color: #333;
  font-size: 16px;
}

.member-list {
  list-style: none;
}

.member-item {
  padding: 10px;
  margin-bottom: 8px;
  border-radius: 6px;
  cursor: pointer;
}

.member-item.active {
  background-color: #e8f5e9;
  color: #2e7d32;
  font-weight: bold;
}

.add-member {
  color: #42b983;
  text-align: center;
  border: 1px dashed #42b983;
  padding: 8px;
}

/* 右侧任务区 */
.task-area {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  overflow-y: auto;
}

.task-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.add-task-btn {
  padding: 8px 16px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

/* 任务卡片 */
.task-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 15px;
}

.task-card {
  background-color: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 15px;
  transition: transform 0.2s;
}

.task-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.task-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 5px;
  color: #333;
}

.task-desc {
  font-size: 13px;
  color: #666;
  margin-bottom: 10px;
}

.task-footer {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
}

.task-status {
  padding: 3px 8px;
  border-radius: 10px;
  font-size: 11px;
}

.task-status.todo { background-color: #fff3cd; color: #fd7e14; }
.task-status.doing { background-color: #cfe2ff; color: #0d6efd; }
.task-status.done { background-color: #d1e7dd; color: #198754; }

/* 弹窗样式 */
.modal-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.modal-wrapper {
  background: white;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
}

.modal-container {
  padding: 20px;
}

.modal-container h3 {
  margin-bottom: 20px;
  text-align: center;
  color: #333;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #555;
}

.form-group input, .form-group textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  outline: none;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.btn-cancel {
  padding: 8px 16px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 4px;
  cursor: pointer;
}

.btn-confirm {
  padding: 8px 16px;
  background: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

/* 以下是复用的登录样式 */
.login-container {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(to right, #f0f9ff, #e6f7ff);
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-box {
  width: 380px;
  background: #ffffff;
  padding: 40px 30px;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.title-area {
  text-align: center;
  margin-bottom: 30px;
}

.title-area h2 {
  font-size: 22px;
  color: #2c3e50;
  margin: 0 0 8px 0;
  font-weight: 600;
}

.title-area p {
  font-size: 13px;
  color: #7f8c8d;
  margin: 0;
}

.form-item {
  margin-bottom: 20px;
}

.form-item label {
  display: block;
  font-size: 14px;
  color: #34495e;
  margin-bottom: 6px;
  font-weight: 500;
}

.form-item input {
  width: 100%;
  height: 42px;
  padding: 0 12px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  box-sizing: border-box;
}

.form-item input:focus {
  border-color: #42b983;
}

.login-btn {
  width: 100%;
  height: 45px;
  background: #42b983;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 15px;
  cursor: pointer;
  margin-top: 10px;
  transition: background 0.3s;
}

.login-btn:hover {
  background: #389e72;
}

.tip {
  text-align: center;
  font-size: 13px;
  margin-top: 15px;
}

.success {
  color: #42b983;
}

.error {
  color: #e74c3c;
}
</style>