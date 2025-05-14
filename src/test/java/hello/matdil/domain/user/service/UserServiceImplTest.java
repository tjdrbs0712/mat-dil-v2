package hello.matdil.domain.user.service;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.user.dto.UserRegisterRequestDto;
import hello.matdil.domain.user.dto.UserRegisterResponseDto;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.domain.user.event.UserMailSendEvent;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;
import hello.matdil.domain.user.factory.UserFactory;
import hello.matdil.domain.user.repository.UserRepository;
import hello.matdil.domain.user.validator.UserValidator;
import hello.matdil.event.GenericEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserFactory userFactory;

    @Mock
    private GenericEventPublisher genericEventPublisher;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void 회원가입_정상_흐름() {
        // given
        UserRegisterRequestDto dto = UserRegisterRequestDto.builder()
                .email("user@example.com")
                .password("pass1234")
                .name("박성균")
                .phoneNumber("01011112222")
                .city("서울")
                .street("강남")
                .detailAddress("101호")
                .build();

        User user = User.builder()
                .email(dto.email())
                .password("encoded")
                .name(dto.name())
                .phoneNumber(dto.phoneNumber())
                .address(new Address(dto.city(), dto.street(), dto.detailAddress()))
                .role(UserRole.USER)
                .build();

        given(userFactory.createUser(dto)).willReturn(user);
        given(userRepository.save(user)).willReturn(user);

        // when
        UserRegisterResponseDto response = userService.register(dto);

        // then
        assertThat(response.getEmail()).isEqualTo(dto.email());
        verify(userValidator).validate(dto);
        verify(userFactory).createUser(dto);
        verify(userRepository).save(user);
        verify(genericEventPublisher).publish(any(UserMailSendEvent.class));
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
