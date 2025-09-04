package com.duck.moodflix.movie.service;

import com.duck.moodflix.movie.client.TMDbClient;
import com.duck.moodflix.movie.domain.entity.Movie;
import com.duck.moodflix.movie.domain.entity.TmdbReview;
import com.duck.moodflix.movie.dto.tmdb.reviews.ReviewDto;
import com.duck.moodflix.movie.dto.tmdb.reviews.ReviewsPageDto;
import com.duck.moodflix.movie.repository.TmdbReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewSyncService {

    private final TMDbClient tmdb;
    private final TmdbReviewRepository reviewRepo;

    /** ko-KR → en-US 순서로 1페이지 리뷰 업서트 */
    @Transactional
    public int syncForMovie(Movie movie) {
        int upserted = 0;
        upserted += upsertAll(movie, tmdb.getReviews(movie.getTmdbId(), "ko-KR", 1));
        upserted += upsertAll(movie, tmdb.getReviews(movie.getTmdbId(), "en-US", 1));
        log.info("Synced TMDb reviews for movieId={}, upserted={}", movie.getId(), upserted);
        return upserted;
    }

    private int upsertAll(Movie movie, ReviewsPageDto page) {
        if (page == null || page.results() == null || page.results().isEmpty()) return 0;
        int cnt = 0;
        for (ReviewDto r : page.results()) {
            cnt += upsert(movie, r);
        }
        return cnt;
    }

    /** ReviewDto는 record: r.id(), r.author(), r.content(), r.url(), r.createdAt(), r.authorDetails().rating() */
    private int upsert(Movie movie, ReviewDto r) {
        if (r == null || r.id() == null) return 0;

        var existing = reviewRepo.findById(r.id()).orElse(null);
        if (existing == null) {
            reviewRepo.save(TmdbReview.of(r.id(), r.author(), r.content(), r.authorDetails().rating(), r.url(), r.createdAt(), movie));
            return 1;
        }

        boolean changed = false;
        if (!Objects.equals(existing.getAuthor(), r.author())) { existing.setAuthor(r.author()); changed = true; }
        if (!Objects.equals(existing.getContent(), r.content())) { existing.setContent(r.content()); changed = true; }
        if (existing.getMovie() == null || !Objects.equals(existing.getMovie().getId(), movie.getId())) {
            existing.setMovie(movie); changed = true;
        }
        if (changed) reviewRepo.save(existing);
        return changed ? 1 : 0;
    }
}
