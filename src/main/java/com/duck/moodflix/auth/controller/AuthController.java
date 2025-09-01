package com.duck.moodflix.auth.controller;

import com.duck.moodflix.auth.dto.KakaoLoginRequest;
import com.duck.moodflix.auth.service.KaKaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final KaKaoService kaKaoService;

    /**
     * 카카오 로그인 콜백을 처리하고 JWT를 반환합니다.
     * @return ResponseEntity<Map<String, String>> 액세스 토큰을 포함한 응답
     */
    // [수정] 불필요한 HttpServletRequest 파라미터 제거
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoLoginRequest requestDto) {
        // ✅ 서비스의 oAuthLogin 메서드가 이제 accessToken을 직접 받습니다.
        String jwtToken = kaKaoService.oAuthLogin(requestDto.getAccessToken());

        return ResponseEntity.ok(Map.of("accessToken", jwtToken));
    }

}