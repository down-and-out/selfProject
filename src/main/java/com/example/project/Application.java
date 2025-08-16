package com.example.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.mybatis.spring.annotation.MapperScan;

/**
 * 项目的入口类，负责启动Spring Boot应用
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.example.project.repository") // 指定MyBatis-Plus Mapper接口所在包路径
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
