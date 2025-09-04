package com.duck.moodflix.movie.util;

import com.duck.moodflix.movie.dto.tmdb.TMDbMovieDetailDto;
import com.duck.moodflix.movie.dto.tmdb.releases.ReleaseDateItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
@Slf4j
public class CertificationExtractor {

    public String extract(TMDbMovieDetailDto d) {
        String c = extractFor(d, "KR");
        if (c == null) c = extractFor(d, "US");
        if (c == null && d.getReleaseDates()!=null && d.getReleaseDates().getResults()!=null) {
            return d.getReleaseDates().getResults().stream()
                    .flatMap(r -> r.getReleaseDates().stream())
                    .map(ReleaseDateItemDto::getCertification)
                    .filter(s -> s!=null && !s.isBlank())
                    .findFirst().orElse(null);
        }
        return c;
    }

    private String extractFor(TMDbMovieDetailDto d, String iso2) {
        if (d.getReleaseDates()==null || d.getReleaseDates().getResults()==null) return null;
        return d.getReleaseDates().getResults().stream()
                .filter(r -> iso2.equalsIgnoreCase(r.getIso31661()))
                .findFirst()
                .flatMap(r -> r.getReleaseDates().stream()
                        .sorted(Comparator.comparingInt(x -> x.getType()==null?99:x.getType()))
                        .map(ReleaseDateItemDto::getCertification)
                        .filter(c -> c!=null && !c.isBlank())
                        .findFirst())
                .orElse(null);
    }
}
