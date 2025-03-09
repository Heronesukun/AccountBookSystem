package com.fm.sso.config;

import com.fm.common.util.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description
 * @Time 2024/12/5 15:44
 */

@Configuration
public class SSOConfig {
    @Bean
    public JWTUtil jwtUtil(){
        return new JWTUtil();
    }
}
