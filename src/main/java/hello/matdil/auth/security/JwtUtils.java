package hello.matdil.auth.security;

import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;

public class JwtUtils {

    private JwtUtils() {}

    public static String extractBearerToken(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new UserException(UserErrorCode.INVALID_ACCESS_TOKEN);
        }
        return header.substring(7);
    }
}

