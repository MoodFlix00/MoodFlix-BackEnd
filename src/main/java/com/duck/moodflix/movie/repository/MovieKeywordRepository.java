package com.duck.moodflix.movie.repository;

import com.duck.moodflix.movie.domain.entity.MovieKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieKeywordRepository extends JpaRepository<MovieKeyword, Long> {
    List<MovieKeyword> findByMovieId(Long movieId);
    boolean existsByMovieIdAndKeywordId(Long movieId, Long keywordId);
}
