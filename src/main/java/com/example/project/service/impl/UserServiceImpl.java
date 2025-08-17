package com.example.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.project.config.BloomFilterConfig;
import com.example.project.dto.ResponseDTO;
import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import com.example.project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RBloomFilter<String> userBloomFilter;

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
        if(userName.length() < 3 || userName.length() > 15){
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_REGISTER_FAILURE,"用户名称长度限制在3 ~ 15字之间"));
        }
        if(password.length() < 6 || password.length() > 15){
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_REGISTER_FAILURE,"用户密码长度限制在6 ~ 15个字符"));
        }
        // TODO 这里可以写一个 loginDTO,只获取用户的账号，密码和加盐，减少数据库的查询量，提高速度
        // TODO 为用户注册时密码 加密 + 加盐
        // TODO 这里可以使用 布隆过滤器 来对用户进行过滤?
        // 使用 布隆过滤器 判断用户是否存在
        if(!userBloomFilter.contains(userName)){
            log.debug("布隆过滤器判断用户 {} 不存在，直接返回",userName);
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_NOT_EXIST,"用户名不存在，请先注册后再重新登录"));
        }
        // 先检查用户是否存在
        // 若不存在，则返回 用户不存在，请先注册。
        User user= userRepository.FindByUserName(userName);
        if(user == null){
            log.debug("布隆过滤器误判用户 {} 存在",userName);
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_NOT_EXIST,"用户名不存在，请先注册后再重新登录"));
        }

        // 登录逻辑修改为一次查询
        // 如果从数据库获取的密码不同与传入的密码，则返回登录失败。
        if(!user.getPassword().equals(password)){
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(LOGIN_FAILURE,"用户名或密码错误"));
        }

        // 为账号生成 token
        String token = createToken(userName);
        // TODO 如果只允许用户在一个设备登录，那么我们必须在将 token 插入数据库前先检查是否有旧的 token，若有则需要先删除
        stringRedisTemplate.opsForValue().set(LOG_TOKEN + token,JSON.toJSONString(user),1,TimeUnit.DAYS);

        return ResponseEntity.ok(ResponseDTO.ResponseMsg(LOGIN_SUCCESS,"登录成功"));
    }

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
        // 使用 布隆过滤器帮助判断用户是否存在
        if(!userBloomFilter.contains(userName)){
            log.debug("布隆过滤器判断用户 {} 不存在，直接返回",userName);
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_NOT_EXIST,"用户名不存在，请先注册后再重新登录"));
        }
        // 先检查用户是否存在
        // 若用户已存在，则返回 用户不存在，请先注册。
        User userExist= userRepository.FindByUserName(userName);
        if(userExist != null){
            log.debug("布隆过滤器误判用户 {} 存在",userName);
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_EXIST,"用户已存在，禁止重复注册"));
        }
        boolean userIndert = userRepository.UserRegister(userName,password);
        if(!userIndert){
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(USER_REGISTER_FAILURE,"用户注册失败"));
        }

        // TODO 这里可以做两种逻辑，一种是注册成功后系统自动帮助登录。另外一种是注册成功后跳转回登录页面，需要用户再次手动登录。
        // 为用户创建 token，存入 redis 中。
        String token = createToken(userName);
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        stringRedisTemplate.opsForValue().set(LOG_TOKEN + token , JSON.toJSONString(user),1,TimeUnit.DAYS);

        // 注册成功之后，将用户添加到 布隆过滤器 中
        userBloomFilter.add(userName);
        log.info("用户 {} 注册成功，添加到布隆过滤器中",userName);

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
            boolean tokenExist = stringRedisTemplate.hasKey(LOG_TOKEN + token);
            if(!tokenExist){
                return ResponseEntity.ok(ResponseDTO.ResponseMsg(LOGOUT_FAILURE,"退出异常，无法找到用户信息"));
            }
            stringRedisTemplate.delete(LOG_TOKEN + token);
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(LOGOUT_SUCCESS,"退出成功"));
        } catch (Exception e) {
            log.info("退出时出现异常");
            return ResponseEntity.ok(ResponseDTO.ResponseMsg(LOGOUT_FAILURE,"退出失败！"));
        }
    }
}