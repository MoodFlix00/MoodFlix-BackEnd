//package com.duck.moodflix.movie.controller;
//
//import com.duck.moodflix.movie.domain.entity.Movie;
//import com.duck.moodflix.movie.dto.response.MovieDetailResponse;
//import com.duck.moodflix.movie.dto.response.MovieDetailView;
//import com.duck.moodflix.movie.dto.response.MovieSummaryResponse;
//import com.duck.moodflix.movie.service.MovieService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Tag(name = "Movie API", description = "영화 정보 관리 API")
//@RestController
//@RequestMapping("/api/movies")
//@RequiredArgsConstructor
//public class MovieController {
//
//    private final MovieService movieService;
//
//    @Operation(
//            summary = "TMDb 영화 정보 동기화",
//            description = "TMDb에서 인기 영화를 가져와 DB에 저장합니다. (관리자용 권한 필요)"
//    )
//    @ApiResponse(responseCode = "200", description = "동기화 시작됨")
//    @SecurityRequirement(name = "Bearer Authentication") // ← OpenAPI 보안 스키마 이름과 일치시킬 것
//    @PostMapping("/sync")
//    public ResponseEntity<String> syncMovies() {
//        movieService.syncMoviesFromTMDb();
//        return ResponseEntity.ok("Movie data synchronization has been initiated.");
//    }
//
//    @Operation(
//            summary = "전체 영화 목록(요약) 조회",
//            description = "DB에 저장된 영화의 요약 정보를 반환합니다."
//    )
//    @ApiResponse(responseCode = "200", description = "성공")
//    @SecurityRequirement(name = "Bearer Authentication")
//    @GetMapping
//    public ResponseEntity<List<MovieSummaryResponse>> getAllMovies() {
//        var list = movieService.findAllMovies().stream()
//                .map(m -> new MovieSummaryResponse(
//                        m.getId(), m.getTmdbId(), m.getTitle(),
//                        m.getPosterUrl(), m.getGenre(), m.getReleaseDate(), m.getVoteAverage()
//                ))
//                .toList();
//        return ResponseEntity.ok(list);
//    }
//
//    @Operation(
//            summary = "영화 상세 조회",
//            description = "ID로 영화를 조회하고, TMDb 세부 정보(등급/미디어/크레딧/리뷰 등)를 합쳐 반환합니다."
//    )
//    @ApiResponse(responseCode = "200", description = "성공")
//    @ApiResponse(responseCode = "404", description = "영화 없음", content = @Content)
//    @SecurityRequirement(name = "Bearer Authentication")
//    @GetMapping("/{id}")
//    public ResponseEntity<MovieDetailResponse> getMovie(@PathVariable Long id) {
//        return ResponseEntity.ok(movieService.getMovieDetailResponse(id)); // ← 서비스에서 DTO 조립
//    }
//}
