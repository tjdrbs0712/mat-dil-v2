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

    //로그인
    @Transactional
    public UserLoginResponseDto login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.AUTHENTICATION_FAILED));

        if (!user.isPasswordMatch(password, passwordEncoder)) {
            throw new UserException(UserErrorCode.AUTHENTICATION_FAILED);
        }

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return new UserLoginResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public RefreshTokenResponseDto refreshAccessToken(String refreshToken) {
        String newAccessToken = tokenService.refreshAccessToken(refreshToken);
        return new RefreshTokenResponseDto(newAccessToken);
    }

    //로그아웃
    @Transactional
    public void logout(String bearerToken) {
        String token = JwtUtils.extractBearerToken(bearerToken);
        tokenService.deleteRefreshToken(token);
    }
}

