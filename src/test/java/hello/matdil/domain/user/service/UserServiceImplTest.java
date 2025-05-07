package hello.matdil.domain.user.service;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.user.dto.UserRegisterRequestDto;
import hello.matdil.domain.user.dto.UserRegisterResponseDto;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;
import hello.matdil.domain.user.factory.UserFactory;
import hello.matdil.domain.user.repository.UserRepository;
import hello.matdil.global.exception.translator.DataIntegrityExceptionTranslator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        UserRegisterRequestDto dto = new UserRegisterRequestDto(
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

        given(userFactory.from(dto)).willReturn(user);
        given(userRepository.save(user)).willReturn(user);

        // when
        UserRegisterResponseDto result = userService.register(dto);

        // then
        assertThat(result.getEmail()).isEqualTo(dto.email());
        assertThat(result.getName()).isEqualTo(dto.name());

        verify(userRepository).existsByEmail(dto.email());
        verify(userRepository).existsByPhoneNumber(dto.phoneNumber());
        verify(userFactory).from(dto);
        verify(userRepository).save(user);
    }

    @Test
    void 회원정보_조회_실패() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> userService.getMyInfo(userId))
                .isInstanceOf(UserException.class)
                .hasMessageContaining(UserErrorCode.USER_NOT_FOUND.getErrorMessage());
    }

}
