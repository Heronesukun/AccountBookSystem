# FM项目安装使用说明书

## 目录

- [环境准备](#环境准备)
  - [Java环境](#java环境)
  - [Maven安装配置](#maven安装配置)
  - [Redis安装](#redis安装)
  - [MySQL安装](#mysql安装)
- [后端配置](#后端配置)
  - [配置文件说明](#配置文件说明)
  - [POM文件配置](#pom文件配置)
- [前端配置](#前端配置)
  - [Node.js和npm安装](#nodejs和npm安装)
  - [前端依赖安装](#前端依赖安装)
  - [Element Plus和图标配置](#element-plus和图标配置)
- [项目启动](#项目启动)
  - [后端服务启动](#后端服务启动)
  - [前端服务启动](#前端服务启动)
- [常见问题](#常见问题)

## 环境准备

### Java环境

1. 下载并安装JDK 11
   - 访问Oracle官网下载JDK 11: https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
   - 按照安装向导完成安装

2. 配置环境变量
   - 设置JAVA_HOME环境变量指向JDK安装目录
   - 将%JAVA_HOME%\bin添加到PATH环境变量
   - 验证安装：打开命令提示符，输入`java -version`，确认显示版本11

### Maven安装配置

1. 下载Maven

   - 访问Maven官网下载: https://maven.apache.org/download.cgi
   - 下载二进制zip压缩包

2. 安装Maven

   - 解压下载的zip文件到合适的目录（如D:\maven）
   - 设置环境变量MAVEN_HOME指向Maven安装目录
   - 将%MAVEN_HOME%\bin添加到PATH环境变量
   - 验证安装：打开命令提示符，输入`mvn -v`，确认Maven正确安装

3. 配置Maven

   - 编辑Maven安装目录下的conf/settings.xml文件

   - 配置本地仓库位置（可选）：

     ```xml
     <localRepository>D:\maven\repository</localRepository>
     ```

   - 配置国内镜像（推荐使用阿里云镜像）：

     ```xml
     <mirrors>
       <mirror>
         <id>alimaven</id>
         <name>aliyun maven</name>
         <url>https://maven.aliyun.com/repository/public</url>
         <mirrorOf>central</mirrorOf>
       </mirror>
     </mirrors>
     ```

### Redis安装

1. Windows环境下安装Redis
   - 下载Windows版Redis: https://github.com/microsoftarchive/redis/releases
   - 解压并安装
   - 启动Redis服务：`redis-server.exe`

2. 配置Redis（可选）
   - 编辑redis.windows.conf文件
   - 设置密码：`requirepass yourpassword`

### MySQL安装

1. 下载并安装MySQL

   - 访问MySQL官网下载: https://dev.mysql.com/downloads/mysql/
   - 按照安装向导完成安装
   - 设置root密码

2. 创建数据库

   - 使用MySQL命令行或图形界面工具（如MySQL Workbench）

   - 创建名为`fm_project`的数据库：

     ```sql
     CREATE DATABASE fm_project CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
     ```

3. 导入SQL脚本

   - 导入项目中的SQL脚本文件（位于sql目录下）：
     - fm_user.sql
     - fm_category.sql
     - fm_account_category.sql
     - 其他相关SQL文件

## 后端配置

### 配置文件说明

项目中有三个主要的配置文件需要配置：

1. **fm-sso模块配置文件**

   路径：`fm-sso/src/main/resources/application.yaml`

   ```yaml
   server:
     port: 8088
   
   spring:
     redis:
       host: 你的Redis服务器地址
       port: 6379
       password: 你的Redis密码
     datasource:
       druid:
         username: 你的MySQL用户名
         password: 你的MySQL密码
         url: jdbc:mysql://你的MySQL服务器地址:3306/fm_project
         driver-class-name: com.mysql.cj.jdbc.Driver
   mybatis:
     type-aliases-package: com.fm.pojo
     configuration:
       log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
       map-underscore-to-camel-case: true
     mapper-locations: classpath*:mapper/*Mapper.xml
   
   ### JWT
   jwt-secret: 你的JWT密钥
   ```

2. **fm-system模块配置文件**

   路径：`fm-system/src/main/resources/application.yaml`

   ```yaml
   server:
     port: 8080
   
   spring:
     redis:
       host: 你的Redis服务器地址
       port: 6379
       password: 你的Redis密码
     datasource:
       druid:
         username: 你的MySQL用户名
         password: 你的MySQL密码
         url: jdbc:mysql://你的MySQL服务器地址:3306/fm_project
         driver-class-name: com.mysql.cj.jdbc.Driver
   mybatis:
     type-aliases-package: com.fm.pojo
     configuration:
       log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
       map-underscore-to-camel-case: true
     mapper-locations: classpath*:mapper/*Mapper.xml
   
   dict-cache-pre: DICT_
   dict-cache-clear-thread-pool-name: dictCacheClearThreadPoolName
   ```

3. **fm-books模块配置文件**

   路径：`fm-books/src/main/resources/application.yaml`

   ```yaml
   server:
     port: 8060
   
   spring:
     redis:
       host: 你的Redis服务器地址
       port: 6379
       password: 你的Redis密码
     datasource:
       druid:
         username: 你的MySQL用户名
         password: 你的MySQL密码
         url: jdbc:mysql://你的MySQL服务器地址:3306/fm_project
         driver-class-name: com.mysql.cj.jdbc.Driver
   mybatis:
     type-aliases-package: com.fm.pojo
     configuration:
       log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
       map-underscore-to-camel-case: true
     mapper-locations: classpath*:mapper/*Mapper.xml
   
   ### JWT
   jwt-secret: 你的JWT密钥
   ```

4. **ChatGPT API配置（如需使用AI建议功能）**

   路径：`fm-books/src/main/resources/application.properties`

   ```properties
   # ChatGPT API配置
   chatgpt.api.key=你的API密钥
   chatgpt.api.url=https://api.deepseek.com/v1
   chatgpt.api.model=deepseek-chat
   ```

### POM文件配置

项目使用Maven多模块结构，主要的POM文件包括：

1. **根POM文件**

   路径：`pom.xml`

   - 定义了项目的基本信息和模块结构
   - 继承自Spring Boot父POM
   - 管理依赖版本
   - 无需修改，除非需要添加新模块或更改依赖版本

2. **各模块POM文件**

   - 每个模块（fm-common, fm-sso, fm-pojo, fm-system, fm-books）都有自己的POM文件
   - 定义了模块特定的依赖
   - 通常不需要修改，除非需要添加新的依赖

## 前端配置

### Node.js和npm安装

1. 下载并安装Node.js

   - 访问Node.js官网: https://nodejs.org/

   - 下载并安装LTS版本

   - 安装完成后，打开命令提示符验证：

     ```bash
     node -v
     npm -v
     ```

2. 配置npm镜像（可选但推荐）

   - 设置npm使用淘宝镜像：

     ```bash
     npm config set registry https://registry.npmmirror.com
     ```

### 前端依赖安装

1. 进入前端项目目录

   ```bash
   cd front/FM-front
   ```

2. 安装项目依赖

   ```bash
   npm install
   ```

### Element Plus和图标配置

项目已经在package.json中配置了Element Plus和图标依赖，通过npm install会自动安装。如果需要手动安装：

1. 安装Element Plus

   ```bash
   npm install element-plus --save
   ```

2. 安装Element Plus图标

   ```bash
   npm install @element-plus/icons-vue
   ```

3. 配置前端API地址

   - 编辑`src/config/config.js`文件，设置后端API地址：

     ```javascript
     export default {
       ssoBaseUrl: "http://localhost:8088",
       mdBaseUrl: "http://localhost:8060",
       systemBaseUrl: "http://localhost:8080"
     }
     ```

## 项目启动

### 后端服务启动

1. 编译项目

   - 在项目根目录执行：

     ```bash
     mvn clean package -DskipTests
     ```

2. 启动服务（按顺序启动）

   - 启动fm-sso模块（SSO服务）

     ```bash
     java -jar fm-sso/target/fm-sso-1.0-SNAPSHOT.jar
     ```

   - 启动fm-system模块（系统服务）

     ```bash
     java -jar fm-system/target/fm-system-1.0-SNAPSHOT.jar
     ```

   - 启动fm-books模块（账本服务）

     ```bash
     java -jar fm-books/target/fm-books-1.0-SNAPSHOT.jar
     ```

   或者使用IDE（如IntelliJ IDEA）直接运行各模块的主类

### 前端服务启动

1. 进入前端项目目录

   ```bash
   cd front/FM-front
   ```

2. 启动开发服务器

   ```bash
   npm run dev
   ```

3. 访问应用

   - 打开浏览器，访问：http://localhost:5173

## 常见问题

1. **数据库连接失败**
   - 检查MySQL服务是否启动
   - 验证数据库连接信息（用户名、密码、URL）是否正确
   - 确认数据库fm_project是否存在

2. **Redis连接失败**
   - 检查Redis服务是否启动
   - 验证Redis连接信息（主机、端口、密码）是否正确

3. **前端API请求失败**
   - 确认后端服务是否正常运行
   - 检查config.js中的API地址配置是否正确
   - 检查浏览器控制台是否有跨域错误

4. **JWT认证失败**
   - 确保所有服务模块使用相同的jwt-secret
   - 检查token是否正确传递

5. **Maven依赖下载失败**
   - 检查网络连接
   - 尝试使用国内Maven镜像
   - 手动下载依赖并放入本地仓库

6. **npm安装依赖失败**
   - 检查网络连接
   - 使用淘宝npm镜像
   - 尝试清除npm缓存：`npm cache clean --force`


​        
