package hello.matdil.auth.model;

import hello.matdil.domain.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "authenticated")
public class AuthUser {
    private final Long userId;
    private final String strRole;
    private final UserRole role;

    public static AuthUser anonymous() {
        return new AuthUser(null, "user", UserRole.USER);
    }

    public boolean isAnonymous() {
        return userId == null;
    }
}