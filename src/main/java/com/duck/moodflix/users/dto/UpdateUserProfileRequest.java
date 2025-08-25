package com.duck.moodflix.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

@Getter
@Schema(description = "사용자 프로필 수정 요청 DTO")
public class UpdateUserProfileRequest {

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Size(max = 50, message = "이름은 50자를 초과할 수 없습니다.") // [수정] 최대 길이 검증
    @Schema(description = "새로운 사용자 이름", example = "김무드")
    private String name;

    @Pattern(regexp = "^(M|F|N)$", message = "성별은 M, F, N 중 하나여야 합니다.") // [수정] 형식 검증
    @Schema(description = "성별", example = "M", allowableValues = {"M", "F", "N"})
    private String gender;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일은 YYYY-MM-DD 형식이어야 합니다.") // [수정] 형식 검증
    @Schema(description = "생년월일", example = "2000-01-01")
    private String birthDate;

    @Pattern(regexp = "^https?://.+", message = "프로필 이미지는 http 또는 https URL이어야 합니다.") // [수정] 형식 검증
    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private String profileImage;
}