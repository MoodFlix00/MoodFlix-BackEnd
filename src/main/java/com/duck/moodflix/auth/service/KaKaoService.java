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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // [수정] 카카오 계정 및 이메일 Null 체크
        KakaoDto.KakaoProfile.KakaoAccount kakaoAccount = profile.getKakao_account();
        if (kakaoAccount == null || kakaoAccount.getEmail() == null) {
            throw new IllegalArgumentException("카카오 계정에서 이메일 정보를 가져올 수 없습니다. 정보 제공에 동의했는지 확인해주세요.");
        }
        String email = kakaoAccount.getEmail();

        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
                .orElseGet(() -> {
                    // [수정] 카카오 프로필에서 닉네임 안전하게 가져오기
                    String nickname = "카카오유저"; // 기본값
                    if (kakaoAccount.getProfile() != null && kakaoAccount.getProfile().getNickname() != null) {
                        nickname = kakaoAccount.getProfile().getNickname();
                    }

                    // [수정] 신규 사용자 생성 시 role과 status 명시적으로 설정
                    return userRepository.save(
                            User.builder()
                                    .email(email)
                                    .name(nickname)
                                    .provider("kakao")
                                    .role(Role.USER) // 기본 역할 부여
                                    .status(UserStatus.ACTIVE) // 활성 상태로 생성
                                    .build()
                    );
                });

        // [수정] 사용자의 단일 role을 토큰 생성 메소드에 전달
        return jwtTokenProvider.generateToken(user.getUserId(), user.getRole());
    }
}