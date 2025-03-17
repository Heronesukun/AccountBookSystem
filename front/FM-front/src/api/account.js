import axios from 'axios';
import config from '@/config/config.js';

export default {
  // 获取账户列表
  getAccountList(bookId, userId) {
    return axios.get(`${config.mdBaseUrl}/accountCategory/list`, {
      params: {
        bookId,
        userId
      },
      headers: {
        token: localStorage.getItem('token')
      }
    });
  },

  // 获取净资产信息
  getNetWorth(bookId, userId) {
    return axios.get(`${config.mdBaseUrl}/accountCategory/netWorth`, {
      params: {
        bookId,
        userId
      },
      headers: {
        token: localStorage.getItem('token')
      }
    });
  },

  // 添加账户
  addAccount(accountData) {
    return axios.post(`${config.mdBaseUrl}/accountCategory/add`, accountData, {
      headers: {
        token: localStorage.getItem('token')
      }
    });
  },

  // 更新账户
  updateAccount(accountData) {
    return axios.put(`${config.mdBaseUrl}/accountCategory/update`, accountData, {
      headers: {
        token: localStorage.getItem('token')
      }
    });
  },

  // 删除账户
  deleteAccount(id, userId) {
    return axios.delete(`${config.mdBaseUrl}/accountCategory/delete`, {
      params: {
        id,
        userId
      },
      headers: {
        token: localStorage.getItem('token')
      }
    });
  },

  // 批量删除账户
  batchDeleteAccounts(ids, userId) {
    return axios.delete(`${config.mdBaseUrl}/accountCategory/batchDelete`, {
      params: {
        ids,
        userId
      },
      headers: {
        token: localStorage.getItem('token')
      }
    });
  },

  // 更新账户金额
  updateAccountAmount(id, userId, amount) {
    return axios.put(`${config.mdBaseUrl}/accountCategory/updateAmount`, null, {
      params: {
        id,
        userId,
        amount
      },
      headers: {
        token: localStorage.getItem('token')
      }
    });
  }
};