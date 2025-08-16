package com.example.project.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.project.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 与数据库交互，提供数据访问接口
 */
@Mapper // 标记为MyBatis映射接口
public interface UserRepository extends BaseMapper<User> {
    // 用户使用名称和密码进行登录 - 修改参数名与实体类属性名一致
    @Select("SELECT * FROM try WHERE userName = #{userName} AND password = #{password}")
    User UserLogin(String userName, String password);

    // 用户注册
    @Insert("INSERT INTO try (userName, password) VALUES (#{userName},#{password})")
    boolean UserRegister(String userName,String password);

    // 查询用户是否已存在
    @Select("SELECT COUNT(*) > 0 FROM try WHERE userName = #{userName}")
    boolean UserExist(String userName);
}