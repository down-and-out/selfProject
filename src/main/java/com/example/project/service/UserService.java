package com.example.project.service;

import com.example.project.entity.User;

public interface UserService {
    User login(String userName, String password);
    boolean userCheck(String userName);
    boolean userRegister(String userName,String password);
}