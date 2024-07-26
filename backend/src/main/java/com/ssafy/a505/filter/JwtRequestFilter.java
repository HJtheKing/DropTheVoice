package com.ssafy.a505.filter;

import com.ssafy.a505.util.JwtUtil;
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

    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String path = request.getServletPath();

        // 예외 URL 설정
        if (path.equals("/api-user/login") || path.equals("/api-user/signup")) {
            chain.doFilter(request, response);
            log.info("예외 URL");
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        final String refreshToken = getRefreshTokenFromCookie(request);

        log.info("dofilter");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            try {
                Jws<Claims> claims = jwtUtil.validate(jwt);
                request.setAttribute("userId", claims.getBody().get("id"));
                System.out.println("access token 확인 완료");
                chain.doFilter(request, response);
                return;
            } catch (JwtException e) {
                // Access Token이 유효하지 않은 경우
                log.info("access token 확인 불가");
            }
        }

        // Access Token이 없거나 유효하지 않은 경우
        if (refreshToken != null && !refreshToken.isEmpty()) {
            try {
                Jws<Claims> refreshClaims = jwtUtil.validate(refreshToken);
                String newAccessToken = jwtUtil.refreshAccessToken(refreshToken);
                response.setHeader("New-Access-Token", newAccessToken);
                request.setAttribute("Authorization", "Bearer " + newAccessToken);
                log.info("refresh token 확인 완료");
                chain.doFilter(request, response);
            } catch (JwtException ex) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Refresh Token");
            }
        } else {
            log.info("refresh token 확인 불가");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
        }
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("refreshToken")) {
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
