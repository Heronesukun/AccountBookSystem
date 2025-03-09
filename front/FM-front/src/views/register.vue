<template>
  <el-card :title="注册新用户">
    <template #default>
      <el-row style="text-align: center">
        <el-col :offset="6" :span="12">
          <el-form :model="registerForm" :rules="rules" ref="registerFormRef" label-width="100px">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="registerForm.username"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input type="password" v-model="registerForm.password"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input type="password" v-model="registerForm.confirmPassword"></el-input>
            </el-form-item>
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="registerForm.nickname"></el-input>
            </el-form-item>
            <el-form-item label="验证码" prop="captcha">
              <el-row>
                <el-col :span="16">
                  <el-input v-model="registerForm.captcha"></el-input>
                </el-col>
                <el-col :span="8">
                  <img :src="captchaUrl" @click="refreshCaptcha" style="cursor: pointer; height: 40px;" alt="验证码"/>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="doRegister">注册</el-button>
              <el-button @click="goToLogin">返回登录</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </template>
  </el-card>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

let registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  captcha: '',
  createdAt: '',
  updatedAt: ''
});

let captchaUrl = ref('/api/captcha?timestamp=' + new Date().getTime());

let rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少 6 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
};

let registerFormRef = ref(null);
let router = useRouter();

function validateConfirmPassword(rule, value, callback) {
  if (value !== registerForm.value.password) {
    callback(new Error('两次输入的密码不一致'));
  } else {
    callback();
  }
}

function refreshCaptcha() {
  captchaUrl.value = '/api/captcha?timestamp=' + new Date().getTime();
}

function doRegister() {
  registerForm.value.createdAt = new Date().toISOString();
  registerForm.value.updatedAt = registerForm.value.createdAt;

  registerFormRef.value.validate((valid) => {
    if (valid) {
      console.log('Register Data:', registerForm.value);
      ElMessage({
        type: 'success',
        message: '注册成功'
      });
      router.push('/login');
    } else {
      ElMessage({
        type: 'error',
        message: '注册表单验证失败'
      });
    }
  });
}

function goToLogin() {
  router.push('/');
}
</script>

<style scoped>
</style>
