package com.duck.moodflix.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tmdb")
@Getter
@Setter
public class TMDbProperties {
    private String apiKey;
    private final String posterBaseUrl = "https://image.tmdb.org/t/p/w500";
}
