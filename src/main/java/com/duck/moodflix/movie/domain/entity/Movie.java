package com.duck.moodflix.movie.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(
        name = "movies",
        indexes = {
                @Index(name = "ux_movies_tmdb_id", columnList = "tmdb_id", unique = true)
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tmdb_id", nullable = false, unique = true)
    private Long tmdbId;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @Column(length = 255)
    private String posterUrl;

    @Column(length = 100)
    private String genre;          // 대표 장르(요약 저장)

    private LocalDate releaseDate;

    private Double voteAverage;    // TMDb 평점 요약

    // 키워드 연결(N:M 조인 엔티티)
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MovieKeyword> movieKeywords = new HashSet<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 연관관계 편의 메서드(선택)
    public void addMovieKeyword(MovieKeyword mk) {
        this.movieKeywords.add(mk);
        mk.setMovie(this);
    }

    public void removeMovieKeyword(MovieKeyword mk) {
        this.movieKeywords.remove(mk);
        mk.setMovie(null);
    }
}
