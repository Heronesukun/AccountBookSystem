import axios from 'axios'
import config from '@/config/config.js'

export default {
    // 获取用户创建的账本
    getCreatedBooks() {
        return axios.get(`${config.mdBaseUrl}/mainPage/getMainPageBooks`, {
            params: {
                bookMaster: localStorage.getItem('username')
            },
            headers: {
                token: localStorage.getItem('token')
            }
        })
    },

    // 获取用户参与的账本
    getParticipatedBooks() {
        return axios.get(`${config.mdBaseUrl}/mainPage/getMainPageBooksWhenParticipants`, {
            params: {
                bookMaster: localStorage.getItem('username')
            },
            headers: {
                token: localStorage.getItem('token')
            }
        })
    },

    // 创建新账本
    createBook(bookData) {
        return axios.post(`${config.mdBaseUrl}/mainPage/createNewBook`, bookData, {
            headers: {
                token: localStorage.getItem('token')
            }
        })
    },

    // 更新账本名称
    updateBookName(bookData) {
        return axios.post(`${config.mdBaseUrl}/mainPage/updateBookName`, bookData, {
            headers: {
                token: localStorage.getItem('token')
            }
        })
    },

    // 删除账本
    deleteBook(bookId) {
        return axios.post(`${config.mdBaseUrl}/mainPage/deleteBook`, {
            bookId: bookId
        }, {
            headers: {
                token: localStorage.getItem('token')
            }
        })
    }
}
