package com.dtv.global.filter;

import com.dtv.domain.entity.Member;
import com.dtv.domain.repository.MemberRepository;
import com.dtv.global.util.JwtUtil;
import com.dtv.global.execption.CustomException;
import com.dtv.global.execption.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository; // MemberRepository 추가

    public JwtAuthenticationFilter(JwtUtil jwtUtil, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        Long memberId = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                Jws<Claims> claimsJws = jwtUtil.validate(jwt);
                Claims claims = claimsJws.getBody();
                memberId = claims.get("id", Long.class);
            } catch (Exception e) {
                // 토큰 검증 실패 시 로깅 및 처리
                log.info("JWT 토큰 검증 실패: " + e.getMessage());
            }
        }

        if (memberId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER_ID));

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    member, null, null); // member 객체로 변경
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}