package hello.matdil.auth.service;

import hello.matdil.auth.entity.RefreshToken;
import hello.matdil.auth.repository.RefreshTokenRepository;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenManager {

    private final RefreshTokenRepository refreshTokenRepository;

    public void save(Long userId, String refreshToken) {
        refreshTokenRepository.save(new RefreshToken(userId, refreshToken));
    }

    public void validate(Long userId, String token) {
        RefreshToken saved = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (!saved.getToken().equals(token)) {
            throw new UserException(UserErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public void delete(Long userId) {
        refreshTokenRepository.deleteById(userId);
    }
}
