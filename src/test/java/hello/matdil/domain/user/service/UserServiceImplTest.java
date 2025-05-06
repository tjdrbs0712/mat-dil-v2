package hello.matdil.domain.user.service;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.user.dto.UserCreateRequestDto;
import hello.matdil.domain.user.dto.UserCreateResponseDto;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.domain.user.factory.UserFactory;
import hello.matdil.domain.user.repository.UserRepository;
import hello.matdil.global.exception.translator.DataIntegrityExceptionTranslator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserFactory userFactory;

    @Mock
    private DataIntegrityExceptionTranslator dataIntegrityExceptionTranslator;

    @Test
    void 회원가입_성공() {
        // given
        UserCreateRequestDto dto = new UserCreateRequestDto(
                "test@example.com",
                "password123",
                "홍길동",
                "01012345678",
                "서울",
                "강남",
                "101동"
        );

        given(userRepository.existsByEmail(dto.email())).willReturn(false);
        given(userRepository.existsByPhoneNumber(dto.phoneNumber())).willReturn(false);

        User user = User.builder()
                .email(dto.email())
                .password("encodedPassword")
                .name(dto.name())
                .phoneNumber(dto.phoneNumber())
                .address(new Address(dto.city(), dto.street(), dto.detailAddress()))
                .role(UserRole.USER)
                .build();

        given(userFactory.create(dto)).willReturn(user);
        given(userRepository.save(user)).willReturn(user);

        // when
        UserCreateResponseDto result = userService.createUser(dto);

        // then
        assertThat(result.getEmail()).isEqualTo(dto.email());
        assertThat(result.getName()).isEqualTo(dto.name());

        verify(userRepository).existsByEmail(dto.email());
        verify(userRepository).existsByPhoneNumber(dto.phoneNumber());
        verify(userFactory).create(dto);
        verify(userRepository).save(user);
    }
}
