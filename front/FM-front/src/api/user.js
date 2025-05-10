import axios from 'axios';
import config from '@/config/config.js';

// 用户注册
return request({
  url: '/auth/register',
  method: 'post',
  data,
  noToken: true // 标记此请求不需要添加 token
})

// 用户登录
export function login(data) {
  return axios.post(`${config.mdBaseUrl}/auth/login`, data);
}

// 获取用户信息
export function getUserInfo() {
  return axios.get(`${config.mdBaseUrl}/auth/info`, {
    headers: {
      token: localStorage.getItem('token')
    }
  });
}

// 退出登录
export function logout() {
  return axios.post(`${config.mdBaseUrl}/auth/logout`, null, {
    headers: {
      token: localStorage.getItem('token')
    }
  });
}