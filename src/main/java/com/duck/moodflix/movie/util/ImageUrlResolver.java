package com.duck.moodflix.movie.util;

import org.springframework.stereotype.Component;

@Component
public class ImageUrlResolver {
    public static final String ROOT = "https://image.tmdb.org/t/p";

    public String w185(String path){ return path==null || path.isBlank()? null : ROOT + "/w185" + path; }
    public String w342(String path){ return path==null || path.isBlank()? null : ROOT + "/w342" + path; }
    public String w500(String path){ return path==null || path.isBlank()? null : ROOT + "/w500" + path; }
    public String w780(String path){ return path==null || path.isBlank()? null : ROOT + "/w780" + path; }

    /** TMDb avatarPath('/abc.jpg') vs gravatar('https://...') 모두 대응 */
    public String avatar(String avatarPath) {
        if (avatarPath == null || avatarPath.isBlank()) return null;
        return avatarPath.startsWith("/") ? w185(avatarPath) : avatarPath;
    }
}