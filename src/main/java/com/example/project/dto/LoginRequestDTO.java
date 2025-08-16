package com.example.project.dto;

import lombok.Data;

/**
 * 登录请求数据传输对象
 */
@Data
public class LoginRequestDTO {
    private String userName;
    private String password;
}