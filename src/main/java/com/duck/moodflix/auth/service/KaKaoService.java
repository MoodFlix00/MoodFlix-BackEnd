package com.duck.moodflix.auth.service;

import com.duck.moodflix.auth.dto.KakaoDto;
import com.duck.moodflix.auth.util.KakaoUtil;
import com.duck.moodflix.users.domain.entity.User;
import com.duck.moodflix.users.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KaKaoService {

    private final KakaoUtil kakaoUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User oAuthLogin(String accessCode, HttpServletRequest request) {
        // 1. 카카오 인가코드로 access token 발급
        KakaoDto.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);

        // 2. access token으로 사용자 프로필 정보 요청
        KakaoDto.KakaoProfile profile = kakaoUtil.requestProfile(oAuthToken);

        String email = profile.getKakao_account().getEmail();
        String nickname = profile.getKakao_account().getProfile().getNickname();

        // 3. 우리 DB에 해당 email의 유저가 있는지 확인 (없으면 신규가입)
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .name(nickname)
                                .provider("kakao")
                                .build()
                ));

        // 4. 세션에 유저 정보 저장 (세션 로그인)
        HttpSession session = request.getSession(true); // 세션 없으면 새로 생성
        session.setAttribute("loginUser", user);

        return user;
    }
}
