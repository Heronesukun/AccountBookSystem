<template>
  <el-card :title="选择登录用户">
    <template #default>
      <el-row style="text-align: center">
        <el-col :offset="8" :span="6" >
          <el-form :model="user" label-width="auto">
            <el-form-item label="用户名">
              <el-input v-model="user.username"></el-input>
            </el-form-item>
            <el-form-item label="密码">
              <el-input type="password" v-model="user.userPass"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button @click="doLogin">登录</el-button>
              <!-- 在登录表单底部添加 -->
              <div class="register-link">
                还没有账号？<router-link to="/register">立即注册</router-link>
              </div>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </template>
  </el-card>
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

</style>
