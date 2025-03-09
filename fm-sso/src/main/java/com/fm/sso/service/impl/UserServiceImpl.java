package com.fm.sso.service.impl;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.fm.common.pojo.FMResult;
import com.fm.common.util.Constant;
import com.fm.common.util.JSONTools;
import com.fm.common.util.JWTUtil;
import com.fm.pojo.User;
import com.fm.sso.mapper.UserMapper;
import com.fm.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description
 * @Time 2024/12/5 16:28
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public FMResult login(String username, String password) {
        // 通过用户名查询用户
        User user = userMapper.selectByUsername(username);
        // 校验登录信息
        if(user == null){// 用户名错误
            return FMResult.error("用户名或密码错误");
        }
        // 校验密码   将传过来的密码进行加密之后再和数据库查询的密码进行比较
        String tempPass = DigestUtils.md5DigestAsHex(password.getBytes());
        if(user.getUserPass().equals(tempPass)){
            // 成功，生成token，
            Map claim = new HashMap();
            claim.put("userId",user.getUserId().toString());// 将userId放入claim
            String token = jwtUtil.getToken(claim);
            // 将token保存到redis中 以token为key
            String key = "TOKEN_" + token;
            // 设置token，设置其次数为1
            stringRedisTemplate.opsForValue().increment(key);
            //  设置token在redis中的有效期24小时
            stringRedisTemplate.expire(key,24, TimeUnit.HOURS);
            // 脱敏处理
            user.setUserPass(null);
            // 将用户信息也放在redis中
            key = "USER_" + token;
            stringRedisTemplate.opsForValue().set(key, JSONTools.object2json(user));
            // 设置时间
            stringRedisTemplate.expire(key,24, TimeUnit.HOURS);
            // 返回登录成功的结果
            return FMResult.success(Constant.RESPONSE_STATUS_OK,token,user);
        }
        // 返回登录结果
        return FMResult.error("用户名或密码错误");
    }

    @Override
    public FMResult checkLogin(String token) {
        // 直接校验token
        try {
            jwtUtil.checkToken(token);
        }  catch (TokenExpiredException e) {
            System.err.println("token 已经过期 :" + e.getMessage());
            //return FMResult.error("登录已经过期");
            // 判断redis中的token是否已经过期了？
            String key = "TOKEN_" + token;
            if(stringRedisTemplate.hasKey(key) && stringRedisTemplate.opsForValue().increment(key) <= 24){
                // 取出用户信息
                String userKey = "USER_" + token;
                String userJson = stringRedisTemplate.opsForValue().get(userKey);
                User user = JSONTools.json2object(userJson,User.class);
                // 说明token过期了，而且24小时内续签次数是小于24的，就可以续签
                Map<String,String> map = new HashMap<>();
                map.put("userId",user.getUserId().toString());
                String newToken = jwtUtil.getToken(map);
                // 重新设置reids中的内容
                String newKey = "TOKEN_" + newToken;
                stringRedisTemplate.opsForValue().set(newKey,stringRedisTemplate.opsForValue().increment(key).toString());
                stringRedisTemplate.expire(newKey,stringRedisTemplate.getExpire(key),TimeUnit.SECONDS);
                // 清除原来的key
                stringRedisTemplate.delete(key);
                // 用户信息也需要重新处理一次
                // 产生新的userKey
                String newUserKey = "USER_" + newToken;
                stringRedisTemplate.opsForValue().set(newUserKey,userJson);
                //设置时间
                stringRedisTemplate.expire(newUserKey,stringRedisTemplate.getExpire(userKey),TimeUnit.SECONDS);
                // 清除原来的user信息
                stringRedisTemplate.delete(userKey);
                // 返回一个FMResult，其中状态码为特定的507，表示token续签成功，并且将新的token放在msg中
                return FMResult.success(Constant.AUTHENTICATION_RENEW,newToken,user);
            }
        } catch (JWTDecodeException | SignatureVerificationException e){
            return FMResult.error("校验失败");
        }
        // 校验成功
        // 从toke中获取用户编号
        Map<String, Claim> tokenInfo = jwtUtil.getTokenInfo(token);
        Long userId = new Long(tokenInfo.get("userId").asString());
        // 查询用户信息
        User user = userMapper.selectByUserId(userId);
        // 脱敏处理
        user.setUserPass(null);
        return FMResult.success(Constant.RESPONSE_STATUS_OK,token,user);
    }
}