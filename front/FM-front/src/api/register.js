import axios from 'axios';
import request from '@/utils/request';

export default {
    async register(data) {
        return await request.post('/auth/register', data);
    },
    async fetchCaptcha() {
        return `/api/captcha?timestamp=${new Date().getTime()}`;
    }
};
