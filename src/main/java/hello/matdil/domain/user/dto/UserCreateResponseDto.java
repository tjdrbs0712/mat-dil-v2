package hello.matdil.domain.user.dto;

import hello.matdil.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserCreateResponseDto {
    private final String email;
    private final String name;

    private UserCreateResponseDto(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public static UserCreateResponseDto from(User user) {
        return new UserCreateResponseDto(user.getEmail(), user.getName());
    }
}