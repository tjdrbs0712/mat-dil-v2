package hello.matdil.domain.user.factory;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.user.dto.UserRegisterRequestDto;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public User create(UserRegisterRequestDto dto) {
        return User.builder()
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .name(dto.name())
                .phoneNumber(dto.phoneNumber())
                .address(new Address(dto.city(), dto.street(), dto.detailAddress()))
                .role(UserRole.USER)
                .build();
    }
}
