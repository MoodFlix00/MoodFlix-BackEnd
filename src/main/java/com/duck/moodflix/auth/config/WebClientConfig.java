package com.duck.moodflix.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean("tmdbWebClient") // (1) TMDb용 WebClient에 "tmdbWebClient" 라는 이름 부여
    public WebClient tmdbWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.themoviedb.org/3")
                .build();
    }

    @Bean("kakaoWebClient") // (2) 카카오용 WebClient에 "kakaoWebClient" 라는 이름 부여
    public WebClient kakaoWebClient() {
        return WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .build();
    }
}