package hello.matdil.domain.user.dto;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.entity.UserRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfoResponseDto {
    private final String email;
    private final String name;
    private final String phoneNumber;
    private final Address address;
    private final UserRole role;
    private final LocalDateTime createAt;

    public static UserInfoResponseDto from(User user){
        return UserInfoResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .role(user.getRole())
                .createAt(user.getCreatedAt())
                .build();
    }
}
