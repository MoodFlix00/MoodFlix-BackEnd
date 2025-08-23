package com.duck.moodflix.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "프로필 수정 결과 응답 DTO")
public class ProfileEditResult {

    @Schema(description = "사용자 고유 ID", example = "1")
    private Long userId;

    @Schema(description = "수정된 사용자 이름", example = "김플릭스")
    private String name;

    @Schema(description = "수정된 프로필 이미지 URL", example = "https://example.com/new_profile.jpg")
    private String profileImage;

    @Schema(description = "수정일")
    private LocalDateTime updatedAt;
}