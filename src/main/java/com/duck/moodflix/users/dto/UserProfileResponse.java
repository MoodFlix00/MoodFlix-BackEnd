package com.duck.moodflix.users.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserProfileResponse {
    private Long userId;
    private String email;
    private String name;
    private String birthDate;
    private String gender;
    private String profileImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}