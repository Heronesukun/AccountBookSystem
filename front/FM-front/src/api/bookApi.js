import axios from 'axios'
import config from '@/config/config.js'

export default {
    getMyBooks() {
        return axios.get(config.baseUrl + '/mainPage/getMainPageBooks', {
            params: {
                bookMaster: localStorage.getItem('username')
            },
            headers: {
                token: localStorage.getItem('token')
            }
        })
    },

    getParticipatedBooks() {
        return axios.get(config.baseUrl + '/mainPage/getMainPageBooksWhenParticipants', {
            params: {
                bookMaster: localStorage.getItem('username')
            },
            headers: {
                token: localStorage.getItem('token')
            }
        })
    },

    createNewBook(bookData) {
        return axios.post(config.baseUrl + '/mainPage/createNewBook', bookData, {
            headers: {
                token: localStorage.getItem('token')
            }
        })
    },

    updateBookName(bookData) {
        return axios.post(config.baseUrl + '/mainPage/updateBookName', bookData, {
            headers: {
                token: localStorage.getItem('token')
            }
        })
    },

    deleteBook(bookData) {
        return axios.post(config.baseUrl + '/mainPage/deleteBook', bookData, {
            headers: {
                token: localStorage.getItem('token')
            }
        })
    }
}
