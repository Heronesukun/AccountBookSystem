import axios from 'axios';
import { ElMessage } from 'element-plus';
import router from '@/router';

// 创建axios实例
const service = axios.create({
  timeout: 10000 // 请求超时时间
});

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 如果有token，添加到请求头
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['token'] = token;
    }
    return config;
  },
  error => {
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data;
    // 如果返回的状态码不是200，说明接口请求有误
    if (res.code !== 200) {
      // 特殊处理token过期的情况
      if (res.code === 401) {
        ElMessage.error('登录已过期，请重新登录');
        localStorage.removeItem('token');
        router.push('/login');
      } else {
        ElMessage.error(res.msg || '请求失败');
      }
      return Promise.reject(new Error(res.msg || '请求失败'));
    } else {
      return response;
    }
  },
  error => {
    console.error('响应错误:', error);
    ElMessage.error('网络异常，请稍后重试');
    return Promise.reject(error);
  }
);

export default service;