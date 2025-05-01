import axios from 'axios';
import config from '@/config/config.js';

export default {
  // 添加流水记录
  addGeneralTable(generalTable) {
    return axios.post(`${config.mdBaseUrl}/general-table/add`, generalTable, {
      headers: {
        token: localStorage.getItem('token')
      }
    });
  },

  // 根据ID获取流水记录
  getGeneralTableById(id) {
    return axios.get(`${config.mdBaseUrl}/general-table/get/${id}`, {
      headers: {
        token: localStorage.getItem('token')
      }
    });
  },

  // 根据账本ID获取流水记录
  getGeneralTablesByBid(bid, userId) {
    return axios.get(`${config.mdBaseUrl}/general-table/list/${bid}/${userId}`, {
      headers: {
        token: localStorage.getItem('token')
      }
    });
  },

  // 条件查询流水记录
  searchGeneralTables(params) {
    return axios.post(`${config.mdBaseUrl}/general-table/search`, params, {
      headers: {
        token: localStorage.getItem('token')
      }
    });
  },

  // 更新流水记录
  updateGeneralTable(generalTable) {
    return axios.put(`${config.mdBaseUrl}/general-table/update`, generalTable, {
      headers: {
        token: localStorage.getItem('token')
      }
    });
  },

  // 删除流水记录
  deleteGeneralTable(id, userId) {
    return axios.delete(`${config.mdBaseUrl}/general-table/delete/${id}/${userId}`, {
      headers: {
        token: localStorage.getItem('token')
      }
    });
  },

  // 批量删除流水记录
  batchDeleteGeneralTables(ids, userId) {
    return axios.delete(`${config.mdBaseUrl}/general-table/batch-delete`, {
      data: { ids, userId },
      headers: {
        token: localStorage.getItem('token')
      }
    });
  },

  // 按日期汇总流水记录
  getSummaryByDate(bid, userId, summaryType, startDate, endDate) {
    return axios.get(`${config.mdBaseUrl}/general-table/summary/${bid}/${userId}/${summaryType}`, {
      params: {
        startDate,
        endDate
      },
      headers: {
        token: localStorage.getItem('token')
      }
    });
  }
};