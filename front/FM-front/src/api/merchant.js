import axios from 'axios';
import config from '@/config/config.js';

const merchantRequest = {
  // 获取商家列表
  getMerchantList(bookId, userId, page = 1, pageSize = 10, keyword = '') {
    return axios.get(`${config.mdBaseUrl}/merchant/list`, {
      params: {
        bookId,
        userId,
        page,
        pageSize,
        keyword
      },
      headers: { token: localStorage.getItem('token') }
    });
  },

  // 获取单个商家
  getMerchantById(id) {
    return axios.get(`${config.mdBaseUrl}/merchant/get/${id}`, {
      headers: { token: localStorage.getItem('token') }
    });
  },

  // 添加商家
  addMerchant(data) {
    return axios.post(`${config.mdBaseUrl}/merchant/add`, data, {
      headers: { token: localStorage.getItem('token') }
    });
  },

  // 更新商家
  updateMerchant(data) {
    return axios.put(`${config.mdBaseUrl}/merchant/update`, data, {
      headers: { token: localStorage.getItem('token') }
    });
  },

  // 删除商家
  deleteMerchant(id, userId) {
    return axios.delete(`${config.mdBaseUrl}/merchant/delete`, {
      params: {
        id,
        userId
      },
      headers: { token: localStorage.getItem('token') }
    });
  },

  // 根据分类获取商家
  getMerchantsByCategory(categoryId, bookId, userId) {
    return axios.get(`${config.mdBaseUrl}/merchant/category/${categoryId}`, {
      params: {
        bookId,
        userId
      },
      headers: { token: localStorage.getItem('token') }
    });
  }
};

export default merchantRequest;