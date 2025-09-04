package com.duck.moodflix.movie.service;

import com.duck.moodflix.movie.client.TMDbClient;
import com.duck.moodflix.movie.domain.entity.Movie;
import com.duck.moodflix.movie.dto.tmdb.TMDbMovieDetailDto;
import com.duck.moodflix.movie.dto.tmdb.TMDbMovieListResponse;
import com.duck.moodflix.movie.dto.tmdb.related.MovieBriefDto;
import com.duck.moodflix.movie.mapper.MovieMapper;
import com.duck.moodflix.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieSyncService {

    private final MovieRepository movieRepository;
    private final TMDbClient tmdb;
    private final MovieMapper movieMapper;
    private final KeywordManager keywordManager;
    private final ReviewSyncService reviewSyncService;

    @Transactional
    public void syncPopularPage1() {
        TMDbMovieListResponse resp = tmdb.getPopular(1);
        List<MovieBriefDto> briefs = (resp == null || resp.getResults() == null)
                ? List.of() : resp.getResults();

        if (briefs.isEmpty()) {
            log.warn("No popular results from TMDb");
            return;
        }

        int saved = 0;
        for (MovieBriefDto brief : briefs) {
            // record면 .id(), Lombok면 .getId() 로 바꾸세요.
            Long tmdbId = brief.id();
            if (tmdbId == null) continue;

            // 성능 최적화: 전체 조회 대신 존재 여부 체크
            if (movieRepository.existsByTmdbId(tmdbId)) continue;

            TMDbMovieDetailDto detail = tmdb.getMovieDetail(tmdbId);
            if (detail == null) {
                log.warn("Skip: TMDb detail is null. tmdbId={}", tmdbId);
                continue;
            }

            Movie movie = movieMapper.toEntity(detail);
            movieRepository.save(movie);

            List<String> keywordNames = extractKeywordNames(detail);
            keywordManager.upsert(movie, keywordNames);

            reviewSyncService.syncForMovie(movie);
            saved++;
            log.info("Saved movie: {} ({}) with {} keywords", movie.getTitle(), movie.getTmdbId(), keywordNames.size());
        }

        log.info("Movie sync completed. saved={}", saved);
    }

    private List<String> extractKeywordNames(TMDbMovieDetailDto dto) {
        if (dto.getKeywords() == null || dto.getKeywords().keywords() == null) return List.of();
        return dto.getKeywords().keywords().stream()
                .filter(Objects::nonNull)
                .map(k -> k.name())
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .toList();
    }
}
