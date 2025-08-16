package com.example.project.controller;

import com.example.project.dto.LoginRequestDTO;
import com.example.project.dto.RegisterRequestDTO;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 处理HTTP请求，接收前端参数，调用业务逻辑层，返回响应结果
 */
@RestController // 标记为RESTful控制器，返回JSON格式数据
@RequestMapping("/AqTest/auth") // 指定请求路径前缀
public class AuthController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录接口 - 使用DTO自动解析JSON请求体
     * @param loginRequestDTO
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.login(loginRequestDTO.getUserName(),loginRequestDTO.getPassword());
    }

    /**
     *
     * @param registerRequestDTO 前端传入的用户信息
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequestDTO){
        return userService.userRegister(registerRequestDTO.getUserName(),registerRequestDTO.getPassword());
    }

    /**
     * 用户登出
     * @param token
     * @return
     */
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token){
        return userService.logout(token);
    }
}