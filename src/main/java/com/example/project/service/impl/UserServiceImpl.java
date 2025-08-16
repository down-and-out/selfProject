package com.example.project.service.impl;

import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import com.example.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 实现业务逻辑，协调数据访问层和控制器层
 */
@Service // 标记为服务层组件
@RequiredArgsConstructor // Lombok注解，生成构造函数依赖注入
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    // 移除BCryptPasswordEncoder依赖

    @Override
    public User login(String userName, String password) {
        // 直接使用userRepository.UserLogin查询，不使用passwordEncoder
        User user = userRepository.UserLogin(userName, password);
        if (user != null) {
            return user;
        }
        return null;
    }

    @Override
    public boolean userCheck(String userName) {
        return userRepository.UserExist(userName);
    }

    @Override
    public boolean userRegister(String userName,String password){
        userRepository.UserRegister(userName,password);
        return true;
    }
}