import axios from 'axios';

export default {
    async register(data) {
        return await axios.post('/api/user/register', data);
    },
    async fetchCaptcha() {
        return `/api/captcha?timestamp=${new Date().getTime()}`;
    }
};
