package com.duck.moodflix.users.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProfileEditResult {
    private Long userId;
    private String name;
    private String profileImage;
    private LocalDateTime updatedAt;
}