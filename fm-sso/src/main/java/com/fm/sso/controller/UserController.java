package com.fm.sso.controller;

import com.fm.common.pojo.FMResult;
import com.fm.pojo.User;
import com.fm.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description
 * @Time 2024/12/5 15:58
 */

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public FMResult login(@RequestBody User user) {
        return userService.login(user.getUsername(), user.getUserPass());
    }

    /**
     * 登录状态校验
     *
     * @param token
     * @return
     */
    @RequestMapping("/check")
    public FMResult check(String token) {
        return userService.checkLogin(token);
    }
}
