package com.duck.moodflix.users.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tmdbId;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @Column(length = 255)
    private String posterUrl;

    private LocalDate releaseDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
