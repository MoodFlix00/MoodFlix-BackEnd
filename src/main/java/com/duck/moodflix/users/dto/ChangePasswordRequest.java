package com.duck.moodflix.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank
    private String currentPassword;

    @NotBlank
    @Size(min = 6, message = "새 비밀번호는 6자리 이상이어야 합니다.")
    private String newPassword;
}