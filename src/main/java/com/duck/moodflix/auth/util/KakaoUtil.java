package com.duck.moodflix.auth.util;

import com.duck.moodflix.auth.config.KakaoProperties;
import com.duck.moodflix.auth.dto.KakaoDto;
import com.duck.moodflix.auth.dto.KakaoTokenResponseDto; // DTO import
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KakaoUtil {

    private final KakaoProperties kakaoProperties;

    // [수정] 반환 타입을 KakaoTokenResponseDto로 변경
    public KakaoTokenResponseDto requestToken(String code) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .build();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.getClientId());
        params.add("redirect_uri", kakaoProperties.getRedirectUri());
        params.add("code", code);

        return webClient.post()
                .uri("/oauth/token")
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .bodyToMono(KakaoTokenResponseDto.class) // [수정] DTO 클래스 타입 변경
                .block();
    }

    // [수정] 파라미터 타입을 KakaoTokenResponseDto로 변경
    public KakaoDto.KakaoProfile requestProfile(KakaoTokenResponseDto oAuthToken) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .build();

        return webClient.get()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + oAuthToken.getAccessToken()) // [수정] 필드명 변경
                .retrieve()
                .bodyToMono(KakaoDto.KakaoProfile.class)
                .block();
    }
}