package com.fm.system.config;

import com.fm.system.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description
 * @Time 2025/1/2 10:57
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 所有请求全部拦截
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
    }
}
