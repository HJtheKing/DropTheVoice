/*
package com.ssafy.a505.global.filter;

import com.ssafy.a505.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends HttpFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String path = request.getServletPath();

        System.out.println();
        log.info("doFilter 시작");

        // 예외 URL 설정
        if (path.equals("/api-user/login") || path.equals("/api-user/signup")) {
            log.info("예외 URL");
            chain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        final String refreshToken = getRefreshTokenFromCookie(request);

        log.info("access token : "+authorizationHeader);
        log.info("refresh token : "+refreshToken);
        
        // 먼저 accessToken 유무를 확인
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring(7);
            try {
                Jws<Claims> claims = jwtUtil.validate(accessToken);
                request.setAttribute("userId", claims.getBody().get("id"));
                log.info("access token 확인 완료");
                chain.doFilter(request, response);
                return;
            } catch (JwtException e) {
                log.info("access token 확인 불가: {}", e.getMessage());
            }
        }

        // 없으면 refreshToken
        if (refreshToken != null && !refreshToken.isEmpty()) {
            try {
                Jws<Claims> refreshClaims = jwtUtil.validate(refreshToken);
                String newAccessToken = jwtUtil.refreshAccessToken(refreshToken);
                response.setHeader("New-Access-Token", newAccessToken);
                request.setAttribute("Authorization", "Bearer " + newAccessToken);
                log.info("refresh token 확인 완료");
                chain.doFilter(request, response);
                return;
            } catch (JwtException ex) {
                log.info("refresh token 확인 불가: {}", ex.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Refresh Token");
                return;
            }
        }

        log.info("access token 및 refresh token 확인 불가");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("refreshToken")) {
                    log.info("yes");
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화
    }

    @Override
    public void destroy() {
        // 필터 종료 작업
    }
}
*/