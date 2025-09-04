package com.duck.moodflix.movie.service;

import com.duck.moodflix.movie.domain.entity.Keyword;
import com.duck.moodflix.movie.domain.entity.Movie;
import com.duck.moodflix.movie.domain.entity.MovieKeyword;
import com.duck.moodflix.movie.repository.KeywordRepository;
import com.duck.moodflix.movie.repository.MovieKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class KeywordManager {

    private final KeywordRepository keywordRepository;
    private final MovieKeywordRepository movieKeywordRepository;

    @Transactional
    public void upsert(Movie movie, List<String> names) {
        if (names == null || names.isEmpty()) return;

        Map<String, Keyword> map = new HashMap<>();
        keywordRepository.findByNameIn(names)
                .forEach(k -> map.put(k.getName().toLowerCase(), k));

        for (String raw : names) {
            String key = raw.toLowerCase();
            Keyword k = map.get(key);
            if (k == null) {
                k = keywordRepository.save(Keyword.builder().name(raw).build());
                map.put(key, k);
            }
            if (!movieKeywordRepository.existsByMovieIdAndKeywordId(movie.getId(), k.getId())) {
                movieKeywordRepository.save(MovieKeyword.of(movie, k));
            }
        }
    }
}
