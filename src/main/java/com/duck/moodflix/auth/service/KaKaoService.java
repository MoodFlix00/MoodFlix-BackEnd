package com.duck.moodflix.auth.service;

import com.duck.moodflix.auth.dto.KakaoDto;
import com.duck.moodflix.auth.dto.KakaoTokenResponseDto;
import com.duck.moodflix.auth.util.JwtTokenProvider; // JWT 프로바이더 import
import com.duck.moodflix.auth.util.KakaoUtil;
import com.duck.moodflix.users.domain.entity.User;
import com.duck.moodflix.users.domain.entity.enums.Role;
import com.duck.moodflix.users.domain.entity.enums.UserStatus;
import com.duck.moodflix.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KaKaoService {

    private final KakaoUtil kakaoUtil;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public String oAuthLogin(String accessCode) {
        KakaoTokenResponseDto oAuthToken = kakaoUtil.requestToken(accessCode);
        KakaoDto.KakaoProfile profile = kakaoUtil.requestProfile(oAuthToken);

        KakaoDto.KakaoProfile.KakaoAccount kakaoAccount = profile.getKakao_account();
        if (kakaoAccount == null || kakaoAccount.getEmail() == null) {
            throw new IllegalArgumentException("카카오 계정에서 이메일 정보를 가져올 수 없습니다.");
        }
        String email = kakaoAccount.getEmail();
        String provider = "kakao";

        User user;
        try {
            // [수정] provider까지 조건에 추가하여 조회
            user = userRepository.findByEmailAndProviderAndStatus(email, provider, UserStatus.ACTIVE)
                    .orElseGet(() -> {
                        String nickname = "카카오유저";
                        if (kakaoAccount.getProfile() != null && kakaoAccount.getProfile().getNickname() != null) {
                            nickname = kakaoAccount.getProfile().getNickname();
                        }
                        return userRepository.save(
                                User.builder()
                                        .email(email)
                                        .name(nickname)
                                        .provider(provider)
                                        .role(Role.USER)
                                        .status(UserStatus.ACTIVE)
                                        .build()
                        );
                    });
        } catch (DataIntegrityViolationException e) {
            // [수정] 동시성 문제로 유니크 제약 조건 위반 시, 이미 저장된 사용자를 다시 조회
            log.warn("Race condition occurred during user creation for email: {}", email);
            user = userRepository.findByEmailAndProviderAndStatus(email, provider, UserStatus.ACTIVE)
                    .orElseThrow(() -> new RuntimeException("Failed to retrieve user after race condition.", e));
        }

        // [수정] 사용자의 단일 role을 토큰 생성 메소드에 전달
        return jwtTokenProvider.generateToken(user.getUserId(), user.getRole());
    }
}