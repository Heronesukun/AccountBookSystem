package com.fm.common.util;

import java.util.Calendar;
import java.util.Map;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description    token获取解析工具
 * @Time 2024/12/5 15:46
 */

public class JWTUtil {
    // 默认读取配置文件中的jwt-secret,如果没有配置，默认值为KAR98
    @Value("${jwt-secret:KAR98}")
    private String secret;

    /**
     * 通过JWT的方式得到token字符串
     * @param map 这个map中就是我们的有效荷载数据
     * @return
     */
    public String getToken(Map<String,String> map){
        JWTCreator.Builder builder = JWT.create();
        // 设置客户的payload
        map.forEach((k,v)->{
            builder.withClaim(k, v);
        });
        // 获取过期时间
        Calendar instance = Calendar.getInstance(); // 获取当前系统时间
        instance.add(Calendar.SECOND, 60 * 60);//这边我改成30分钟
        // 设置过期时间
        builder.withExpiresAt(instance.getTime());
        // 生成token
        return builder.sign(Algorithm.HMAC256(secret));
    }

    // 验证token的合法性
    // 如果没有异常说明校验通过，信息被修改和过期分别会抛出不同的异常。
    public DecodedJWT checkToken(String token){
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
    }

    /**
     * 获取token中的数据信息
     * @param token
     * @return
     */
    public Map<String, Claim> getTokenInfo(String token){
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token).getClaims();
    }
}

