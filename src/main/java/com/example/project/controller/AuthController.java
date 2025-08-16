package com.example.project.controller;

import com.example.project.dto.LoginRequest;
import com.example.project.dto.RegisterRequest;
import com.example.project.entity.User;
import com.example.project.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.example.project.constant.StatusNum.*;

/**
 * 处理HTTP请求，接收前端参数，调用业务逻辑层，返回响应结果
 */
@RestController // 标记为RESTful控制器，返回JSON格式数据
@RequestMapping("/AqTest/auth") // 指定请求路径前缀
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) { // 修改构造函数参数
        this.userService = userService;
    }

    /**
     * 用户登录接口 - 使用DTO自动解析JSON请求体
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Map<String,Object> response = new HashMap<>();
        // 先检查用户是否存在，若不存在，则返回 用户不存在，请先登录。
        boolean userExist = userService.userCheck(loginRequest.getUserName());
        if(!userExist){
            response.put("status",USER_NOT_EXIST);
            response.put("message","用户名不存在，请先注册后再重新登录");
            // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            return ResponseEntity.ok(response); // 修改这里，使用200状态码
        }

        User user = userService.login(loginRequest.getUserName(), loginRequest.getPassword());
        if (user != null) {
            response.put("status",LOGIN_SUCCESS);
            response.put("message","登录成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("status",LOGIN_FAILURE);
            response.put("message","用户名或密码错误");
            // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            return ResponseEntity.ok(response); // 修改这里，使用200状态码
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        boolean exist = userService.userCheck(registerRequest.getUserName());

        Map<String,Object> response = new HashMap<>();
        // 若用户已存在，请直接登录。
        if(exist){
            response.put("status",USER_EXIST);
            response.put("message","用户已存在，禁止重复注册");
            return ResponseEntity.ok(response);
        }

        // 若用户不存在，则创建用户
        boolean registerUser = userService.userRegister(registerRequest.getUserName(),registerRequest.getPassword());
        if(registerUser){
            response.put("status",USER_REGISTER_SUCCESS);
            response.put("message","用户注册成功");
            return ResponseEntity.ok(response);
        }else{
            response.put("status",USER_REGISTER_FAILURE);
            response.put("message","用户注册失败");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}