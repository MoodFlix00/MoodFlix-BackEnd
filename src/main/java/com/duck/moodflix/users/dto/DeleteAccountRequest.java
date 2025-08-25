package com.duck.moodflix.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // Jackson 역직렬화를 위해 추가
@NoArgsConstructor // Jackson 역직렬화를 위해 추가
@Schema(description = "회원 탈퇴 요청 DTO")
public class DeleteAccountRequest {

    // [수정] Schema 및 JsonProperty 어노테이션 추가
    @Schema(
            description = "계정 확인을 위한 현재 비밀번호 (자체 로그인 사용자만 해당)",
            example = "password123",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED // Swagger 문서에 선택값으로 표시
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // 요청 시에만 사용하고, 응답에는 포함하지 않음
    private String password;
}
