package com.example.project.constant;

public class StatusNum {
    // 登录状态码
    // 登录成功
    public static final int LOGIN_SUCCESS = 200000;
    // 登录失败：账号或密码错误
    public static final int LOGIN_FAILURE = 300000;
    // 退出成功
    public static final int LOGOUT_SUCCESS = 200100;
    // 退出失败
    public static final int LOGOUT_FAILURE = 300100;

    // 用户查询状态码
    // 用户名不存在
    public static final int USER_NOT_EXIST = 200001;
    // 用户名存在
    public static final int USER_EXIST = 300001;

    // 用户注册状态码
    // 用户注册成功
    public static final int USER_REGISTER_SUCCESS = 200002;
    // 用户注册失败
    public static final int USER_REGISTER_FAILURE = 300002;
}
