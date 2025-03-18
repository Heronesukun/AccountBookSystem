import axios from 'axios';
import config from '@/config/config.js'; // 修正导入路径

const bookMemberRequest = {
  // 获取账本所有成员
  getBookMembers(bookId) {
    return axios.get(`${config.mdBaseUrl}/bookMember/getMembers`, {
      params: { bookId },
      headers: { token: localStorage.getItem('token') }
    });
  },

  // 添加账本成员
  addBookMember(bookId, memberName, position) {
    return axios.post(`${config.mdBaseUrl}/bookMember/add`, null, {
      params: { bookId, memberName, position },
      headers: { token: localStorage.getItem('token') }
    });
  },

  // 删除账本成员
  removeBookMember(bookId, position) {
    return axios.delete(`${config.mdBaseUrl}/bookMember/remove`, {
      params: { bookId, position },
      headers: { token: localStorage.getItem('token') }
    });
  },

  // 更新账本成员
  updateBookMember(bookId, oldMemberName, newMemberName) {
    return axios.put(`${config.mdBaseUrl}/bookMember/update`, null, {
      params: { bookId, oldMemberName, newMemberName },
      headers: { token: localStorage.getItem('token') }
    });
  },

  // 检查用户是否为账本成员
  checkIsMember(bookId, memberName) {
    return axios.get(`${config.mdBaseUrl}/bookMember/check`, {
      params: { bookId, memberName },
      headers: { token: localStorage.getItem('token') }
    });
  }
};

export default bookMemberRequest;