package hello.matdil.auth.service;

import hello.matdil.auth.entity.RefreshToken;
import hello.matdil.auth.repository.RefreshTokenRepository;
import hello.matdil.auth.security.JwtTokenProvider;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;
import hello.matdil.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    //토큰 발급
    public String generateAccessToken(User user) {
        return jwtTokenProvider.generateToken(user.getId(), user.getRole());
    }

    //리프레시 토큰 발급
    public String generateRefreshToken(User user) {
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());
        refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));
        return refreshToken;
    }

    // 리프레시 토큰으로 새 토큰 발급
    public String refreshAccessToken(String refreshToken) {
        Long userId = validateAndExtractUserId(refreshToken);

        RefreshToken saved = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (!saved.getToken().equals(refreshToken)) {
            throw new UserException(UserErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        return generateAccessToken(user);
    }

    //리프레시 토큰 삭제
    public void deleteRefreshToken(String accessToken) {
        Long userId = validateAndExtractUserId(accessToken);
        refreshTokenRepository.deleteById(userId);
    }

    private Long validateAndExtractUserId(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new UserException(UserErrorCode.INVALID_REFRESH_TOKEN);
        }
        return jwtTokenProvider.getUserId(token);
    }
}
