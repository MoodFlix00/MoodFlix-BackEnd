package com.duck.moodflix.users.service;

import com.duck.moodflix.users.domain.entity.User;
import com.duck.moodflix.users.dto.ChangePasswordRequest;
import com.duck.moodflix.users.dto.ProfileEditResult;
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
    public ProfileEditResult updateProfile(Long userId, UpdateUserProfileRequest dto) {
        // 1. DB에서 사용자 정보를 조회합니다. 'user'는 영속성 컨텍스트가 관리하는 엔티티가 됩니다.
        User user = findUserById(userId);
        LocalDate birthDate = (dto.getBirthDate() != null && !dto.getBirthDate().isBlank()) ? LocalDate.parse(dto.getBirthDate()) : null;

        // 2. 엔티티의 상태를 변경합니다. (Setter 대신 비즈니스 메서드 사용)
        user.updateProfile(dto.getName(), birthDate, dto.getGender(), dto.getProfileImage());

        // 3. 메서드가 종료될 때 @Transactional에 의해 변경된 내용(Dirty Checking)이
        //    DB에 자동으로 UPDATE 쿼리로 반영됩니다. 따라서 save()가 필요 없습니다.
        return ProfileEditResult.builder()
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

    // [변경] 회원탈퇴 -> 계정 삭제
    @Transactional
    public void deleteAccount(Long userId, String password) {
        User user = findUserById(userId);

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