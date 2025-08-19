package com.duck.moodflix.users.controller;

import com.duck.moodflix.users.dto.*;
import com.duck.moodflix.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. 회원정보 조회
    @GetMapping("/profile")
    // [수정] HttpSession 대신 @AuthenticationPrincipal 사용
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        // userDetails.getUsername()에는 JwtAuthenticationFilter에서 넣어준 사용자 ID(문자열)가 들어있습니다.
        Long userId = Long.parseLong(userDetails.getUsername());
        UserProfileResponse profile = userService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }

    // 2. 회원정보 수정
    @PutMapping("/profile")
    // [수정] @Validated로 유효성 검사 자동화, HttpSession 제거
    public ResponseEntity<ProfileEditResult> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Validated @RequestBody UpdateUserProfileRequest dto) {
        Long userId = Long.parseLong(userDetails.getUsername());
        ProfileEditResult updatedResult = userService.updateProfile(userId, dto);
        return ResponseEntity.ok(updatedResult);
    }

    // 3. 비밀번호 변경
    @PutMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Validated @RequestBody ChangePasswordRequest dto) {
        Long userId = Long.parseLong(userDetails.getUsername());
        userService.changePassword(userId, dto);
        return ResponseEntity.ok(Map.of("message", "비밀번호가 변경되었습니다."));
    }

    // 4. 계정 삭제
    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @Validated @RequestBody DeleteAccountRequest dto) {
        Long userId = Long.parseLong(userDetails.getUsername());
        userService.deleteAccount(userId, dto.getPassword());
        // JWT 방식에서는 서버에서 할 일은 없으며, 클라이언트가 토큰을 삭제해야 합니다.
        return ResponseEntity.ok(Map.of("message", "계정 삭제가 완료되었습니다."));
    }
}