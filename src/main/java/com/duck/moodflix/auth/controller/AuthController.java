package com.duck.moodflix.auth.controller;

import com.duck.moodflix.auth.service.KaKaoService;
import com.duck.moodflix.users.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final KaKaoService kakaoService;

    // 카카오 로그인 콜백(인가코드 받는 엔드포인트)
    // 예시: http://localhost:8080/auth/login/kakao?code=xxxx
    @GetMapping("/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String code, HttpServletRequest request) {
        User user = kakaoService.oAuthLogin(code, request);

        // 프론트에서는 세션 쿠키(JSESSIONID)로 로그인 유지
        // 로그인된 유저정보 반환 or 성공 메시지 등
        return ResponseEntity.ok(user);
    }

    // 로그인 상태 확인용(세션 기반)
    @GetMapping("/me")
    public ResponseEntity<?> getLoginUser(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        return ResponseEntity.ok(loginUser);
    }

    // 로그아웃 (세션 제거)
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().invalidate(); // 세션 삭제
        return ResponseEntity.ok("로그아웃 성공");
    }
}
