package hello.matdil.auth.service;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.user.dto.UserLoginResponseDto;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;
import hello.matdil.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    private final String email = "user@example.com";
    private final String rawPassword = "password123";
    private final String encodedPassword = "encoded_password";

    private User activeUser;

    @BeforeEach
    void setUp() {
        Address address = new Address("서울", "강남", "101호");
        activeUser = User.builder()
                .email(email)
                .password(encodedPassword)
                .name("홍길동")
                .phoneNumber("01012345678")
                .role(UserRole.USER)
                .address(address)
                .build();

        activeUser.verifyEmail();
    }

    @Test
    void 로그인_성공() {
        // given
        given(userRepository.findByEmail(email)).willReturn(Optional.of(activeUser));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);
        given(tokenService.generateAccessToken(activeUser)).willReturn("access-token");
        given(tokenService.generateRefreshToken(activeUser)).willReturn("refresh-token");

        // when
        UserLoginResponseDto result = authService.login(email, rawPassword);

        // then
        assertThat(result.getAccessToken()).isEqualTo("access-token");
        assertThat(result.getRefreshToken()).isEqualTo("refresh-token");
    }

    @Test
    void 로그인_실패_이메일_에러() {
        // given
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.login(email, rawPassword))
                .isInstanceOf(UserException.class)
                .hasMessageContaining(UserErrorCode.AUTHENTICATION_FAILED.getErrorMessage());
    }

    @Test
    void 로그인_실패_비밀번호_에러() {
        // given
        given(userRepository.findByEmail(email)).willReturn(Optional.of(activeUser));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.login(email, rawPassword))
                .isInstanceOf(UserException.class)
                .hasMessageContaining(UserErrorCode.AUTHENTICATION_FAILED.getErrorMessage());
    }

    @Test
    void 로그인_실패_탈퇴회원() {
        // given
        activeUser.userStatusWithdraw();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(activeUser));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> authService.login(email, rawPassword))
                .isInstanceOf(UserException.class)
                .hasMessageContaining(UserErrorCode.WITHDRAWN_USER.getErrorMessage());
    }
}
