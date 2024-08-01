package com.ssafy.a505.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {
    private String key = "SSAFY_NonMajor_JavaTrack_SecretKey" ;
    private SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    private int accessTokenExp = 1;
    private int refreshTokenExp = 100;

    private Date exp(int t){
        return new Date(System.currentTimeMillis() + 1000*60*t); // t분
    }

    // refresh 토큰, access 토큰 생성
    public Map<String, String> createTokens(long id) {
        String accessToken =  Jwts.builder().header().add("typ", "JWT").and().claim("id", id)
                .expiration(exp(accessTokenExp)).signWith(secretKey).compact();
        String refreshToken = Jwts.builder().header().add("typ", "JWT").and().claim("id", id)
                .expiration(exp(refreshTokenExp)).signWith(secretKey).compact();
        Map<String, String> createdTokens = new HashMap<>();
        createdTokens.put("accessToken", accessToken);
        createdTokens.put("refreshToken", refreshToken);
        return createdTokens;
    }

    // 토큰 유효성 (확인
    public Jws<Claims> validate(String token) {
        try {
            log.info("validate");
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우
            System.out.println("Token expired");
            throw e;
        } catch (UnsupportedJwtException e) {
            // 지원되지 않는 토큰 형식의 경우
            System.out.println("Unsupported token");
            throw e;
        } catch (MalformedJwtException e) {
            // 손상되었거나 올바르지 않은 형식의 토큰인 경우
            System.out.println("Malformed token");
            throw e;
        } catch (SignatureException e) {
            // 서명이 유효하지 않은 경우
            System.out.println("Invalid signature");
            throw e;
        } catch (IllegalArgumentException e) {
            // 토큰이 null이거나 빈 문자열인 경우
            System.out.println("Token is null or empty");
            throw e;
        }
    }

    // refreshToken을 검증하고 유효하면 새로운 accessToken을 발급하는 메서드
    public String refreshAccessToken(String refreshToken) {
        log.info("refresh 토큰 검증 시작");
        try {
            Jws<Claims> claimsJws = validate(refreshToken);
            log.info("refresh 토큰 검증 완료");
            Claims claims = claimsJws.getBody();
            Long id = claims.get("id", Long.class);
            return Jwts.builder().header().add("typ", "JWT").and().claim("id", id)
                    .expiration(exp(accessTokenExp)).signWith(secretKey).compact();
        } catch (JwtException e) {
            // 토큰이 유효하지 않은 경우 null 반환
            log.info("refresh 토큰 검증 실패");
            return null;
        }
    }
}

