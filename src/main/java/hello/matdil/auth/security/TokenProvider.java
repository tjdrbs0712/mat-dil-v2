package hello.matdil.auth.security;

import hello.matdil.domain.user.entity.UserRole;

public interface TokenProvider {
    String generateToken(Long userId, UserRole role);
    String generateRefreshToken(Long userId);
    Long getUserId(String token);
    String getRole(String token);
    boolean validateToken(String token);
}
