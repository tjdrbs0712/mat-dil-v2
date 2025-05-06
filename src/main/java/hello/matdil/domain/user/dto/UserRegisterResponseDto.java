package hello.matdil.domain.user.dto;

import hello.matdil.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserRegisterResponseDto {
    private final String email;
    private final String name;

    private UserRegisterResponseDto(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public static UserRegisterResponseDto from(User user) {
        return new UserRegisterResponseDto(user.getEmail(), user.getName());
    }
}