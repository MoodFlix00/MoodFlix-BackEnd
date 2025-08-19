package com.duck.moodflix.auth.config;

import com.duck.moodflix.auth.util.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Claims claims = jwtTokenProvider.getClaimsFromToken(token);
            Long userId = Long.parseLong(claims.getSubject());

            // --- 🔽 이 부분 수정 ---
            String role = claims.get("role", String.class);
            Collection<? extends GrantedAuthority> authorities;

            // role 클레임이 존재하는지 확인
            if (StringUtils.hasText(role)) {
                authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
            } else {
                // 역할 정보가 없는 토큰의 경우, 권한 없음을 명시하거나 기본 권한 부여
                authorities = Collections.emptyList();
                log.warn("JWT token for user '{}' does not contain 'role' claim.", userId);
            }

            UserDetails userDetails = new User(userId.toString(), "", authorities);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 요청 헤더(Authorization)에서 'Bearer ' 접두사를 제거하고 토큰 값만 추출합니다.
     * @param request HttpServletRequest
     * @return String | null 추출된 토큰 또는 null
     */
    // JwtAuthenticationFilter.java
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.info("Authorization Header: {}", bearerToken); // ◀ 로그 추가
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}