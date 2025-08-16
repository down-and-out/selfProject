package com.example.project.service;

import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> login(String userName, String password);
    ResponseEntity<?> userRegister(String userName,String password);
    ResponseEntity<?> logout(String token);
}