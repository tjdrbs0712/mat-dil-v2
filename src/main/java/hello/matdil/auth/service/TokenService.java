package hello.matdil.auth.service;

import hello.matdil.auth.security.JwtTokenProvider;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.entity.UserStatus;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;
import hello.matdil.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenIssuer issuer;
    private final RefreshTokenManager refreshTokenManager;
    private final JwtTokenProvider jwt;
    private final UserRepository userRepository;

    public String generateAccessToken(User user) {
        return issuer.issueAccessToken(user);
    }

    public String generateRefreshToken(User user) {
        String token = issuer.issueRefreshToken(user);
        refreshTokenManager.save(user.getId(), token);
        return token;
    }

    public String refreshAccessToken(String refreshToken) {
        Long userId = validate(refreshToken);

        User user = userRepository.findById(userId)
                .filter(u -> u.getUserStatus() == UserStatus.ACTIVE)
                .orElseThrow(() -> new UserException(UserErrorCode.WITHDRAWN_USER));

        refreshTokenManager.validate(userId, refreshToken);

        return generateAccessToken(user);
    }

    public void deleteRefreshToken(String accessToken) {
        Long userId = validate(accessToken);
        refreshTokenManager.delete(userId);
    }

    private Long validate(String token) {
        if (!jwt.validateToken(token)) {
            throw new UserException(UserErrorCode.INVALID_REFRESH_TOKEN);
        }
        return jwt.getUserId(token);
    }
}
