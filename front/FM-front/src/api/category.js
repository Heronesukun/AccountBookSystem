import axios from 'axios';
import config from '@/config/config.js';

// 分类管理相关API
export default {
  // 获取所有分类
  getAllCategories(bookId, userId) {
    return axios.get(`${config.mdBaseUrl}/category/list`, {
      params: { bookId, userId },
      headers: { token: localStorage.getItem('token') }
    });
  },
  
  // 获取一级分类
  getParentCategories(bookId, userId, type) {
    return axios.get(`${config.mdBaseUrl}/category/parent`, {
      params: { bookId, userId, type },
      headers: { token: localStorage.getItem('token') }
    });
  },
  
  // 获取二级分类
  getChildCategories(parentId, bookId, userId) {
    return axios.get(`${config.mdBaseUrl}/category/child`, {
      params: { parentId, bookId, userId },
      headers: { token: localStorage.getItem('token') }
    });
  },
  
  // 根据ID获取分类
  getCategoryById(id) {
    return axios.get(`${config.mdBaseUrl}/category/get/${id}`, {
      headers: { token: localStorage.getItem('token') }
    });
  },
  
  // 添加分类
  addCategory(data) {
    return axios.post(`${config.mdBaseUrl}/category/add`, data, {
      headers: { token: localStorage.getItem('token') }
    });
  },
  
  // 更新分类
  updateCategory(data) {
    return axios.put(`${config.mdBaseUrl}/category/update`, data, {
      headers: { token: localStorage.getItem('token') }
    });
  },
  
  // 删除分类
  deleteCategory(id, userId) {
    return axios.delete(`${config.mdBaseUrl}/category/delete`, {
      params: { id, userId },
      headers: { token: localStorage.getItem('token') }
    });
  },
  
  // 批量删除分类
  batchDeleteCategories(ids, userId) {
    return axios.delete(`${config.mdBaseUrl}/category/batchDelete`, {
      params: { ids, userId },
      headers: { token: localStorage.getItem('token') }
    });
  }
};