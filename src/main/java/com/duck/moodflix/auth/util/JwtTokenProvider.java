package com.duck.moodflix.auth.util;

import com.duck.moodflix.users.domain.entity.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long expirationMilliseconds;

    public JwtTokenProvider(@Value("${jwt.secret.key}") String secretKey,
                            @Value("${jwt.expiration.ms}") long expirationMilliseconds) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMilliseconds = expirationMilliseconds;
    }

    // [수정] 파라미터를 Set<Role>에서 단일 Role로 변경
    public String generateToken(Long userId, Role role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMilliseconds);

        return Jwts.builder()
                .subject(Long.toString(userId)) // subject 클레임 설정
                .claim("role", role.name())     // "role" 커스텀 클레임 추가
                .issuedAt(now)                  // 발급 시간 설정
                .expiration(expiryDate)           // 만료 시간 설정
                .signWith(key)                  // 서명
                .compact();                     // JWT 문자열 생성
    }


    public Claims getClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}