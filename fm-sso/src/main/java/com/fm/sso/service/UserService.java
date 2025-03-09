package com.fm.sso.service;

import com.fm.common.pojo.FMResult;

public interface UserService {
    /**
     * 根据用户id查询信息
     *
     * @param username
     * @return
     */
    FMResult login(String username, String password);

    /**
     * 检查当前用户的登录状态
     *
     * @param token 要查询的用户token信息
     * @return
     */
    FMResult checkLogin(String token);
}