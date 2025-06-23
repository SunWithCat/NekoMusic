# NekoMusic仿站项目

## 项目简介
本项目为个人学习与研究用途，实现了基础的音乐播放、歌单浏览、搜索等功能，前后端分离.

## 技术栈
- 前端：Vue 3、TypeScript、Vite、Pinia、Vue Router、CSS3
- 后端：Spring Boot、MySQL、Java 17
- 其他：本地 Mock 数据、前后端分离架构

## 功能特性
- 本地音乐播放（音乐文件存放于\my-vue-backend\demo\src\main\resources\）
- 歌曲进度调节
- 歌词滚动显示
- 歌单/歌曲搜索
- 响应式页面布局

## 快速启动

### 前端
```bash
# 安装依赖
npm install
# 启动开发服务器
npm run dev
```
访问：http://localhost:5173

### 后端
```bash
# 进入后端目录
cd my-vue-backend/demo
# 使用 Maven 启动
./mvnw spring-boot:run
```
默认端口：8080

## 截图
- 主页界面  
  ![项目截图](src/assets/image1.png)
- 歌单界面  
  ![项目截图](src/assets/image2.png)
- 搜索界面  
  ![项目截图](src/assets/image3.png)
- 歌曲界面  
  ![项目截图](src/assets/image4.png)

