import axios from 'axios';
import config from '@/config/config.js';

const mainPageBookRequest = {
    // 获取用户创建的账本
    getMainPageBooks(bookMaster) {
        return axios.get(`${config.mdBaseUrl}/mainPage/getMainPageBooks`, {
            params: { bookMaster },
            headers: { token: localStorage.getItem('token') }
        });
    },

    // 获取用户参与的账本
    getMainPageBooksWhenParticipants(bookMaster) {
        return axios.get(`${config.mdBaseUrl}/mainPage/getMainPageBooksWhenParticipants`, {
            params: { bookMaster },
            headers: { token: localStorage.getItem('token') }
        });
    },

    // 创建新账本
    createNewBook(accountBook) {
        return axios.put(`${config.mdBaseUrl}/mainPage/createNewBook`, accountBook, {
            headers: { token: localStorage.getItem('token') }
        });
    },

    // 更新账本名称
    updateBookName(accountBook) {
        return axios.put(`${config.mdBaseUrl}/mainPage/updateBookName`, accountBook, {
            headers: { token: localStorage.getItem('token') }
        });
    },

    // 删除账本
    deleteBook(accountBook) {
        return axios.put(`${config.mdBaseUrl}/mainPage/deleteBook`, accountBook, {
            headers: { token: localStorage.getItem('token') }
        });
    }
};

export default mainPageBookRequest;