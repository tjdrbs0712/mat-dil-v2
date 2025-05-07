package hello.matdil.domain.user.dto;

import hello.matdil.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRegisterResponseDto {
    private final String email;
    private final String name;

    public static UserRegisterResponseDto from(User user) {

        return UserRegisterResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}