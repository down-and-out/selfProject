package com.example.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.project.dto.ResponseDTO;
import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import com.example.project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.example.project.constant.RedisOp.LOG_TOKEN;
import static com.example.project.constant.StatusNum.*;
import static com.example.project.utils.JWTUtils.createToken;

/**
 * 实现业务逻辑，协调数据访问层和控制器层
 */
@Slf4j // 添加此注解生成log变量
@Service // 标记为服务层组件
@RequiredArgsConstructor // Lombok注解，生成构造函数依赖注入
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 实现账号登录逻辑
     * @param userName 用户名称
     * @param password 用户密码
     * @return
     */
    @Override
    public ResponseEntity<?> login(String userName, String password) {
        // 先检查用户名称 和 密码 是否为空
        if(userName.isEmpty() || password.isEmpty()){
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_REGISTER_FAILURE,"用户名称和密码不允许为空"));
        }
        // 显示 用户名称 和 用户密码 的长度
        if(userName.length() < 3){
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_REGISTER_FAILURE,"用户名称至少为3个字"));
        }
        if(password.length() < 6){
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_REGISTER_FAILURE,"用户密码至少为6位"));
        }
        // TODO 为用户注册时密码 加密 + 加盐
        // TODO 这里可以使用 布隆过滤器 来对用户进行过滤?
        // 先检查用户是否存在
        // 若不存在，则返回 用户不存在，请先注册。
        boolean userExist= userRepository.UserExist(userName);
        if(!userExist){
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_NOT_EXIST,"用户名不存在，请先注册后再重新登录"));
        }
        // 检查用户和密码是否正确
        User user = userRepository.UserLogin(userName,password);

        // 若用户为空，返回 账号密码错误
        if(user == null){
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(LOGIN_FAILURE,"用户名或密码错误"));
        }

        // 为账号生成 token
        String token = createToken(userName);
        redisTemplate.opsForValue().set(LOG_TOKEN + token,JSON.toJSONString(user),1,TimeUnit.DAYS);

        return ResponseEntity.ok(ResponseDTO.ResponseMsg(LOGIN_SUCCESS,"登录成功"));
    }

    /**
     * 检查用户是否存在
     * @param userName
     * @return
     */
//    @Override
//    public ResponseEntity<?> userCheck(String userName) {
//        return userRepository.UserExist(userName);
//    }

    @Override
    public ResponseEntity<?> userRegister(String userName,String password){
        // 先检查用户名称 和 密码 是否为空
        if(userName.isEmpty() || password.isEmpty()){
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_REGISTER_FAILURE,"用户名称和密码不允许为空"));
        }
        // 显示 用户名称 和 用户密码 的长度
        if(userName.length() < 3){
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_REGISTER_FAILURE,"用户名称至少为3个字"));
        }
        if(password.length() < 6){
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_REGISTER_FAILURE,"用户密码至少为6位"));
        }
        // TODO 添加对用户密码中至少存在 数字，字符，字母 三者其二
        // TODO 为用户注册时密码 加密 + 加盐
        // TODO 这里可以使用 布隆过滤器 来对用户进行过滤?
        // 先检查用户是否存在
        // 若用户已存在，则返回 用户不存在，请先注册。
        boolean userExist= userRepository.UserExist(userName);
        if(userExist){
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_EXIST,"用户已存在，禁止重复注册"));
        }
        boolean userIndert = userRepository.UserRegister(userName,password);
        if(!userIndert){
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_REGISTER_FAILURE,"用户注册失败"));
        }

        // 为用户创建 token，存入 redis 中。
        String token = createToken(userName);
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        redisTemplate.opsForValue().set(LOG_TOKEN + token , JSON.toJSONString(user),1,TimeUnit.DAYS);
        return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_REGISTER_SUCCESS,"用户注册成功"));
    }

    @Override
    public ResponseEntity<?> logout(String token) {
        log.info("删除用户 token : {}", token); // 关键日志
        if (token == null || token.isEmpty()) {
            log.info("传入 token 为空");
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(LOGOUT_FAILURE,"退出失败！"));
        }
        try {
            // TODO 将 redis 的相关操作封装在单独的 RedisService 中，进行业务解耦
            // 先检查 token 是否存在
            boolean tokenExist = redisTemplate.hasKey(LOG_TOKEN + token);
            if(!tokenExist){
                return ResponseEntity.ok(ResponseDTO.ResponseMsg(LOGOUT_FAILURE,"退出异常，无法找到用户信息"));
            }
            redisTemplate.delete(LOG_TOKEN + token);
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(LOGOUT_SUCCESS,"退出成功"));
        } catch (Exception e) {
            log.info("退出时出现异常");
            // 记录日志
            e.printStackTrace();
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(LOGOUT_FAILURE,"退出失败！"));
        }
    }
}