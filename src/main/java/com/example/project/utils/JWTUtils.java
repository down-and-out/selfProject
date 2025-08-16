package com.example.project.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    private static final String jwtToken = "114514JsonWebToken";

    public static String createToken(String userName){
        Map<String,Object> claims = new HashMap<>();
        claims.put("userName",userName);
        // TODO token 的生成其实能够添加更多的规则 --> 防止
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,jwtToken) // 设置签发算法为 HS256，密钥为 jwtToken
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
        return jwtBuilder.compact();
    }

    public static Map<String,Object> parseToken(String token){
        try {
            Jwt prase = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String,Object>) prase.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // 对 JWT 进行测试
    public static void main(String[] args) {
        String tokenTest = createToken("this Is Token");
        System.out.println(tokenTest);
        Map<String,Object> parseToken = parseToken(tokenTest);
        if (parseToken != null) {
            System.out.println(parseToken.get("userName"));
        }
    }
}
