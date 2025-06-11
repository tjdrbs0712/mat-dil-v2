package hello.matdil.domain.user.factory;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.address.AddressFactory;
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
    private final AddressFactory addressFactory;

    public User createUser(UserRegisterRequestDto dto) {

        Address address = addressFactory.create(dto.getCity(), dto.getStreet(), dto.getDetailAddress());

        return User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .address(address)
                .role(UserRole.USER)
                .build();
    }

    public Address update(String city, String street, String detailAddress) {
        return addressFactory.create(city, street, detailAddress);
    }
}
