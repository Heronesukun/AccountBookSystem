// 从config.js中结构出ssoBaseUrl
import config from '@/config/config.js';
import axios from "axios";

const loginRequest = {
    /**
     * 登录的函数
     * @param username 用户名
     * @param passwrod 密码
     * @param callBack 回调函数
     */
    doLogin(user,callBack){
        console.log(user)
        axios.post(config.ssoBaseUrl + "/auth/login",user).then((res)=>callBack(res.data));
    },
    /**
     * 校验用户登录状态
     * 这个方法需要同步请求
     * @param token
     * @param callBack
     */
    async  checkLogin(token){
        // 直接返回查询的结果
        let res = await axios.get(config.ssoBaseUrl + "/auth/check?token="+token);
        console.log(res)
        return res.data;
    }

}


export default loginRequest;