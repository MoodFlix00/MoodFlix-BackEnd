package com.duck.moodflix.auth.service;

import com.duck.moodflix.auth.dto.KakaoDto;
import com.duck.moodflix.auth.dto.KakaoTokenResponseDto;
import com.duck.moodflix.auth.util.JwtTokenProvider; // JWT 프로바이더 import
import com.duck.moodflix.auth.util.KakaoUtil;
import com.duck.moodflix.users.domain.entity.User;
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
        String email = profile.getKakao_account().getEmail();

        // findByEmail 결과가 없으면 User.builder()가 호출되며,
        // User 생성자에서 role이 자동으로 Role.USER로 설정됩니다.
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .name(profile.getKakao_account().getProfile().getNickname())
                                .provider("kakao")
                                .build()
                ));

        // [수정] 사용자의 단일 role을 토큰 생성 메소드에 전달
        return jwtTokenProvider.generateToken(user.getUserId(), user.getRole());
    }
}