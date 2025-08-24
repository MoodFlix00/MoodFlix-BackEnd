package com.duck.moodflix.users.service;

import com.duck.moodflix.users.domain.entity.User;
import com.duck.moodflix.users.domain.entity.enums.UserStatus;
import com.duck.moodflix.users.dto.ChangePasswordRequest;
import com.duck.moodflix.users.dto.ProfileEditResponse;
import com.duck.moodflix.users.dto.UpdateUserProfileRequest;
import com.duck.moodflix.users.dto.UserProfileResponse;
import com.duck.moodflix.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션 설정
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 프로필 응답 DTO 변환
    public UserProfileResponse getProfile(Long userId) {
        User user = findUserById(userId);
        return UserProfileResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .birthDate(user.getBirthDate() != null ? user.getBirthDate().toString() : null)
                .gender(user.getGender())
                .profileImage(user.getProfileImage())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // 회원정보 수정
    @Transactional // (readOnly=false) - 데이터 변경이 있으므로 클래스 레벨의 설정을 덮어씁니다.
    public ProfileEditResponse updateProfile(Long userId, UpdateUserProfileRequest dto) {
        // 1. DB에서 사용자 정보를 조회합니다.
        User user = findUserById(userId);
        LocalDate birthDate = (dto.getBirthDate() != null && !dto.getBirthDate().isBlank()) ? LocalDate.parse(dto.getBirthDate()) : null;

        // 2. 엔티티의 상태를 변경합니다. (Setter 대신 비즈니스 메서드 사용)
        user.updateProfile(dto.getName(), birthDate, dto.getGender(), dto.getProfileImage());

        // 3. 메서드가 종료될 때 @Transactional에 의해 변경된 내용(Dirty Checking)이
        //    DB에 자동으로 UPDATE 쿼리로 반영됩니다. 따라서 save()가 필요 없습니다.
        return ProfileEditResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest dto) {
        // 1. DB에서 사용자 정보를 조회합니다.
        User user = findUserById(userId);

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 2. 엔티티의 비밀번호를 변경합니다.
        user.changePassword(passwordEncoder.encode(dto.getNewPassword()));

        // 3. 메서드 종료 시 변경된 비밀번호가 DB에 자동으로 UPDATE 됩니다.
    }

    @Transactional
    public void deleteAccount(Long userId, String password) {
        User user = findUserById(userId);

        // [수정] 이미 탈퇴된 계정 처리 (멱등성 보장)
        if (user.getStatus() == UserStatus.DELETED) {
            return; // 또는 예외를 발생시켜 클라이언트에게 알릴 수 있습니다.
        }

        // [수정] 소셜 로그인 계정(비밀번호 없음) 처리
        if (user.getPassword() == null) {
            // 소셜 계정은 비밀번호로 탈퇴할 수 없음을 명확히 알림
            throw new IllegalStateException("소셜 로그인 계정은 비밀번호로 탈퇴할 수 없습니다.");
        }

        // 자체 로그인 사용자의 경우, 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 올바르지 않습니다.");
        }

        user.deleteAccount();
    }


    // 유저 조회 로직 (중복 제거)
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    // 커스텀 예외
    public static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException(String msg) { super(msg); }
    }
}