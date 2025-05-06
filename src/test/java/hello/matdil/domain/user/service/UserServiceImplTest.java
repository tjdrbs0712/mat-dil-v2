package hello.matdil.domain.user.service;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.user.dto.UserCreateRequestDto;
import hello.matdil.domain.user.dto.UserCreateResponseDto;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.entity.UserRole;
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

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private DataIntegrityExceptionTranslator translator;

    @Test
    void 회원가입_성공() {
        // given
        UserCreateRequestDto dto = new UserCreateRequestDto(
                "test@example.com",
                "password123",
                "테스트유저",
                "01012345678",
                "서울시", "강남구", "101동"
        );

        given(userRepository.existsByEmail(dto.email())).willReturn(false);
        given(userRepository.existsByPhoneNumber(dto.phoneNumber())).willReturn(false);
        given(passwordEncoder.encode(dto.password())).willReturn("암호화된비밀번호");

        User user = User.builder()
                .email(dto.email())
                .password("암호화된비밀번호")
                .name(dto.name())
                .phoneNumber(dto.phoneNumber())
                .address(new Address(dto.city(), dto.street(), dto.detailAddress()))
                .role(UserRole.USER)
                .build();

        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        UserCreateResponseDto result = userService.createUser(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(dto.email());
        assertThat(result.getName()).isEqualTo(dto.name());
    }
}
