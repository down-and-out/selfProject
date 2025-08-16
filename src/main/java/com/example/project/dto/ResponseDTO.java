package com.example.project.dto;

import java.io.Serializable;

public class ResponseDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // 状态码常量定义
    public static final int SUCCESS = 200000;
    public static final int USER_NOT_EXIST = 401001;
    public static final int LOGIN_FAILURE = 401002;
    public static final int PARAM_ERROR = 400001;
    public static final int SERVER_ERROR = 500001;
    
    private int status;
    private String message;
    private T data;

    // 三参均有的构造
    public static <T>ResponseDTO<T> ResponseMsg(int status, String msg, T data){
        return new ResponseDTO<T>(status,msg,data);
    }

    // 不含 data 的构造
    public static ResponseDTO<Void> ResponseMsg(int status, String msg){
        return new ResponseDTO<>(status,msg);
    }
    
    // 私有构造函数
    private ResponseDTO(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    private ResponseDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }
    
    // Getter和Setter
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    @Override
    public String toString() {
        return "ResponseDTO{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
