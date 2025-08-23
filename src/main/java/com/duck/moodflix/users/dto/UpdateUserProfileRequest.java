package com.duck.moodflix.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "사용자 프로필 수정 요청 DTO")
public class UpdateUserProfileRequest {

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Schema(description = "새로운 사용자 이름", example = "김무드")
    private String name;

    @Schema(description = "성별", example = "M")
    private String gender;

    @Schema(description = "생년월일", example = "2000-01-01")
    private String birthDate;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private String profileImage;
}