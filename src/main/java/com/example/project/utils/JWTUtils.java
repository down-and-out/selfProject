package com.example.project.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j // 添加日志支持
public class JWTUtils {
    // 重命名为更有意义的变量名
    private static final String SECRET_KEY = "114514JsonWebToken";
    // 1天有效期，提取为常量便于维护
    private static final long TOKEN_EXPIRATION = 24 * 60 * 60 * 1000;

    /**
     * 创建JWT Token
     * @param userName 用户名
     * @return 生成的Token字符串
     */
    public static String createToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", userName);
        // TODO token的生成可以添加更多规则，如IP绑定、设备信息等
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 设置签发算法和密钥
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .compact();
    }

    /**
     * 解析JWT Token
     * @param token JWT Token字符串
     * @return 解析后的Claims对象，如果解析失败则返回null
     */
    public static Map<String, Object> parseToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            log.warn("尝试解析空的JWT Token");
            return null;
        }
        
        try {
            // 使用泛型化的Claims类型，避免原始类型和未检查转换
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token) // 使用更具体的方法获取Claims对象
                    .getBody();
            // 安全地转换为Map
            return claims;
        } catch (Exception e) {
            // 使用日志替代printStackTrace
            log.error("解析JWT Token失败: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 从Token中获取用户名
     * @param token JWT Token字符串
     * @return 用户名（如果解析失败则返回空Optional）
     */
    public static Optional<String> getUserNameFromToken(String token) {
        Map<String, Object> claims = parseToken(token);
        if (claims != null && claims.containsKey("userName")) {
            return Optional.ofNullable(claims.get("userName").toString());
        }
        return Optional.empty();
    }

    /**
     * 验证Token是否有效
     * @param token JWT Token字符串
     * @return Token是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.debug("Token验证失败: {}", e.getMessage());
            return false;
        }
    }

    // 对JWT进行测试
    public static void main(String[] args) {
        String tokenTest = createToken("this Is Token");
        System.out.println("生成的Token: " + tokenTest);
        
        Optional<String> userName = getUserNameFromToken(tokenTest);
        userName.ifPresent(name -> System.out.println("解析出的用户名: " + name));
        
        System.out.println("Token是否有效: " + validateToken(tokenTest));
    }
}
