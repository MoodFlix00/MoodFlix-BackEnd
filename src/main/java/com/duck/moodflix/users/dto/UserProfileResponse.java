package com.duck.moodflix.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "사용자 프로필 조회 응답 DTO")
public class UserProfileResponse {

    @Schema(description = "사용자 고유 ID", example = "1")
    private Long userId;

    @Schema(description = "이메일 주소", example = "user@example.com")
    private String email;

    @Schema(description = "사용자 이름", example = "김무드")
    private String name;

    @Schema(description = "생년월일", example = "2000-01-01")
    private String birthDate;

    @Schema(description = "성별", example = "M")
    private String gender;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private String profileImage;

    @Schema(description = "계정 생성일")
    private LocalDateTime createdAt;

    @Schema(description = "마지막 수정일")
    private LocalDateTime updatedAt;
}