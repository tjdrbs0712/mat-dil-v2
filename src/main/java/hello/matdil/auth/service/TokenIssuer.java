package hello.matdil.auth.service;

import hello.matdil.auth.security.JwtTokenProvider;
import hello.matdil.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenIssuer {

    private final JwtTokenProvider jwt;

    public String issueAccessToken(User user) {
        return jwt.generateToken(user.getId(), user.getEmail(), user.getRole());
    }

    public String issueRefreshToken(User user) {
        return jwt.generateRefreshToken(user.getId());
    }
}
