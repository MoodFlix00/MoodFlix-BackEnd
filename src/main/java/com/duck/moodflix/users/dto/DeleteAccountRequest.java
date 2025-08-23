package com.duck.moodflix.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "회원 탈퇴 요청 DTO")
public class DeleteAccountRequest {

    @Schema(description = "계정 확인을 위한 현재 비밀번호", example = "password123")
    private String password;
}