package com.duck.moodflix.users.controller;

import com.duck.moodflix.users.domain.entity.User;
import com.duck.moodflix.users.dto.*;
import com.duck.moodflix.users.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 1. 회원정보 조회
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "message", "사용자 정보를 찾을 수 없습니다.",
                    "error", Map.of("code", "NOT_FOUND")
            ));
        }
        // 여기는 getProfile(userId)를 호출하도록 변경하는 것이 더 좋습니다.
        return ResponseEntity.ok(Map.of(
                "data", userService.getProfile(user.getUserId())
        ));
    }

    // 2. 회원정보 수정
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestBody UpdateUserProfileRequest dto,
            HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "message", "사용자 정보를 찾을 수 없습니다.",
                    "error", Map.of("code", "NOT_FOUND")
            ));
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "이름은 필수입니다.",
                    "error", Map.of(
                            "code", "VALIDATION_ERROR",
                            "fields", List.of(Map.of("field", "name", "reason", "이름이 비어 있음"))
                    )
            ));
        }

        // [수정된 부분] user 객체 대신 user.getUserId()를 전달합니다.
        ProfileEditResult updatedResult = userService.updateProfile(user.getUserId(), dto);

        return ResponseEntity.ok(Map.of(
                "message", "프로필이 수정되었습니다.",
                "data", updatedResult
        ));
    }

    // 3. 비밀번호 변경
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest dto,
            HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            // 이 부분은 인증/인가 로직에서 처리하는 것이 더 일반적입니다.
            return ResponseEntity.status(401).body(Map.of("message", "로그인이 필요합니다."));
        }
        try {
            // [수정된 부분] user 객체 대신 user.getUserId()를 전달합니다.
            userService.changePassword(user.getUserId(), dto);
            return ResponseEntity.ok(Map.of(
                    "message", "비밀번호가 변경되었습니다."
            ));
        } catch (UserService.InvalidPasswordException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage(),
                    "error", Map.of("code", "INVALID_PASSWORD")
            ));
        }
    }

    // 4. 계정 삭제 (비밀번호 입력)
    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteAccount( // [변경] 메서드 이름을 deleteAccount로 수정
                                            @RequestBody DeleteAccountRequest dto,
                                            HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "로그인이 필요합니다."));
        }
        try {
            // [변경] userService의 deleteAccount 메서드를 호출
            userService.deleteAccount(user.getUserId(), dto.getPassword());
            session.invalidate(); // 계정 삭제 후 세션을 무효화합니다.
            return ResponseEntity.ok(Map.of(
                    "message", "계정 삭제가 완료되었습니다." // [변경] 메시지 내용 수정
            ));
        } catch (UserService.InvalidPasswordException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "비밀번호가 올바르지 않습니다.",
                    "error", Map.of("code", "INVALID_PASSWORD")
            ));
        }
    }
}