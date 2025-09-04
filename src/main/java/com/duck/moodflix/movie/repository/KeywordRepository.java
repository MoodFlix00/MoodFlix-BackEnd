package com.duck.moodflix.movie.repository;

import com.duck.moodflix.movie.domain.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByNameIgnoreCase(String name);
    Optional<Keyword> findByTmdbId(Long tmdbId);
    List<Keyword> findByNameIn(Iterable<String> names);
}
