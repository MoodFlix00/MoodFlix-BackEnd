package com.duck.moodflix.movie.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tmdb_reviews")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TmdbReview {

    @Id
    @Column(length = 64) // TMDb review id는 문자열, 길이 여유
    private String id;

    private String author;

    @Column(columnDefinition = "TEXT")
    private String content;

    // 선택 필드
    private Double rating;      // author_details.rating
    private String url;         // review URL
    private String createdAt;   // ISO 문자열로 저장(간단)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    // private 생성자 + 정적 팩토리
    private TmdbReview(String id, String author, String content,
                       Double rating, String url, LocalDateTime createdAt,
                       Movie movie) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.rating = rating;
        this.url = url;
        this.createdAt = String.valueOf(createdAt);
        this.movie = movie;
    }

    public static TmdbReview of(
            String id, String author, String content, Double rating, String url, String createdAt, Movie movie) {

        TmdbReview r = new TmdbReview();
        r.id = id;
        r.author = author;
        r.content = content;
        r.rating = rating;
        r.url = url;
        r.createdAt = createdAt;
        r.movie = movie;
        return r;
    }
}
