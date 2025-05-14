package hello.matdil.auth.service;

import hello.matdil.auth.dto.RefreshTokenResponseDto;
import hello.matdil.auth.security.JwtUtils;
import hello.matdil.domain.user.dto.UserLoginResponseDto;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;
import hello.matdil.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    /**
     * 로그인
     */
    @Transactional
    public UserLoginResponseDto login(String email, String password) {
        User user = authenticate(email, password);

        // 회원 상태에 따라
        user.validateLoginPossible();

        // 토큰 발급은 TokenService에게 위임
        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return UserLoginResponseDto.of(accessToken, refreshToken);
    }

    /**
     * 리프레시 토큰으로 액세스 토큰 재발급
     */
    @Transactional
    public RefreshTokenResponseDto refreshAccessToken(String refreshToken) {
        String newAccessToken = tokenService.refreshAccessToken(refreshToken);
        return new RefreshTokenResponseDto(newAccessToken);
    }

    /**
     * 로그아웃 (리프레시 토큰 삭제)
     */
    @Transactional
    public void logout(String bearerToken) {
        String token = JwtUtils.extractBearerToken(bearerToken); // "Bearer ~" 제거
        tokenService.deleteRefreshToken(token);
    }

    /**
     * 이메일,비밀번호 인증
     */
    private User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.AUTHENTICATION_FAILED));

        if (!user.isPasswordMatch(password, passwordEncoder)) {
            throw new UserException(UserErrorCode.AUTHENTICATION_FAILED);
        }

        return user;
    }
}
