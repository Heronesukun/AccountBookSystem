package com.fm.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author 张喆
 * @Company
 * @Description
 * @Time 2024/10/14 17:11
 */
@SpringBootApplication
@MapperScan("com.fm.system.mapper")
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class,args);
    }
}
