<template>
  <div class="login-container">
    <div class="login-content">
      <div class="left-section">
        <div class="system-info">
          <h1 class="system-name">财务管理系统</h1>
          <p class="system-slogan">高效、便捷、安全的财务管理解决方案</p>
        </div>
      </div>
      <div class="right-section">
        <el-card class="login-card">
          <template #header>
            <div class="card-header">
              <h2>用户登录</h2>
            </div>
          </template>
          <el-form :model="user" label-width="auto">
            <el-form-item label="用户名">
              <el-input v-model="user.username" prefix-icon="el-icon-user"></el-input>
            </el-form-item>
            <el-form-item label="密码">
              <el-input type="password" v-model="user.userPass" prefix-icon="el-icon-lock"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="doLogin" style="width: 100%;">登录</el-button>
              <div class="register-link">
                还没有账号？<router-link to="/auth/register">立即注册</router-link>
              </div>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import loginRequest from "@/api/login.js";
import {onMounted, ref} from "vue";
import {useRouter} from 'vue-router';
import {ElMessage} from "element-plus";

let user = ref({});// 绑定登录用户信息
let router = useRouter(); // 得到全局的路由对象

function doLogin(){
  // 做一些数据校验....
  loginRequest.doLogin(user.value,(res)=>{
    if(res.code == '200'){
      ElMessage({ type: "success", message: "登录成功" });
      localStorage.setItem("token", res.msg);
      // 存储用户信息，确保与后端字段一致
      localStorage.setItem("username", res.data.username);
      localStorage.setItem("real_name", res.data.realName);
      router.push("/home");
    }else{
      console.log(res)
      // 登录失败
      // 提示登录失败
      ElMessage({
        type:"error",
        message:res.data
      });
    }
  })
}

function goToRegister() {
  // 跳转到注册页面
  router.push("/register");
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url('/pic/fmpic1.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.login-content {
  display: flex;
  width: 80%;
  height: 70%;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 10px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
  overflow: hidden;
}

.left-section {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.right-section {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.system-info {
  text-align: center;
  color: #333;
}

.system-name {
  font-size: 2.5rem;
  margin-bottom: 20px;
  color: #409EFF;
}

.system-slogan {
  font-size: 1.2rem;
  color: #666;
}

.login-card {
  width: 100%;
  max-width: 400px;
}

.card-header {
  text-align: center;
}

.register-link {
  margin-top: 15px;
  text-align: center;
  font-size: 14px;
}

.el-button {
  margin-top: 10px;
}
</style>
