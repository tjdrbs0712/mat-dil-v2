package hello.matdil.domain.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginResponseDto {

    private final String accessToken;
    private final String refreshToken;

    public static UserLoginResponseDto of(String accessToken, String refreshToken) {
        return new UserLoginResponseDto(accessToken, refreshToken);
    }
}