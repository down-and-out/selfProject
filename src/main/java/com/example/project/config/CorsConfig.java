package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 全局跨域配置类
 * 解决前后端分离架构中的跨域问题
 */
@Configuration
public class CorsConfig {
    /**
     * 创建CORS过滤器
     * @return CorsFilter实例
     */
    @Bean
    public CorsFilter corsFilter() {
        // 1. 创建CORS配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 2. 创建CORS配置对象
        CorsConfiguration config = new CorsConfiguration();
        // 3. 配置跨域属性
        // 允许所有来源的请求，可以替换为具体的域名如"http://localhost:8081"
        config.addAllowedOriginPattern("*");
        // 允许发送Cookie
        config.setAllowCredentials(true);
        // 允许所有HTTP方法（GET, POST, PUT, DELETE等）
        config.addAllowedMethod("*");
        // 允许所有请求头
        config.addAllowedHeader("*");
        // 设置预检请求的有效期（秒）
        config.setMaxAge(3600L);
        // 4. 为所有URL注册CORS配置
        source.registerCorsConfiguration("/**", config);
        // 5. 返回CORS过滤器
        return new CorsFilter(source);
    }
}