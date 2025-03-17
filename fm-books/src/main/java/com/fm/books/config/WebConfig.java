package com.fm.books.config;

import com.fm.books.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author 张喆
 * @Company
 * @Description WebMvc配置
 * @Time 2024/10/15 9:08
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
