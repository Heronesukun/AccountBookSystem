package com.fm.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description
 * @Time 2024/12/5 18:55
 */

@SpringBootApplication
@MapperScan("com.fm.sso.mapper")
public class SSOApplication {
    public static void main(String[] args) {
        SpringApplication.run(SSOApplication.class, args);
    }
}
