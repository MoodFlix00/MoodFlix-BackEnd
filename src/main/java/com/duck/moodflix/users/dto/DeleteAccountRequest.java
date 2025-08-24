package com.duck.moodflix.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // Jackson 역직렬화를 위해 추가
@NoArgsConstructor // Jackson 역직렬화를 위해 추가
@Schema(description = "회원 탈퇴 요청 DTO")
public class DeleteAccountRequest {

    // [수정] @NotBlank 제거. 소셜 계정은 비밀번호가 없을 수 있으므로.
    @Schema(description = "계정 확인을 위한 현재 비밀번호 (자체 로그인 사용자만 해당)", example = "password123")
    private String password;
}
