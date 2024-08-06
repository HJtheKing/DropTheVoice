package com.ssafy.a505.global.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private String key = "SSAFY_NonMajor_JavaTrack_SecretKey" ;
    private SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

    // 다양한 데이터를 Map으로 받아서 처리를 할 수도 있지만, 심플하게 ID만 받아서 토큰을 만들어보자
    public String createToken(String name, long id) {
        Date exp = new Date(System.currentTimeMillis() + 1000*60*60); // 1시간, 1초 = 1000
        return Jwts.builder().header().add("typ", "JWT").and().claim("name", name).claim("id", id)
                .expiration(exp).signWith(secretKey).compact();
    }

    // 실제로 확인하려고 하는 용도가 아니라 유효기간이 지나면 알아서 에러 발생
    public Jws<Claims> validate(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);

    }
}



