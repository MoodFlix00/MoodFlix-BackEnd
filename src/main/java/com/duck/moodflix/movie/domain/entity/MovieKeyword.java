package com.duck.moodflix.movie.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "movie_keywords",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_movie_keywords_movie_keyword",
                columnNames = {"movie_id", "keyword_id"}
        ),
        indexes = {
                @Index(name = "idx_movie_keywords_movie", columnList = "movie_id"),
                @Index(name = "idx_movie_keywords_keyword", columnList = "keyword_id")
        }
)
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class MovieKeyword {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ★ PK 필수 (sql_require_primary_key=ON 대응)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    @JsonBackReference // 역참조는 직렬화에서 제외(자식 쪽)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    public static MovieKeyword of(Movie movie, Keyword keyword) {
        return MovieKeyword.builder().movie(movie).keyword(keyword).build();
    }
}
