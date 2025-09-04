package com.duck.moodflix.movie.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "keywords",
        indexes = {
                @Index(name = "idx_keywords_name", columnList = "name")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_keywords_name", columnNames = "name"),
                @UniqueConstraint(name = "uk_keywords_tmdb_id", columnNames = "tmdb_id")
        }
)
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class Keyword {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                     // PK

    @Column(name = "tmdb_id")           // TMDb keyword id (nullable 허용)
    private Long tmdbId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }

    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }
}
