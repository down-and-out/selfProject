package com.example.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 映射数据库表，封装数据
 */
@Data
@TableName("try") // 指定数据库表名
public class User {
    @TableId(type = IdType.AUTO) // 指定主键字段
    private Long id;
    private String userName;
    private String password;
    @TableField(fill = FieldFill.INSERT) // 指定字段属性， fill = FieldFill.INSERT 表示插入时自动填充
    private LocalDateTime createTime;
}