package com.duck.moodflix.movie.controller;
// 응답용 DTO

import com.duck.moodflix.movie.domain.entity.Movie;
import com.duck.moodflix.movie.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Movie API", description = "영화 정보 관리 API")
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @Operation(summary = "TMDb 영화 정보 동기화", description = "TMDb에서 인기 영화 정보를 가져와 데이터베이스에 저장합니다. (관리자용 권한 필요)")
    @SecurityRequirement(name = "Bearer Authentication") // 이 API는 인증이 필요함을 명시
    @PostMapping("/sync")
    public ResponseEntity<String> syncMovies() {
        // 참고: 실제 서비스에서는 이 API에 @PreAuthorize("hasRole('ADMIN')") 등으로 관리자만 호출하도록 권한을 제어해야 합니다.
        movieService.syncMoviesFromTMDb();
        return ResponseEntity.ok("Movie data synchronization has been initiated.");
    }

    @Operation(summary = "전체 영화 목록 조회", description = "데이터베이스에 저장된 모든 영화 목록을 조회합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public ResponseEntity<List<MovieResponseDto>> getAllMovies() {
        List<MovieResponseDto> movies = movieService.findAllMovies().stream()
                .map(MovieResponseDto::new) // Movie 엔티티를 MovieResponseDto로 변환
                .collect(Collectors.toList());
        return ResponseEntity.ok(movies);
    }

    @Operation(summary = "특정 영화 상세 조회", description = "ID로 특정 영화의 상세 정보를 조회합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDto> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.findMovieById(id);
        return ResponseEntity.ok(new MovieResponseDto(movie)); // Movie 엔티티를 MovieResponseDto로 변환
    }
}