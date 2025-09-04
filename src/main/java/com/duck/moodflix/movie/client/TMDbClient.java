package com.duck.moodflix.movie.client;

import com.duck.moodflix.auth.config.TMDbProperties;
import com.duck.moodflix.movie.dto.tmdb.TMDbMovieDetailDto;
import com.duck.moodflix.movie.dto.tmdb.TMDbMovieListResponse;
import com.duck.moodflix.movie.dto.tmdb.reviews.ReviewsPageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class TMDbClient {
    private final WebClient webClient;
    private final TMDbProperties tmdbProperties;

    public TMDbClient(@Qualifier("tmdbWebClient") WebClient webClient,
                      TMDbProperties tmdbProperties) {
        this.webClient = webClient;
        this.tmdbProperties = tmdbProperties;
    }

    public TMDbMovieListResponse getPopular(int page) {
        return webClient.get()
                .uri(b -> b.path("/movie/popular")
                        .queryParam("api_key", tmdbProperties.getApiKey())
                        .queryParam("language", "ko-KR")
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .bodyToMono(TMDbMovieListResponse.class)
                .block();
    }

    public TMDbMovieDetailDto getMovieDetail(Long tmdbId) {
        return webClient.get()
                .uri(b -> b.path("/movie/{id}")
                        .queryParam("api_key", tmdbProperties.getApiKey())
                        .queryParam("language", "ko-KR")
                        .queryParam("append_to_response",
                                "keywords,credits,reviews,release_dates,images,videos,similar,recommendations")
                        .queryParam("reviews.page", 1)
                        .queryParam("include_image_language", "ko,null,en")
                        .queryParam("include_video_language", "ko,en,null")
                        .build(tmdbId))
                .retrieve()
                .bodyToMono(TMDbMovieDetailDto.class)
                .block();
    }

    public ReviewsPageDto getReviews(Long tmdbId, String lang, int page) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/{movieId}/reviews")
                        .queryParam("api_key", tmdbProperties.getApiKey())
                        .queryParam("language", lang) // "en-US" / "ko-KR"
                        .queryParam("page", page)
                        .build(tmdbId))
                .retrieve()
                .bodyToMono(ReviewsPageDto.class)
                .block();
    }
}
