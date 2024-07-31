package com.ssafy.a505.domain.controller;

import com.ssafy.a505.domain.entity.User;
import com.ssafy.a505.global.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;



@RestController
@RequestMapping("/api-user")
@Tag(name = "UserController", description = "유저 CRUD")
@CrossOrigin("*")
public class UserController {

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        // 로그인 처리 로직
        Map<String, Object> result = new HashMap<>();
        HttpStatus status;

        if(user.getUserId() == 1234 && user.getUserPassword().equals("1234")) {
            // 토큰 생성
            Map<String, String> tokens = jwtUtil.createTokens(user.getUserId());

            // Refresh Token 쿠키 생성
            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokens.get("refreshToken"))
                    .httpOnly(true)
                    .secure(false) // true in production
                    .path("/")
                    .maxAge(60 * 60 * 24 * 30) // 30일
                    .sameSite("Lax") // "Strict" for more security
                    .build();

            // Access Token 응답 본문에 포함
            result.put("message", "SUCCESS");
            result.put("access-token", tokens.get("accessToken"));
            status = HttpStatus.ACCEPTED;

            return ResponseEntity.status(status)
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString()) // 헤더에 Refresh Token 쿠키 추가
                    .body(result); // 본문에 Access Token 추가
        } else {
            result.put("message", "FAIL");
            status = HttpStatus.UNAUTHORIZED; // 더 적절한 상태 코드로 변경
            return new ResponseEntity<>(result, status);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        // Refresh Token 쿠키 만료
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false) // true in production
                .path("/")
                .maxAge(0) // 쿠키 즉시 만료
                .sameSite("Lax") // "Strict" for more security
                .build();

        Map<String, String> result = new HashMap<>();
        result.put("message", "Logged out successfully");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString()) // 만료된 쿠키 설정
                .body(result);
    }

}
