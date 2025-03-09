package com.fm.books;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description
 * @Time 2025/2/23 17:13
 */

@SpringBootApplication
@MapperScan("com.fm.books.mapper")
public class MainPageApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainPageApplication.class,args);
    }
}
