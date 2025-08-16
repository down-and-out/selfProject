package com.example.project.dto;

import lombok.Data;

/**
 * 注册请求数据传输对象
 */
@Data
public class RegisterRequestDTO {
    private String userName;
    private String password;
}