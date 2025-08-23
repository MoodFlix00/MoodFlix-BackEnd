package com.duck.moodflix.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Schema(description = "비밀번호 변경 요청 DTO")
public class ChangePasswordRequest {

    @NotBlank
    @Schema(description = "현재 비밀번호", example = "currentPassword123")
    private String currentPassword;

    @NotBlank
    @Size(min = 6, message = "새 비밀번호는 6자리 이상이어야 합니다.")
    @Schema(description = "새로운 비밀번호", example = "newPassword456")
    private String newPassword;
}