package com.duck.moodflix.auth.controller;

import com.duck.moodflix.auth.dto.KakaoLoginRequest;
import com.duck.moodflix.auth.dto.LoginResponseDto;
import com.duck.moodflix.auth.service.KaKaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final KaKaoService kaKaoService;

    /**
     * 카카오 로그인 콜백을 처리하고 JWT를 반환합니다.
     * @return ResponseEntity<Map<String, String>> 액세스 토큰을 포함한 응답
     */
    @PostMapping(value = "/kakao",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseDto> kakaoLogin(@Valid @RequestBody KakaoLoginRequest requestDto) {
        //  [수정] @Valid 추가
        LoginResponseDto responseDto = kaKaoService.oAuthLogin(requestDto.getAccessToken());
        return ResponseEntity.ok(responseDto);
    }


}