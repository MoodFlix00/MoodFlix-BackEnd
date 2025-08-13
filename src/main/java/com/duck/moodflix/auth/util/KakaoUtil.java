package com.duck.moodflix.auth.util;

import com.duck.moodflix.auth.config.KakaoProperties;
import com.duck.moodflix.auth.dto.KakaoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KakaoUtil {
    private final KakaoProperties kakaoProperties;

    // 1. 인가코드로 access_token 발급 요청
    public KakaoDto.OAuthToken requestToken(String code) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .build();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.getClientId());
        params.add("redirect_uri", kakaoProperties.getRedirectUri());
        params.add("code", code);

        // 2. ★ 로그로 실제 파라미터 값 출력
        System.out.println("KAKAO TOKEN REQUEST PARAMS");
        System.out.println("client_id: " + kakaoProperties.getClientId());
        System.out.println("redirect_uri: " + kakaoProperties.getRedirectUri());


        return webClient.post()
                .uri("/oauth/token")
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData(params)) // bodyValue(params) 대신!
                .retrieve()
                .bodyToMono(KakaoDto.OAuthToken.class)
                .block();
    }



    // 2. access_token으로 사용자 프로필 요청
    public KakaoDto.KakaoProfile requestProfile(KakaoDto.OAuthToken oAuthToken) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .build();

        return webClient.get()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + oAuthToken.getAccess_token())
                .retrieve()
                .bodyToMono(KakaoDto.KakaoProfile.class)
                .block();
    }
}
