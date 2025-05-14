package hello.matdil.auth.controller;

import hello.matdil.auth.dto.RefreshTokenRequestDto;
import hello.matdil.auth.dto.RefreshTokenResponseDto;
import hello.matdil.auth.service.AuthService;
import hello.matdil.domain.user.dto.UserLoginRequestDto;
import hello.matdil.domain.user.dto.UserLoginResponseDto;
import hello.matdil.domain.user.dto.UserRegisterRequestDto;
import hello.matdil.domain.user.dto.UserRegisterResponseDto;
import hello.matdil.domain.user.service.UserService;
import hello.matdil.global.response.SuccessCode;
import hello.matdil.global.response.SuccessResponse;
import hello.matdil.mail.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final EmailVerificationService emailVerificationService;

    /**
     * 회원가입
     */
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<UserRegisterResponseDto>> register(
            @RequestBody UserRegisterRequestDto requestDto) {
        UserRegisterResponseDto responseDto = userService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.success(responseDto));
    }

    // 중복 확인 API
    @GetMapping("/check-email")
    public ResponseEntity<SuccessResponse<Void>> checkEmail(@RequestParam String email) {
        userService.checkEmailDuplicate(email);
        return ResponseEntity.ok(SuccessResponse.success(null));
    }

    @GetMapping("/check-phone")
    public ResponseEntity<SuccessResponse<Void>> checkPhone(@RequestParam String phone) {
        userService.checkPhoneDuplicate(phone);
        return ResponseEntity.ok(SuccessResponse.success(null));
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<UserLoginResponseDto>> login(@RequestBody UserLoginRequestDto requestDto) {
        UserLoginResponseDto responseDto = authService.login(requestDto.email(), requestDto.password());
        return ResponseEntity.ok(SuccessResponse.success(responseDto));
    }

    /**
     * 토큰 재발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<SuccessResponse<RefreshTokenResponseDto>> refresh(
            @RequestBody RefreshTokenRequestDto requestDto) {
        RefreshTokenResponseDto responseDto = authService.refreshAccessToken(requestDto.refreshToken());
        return ResponseEntity.ok(SuccessResponse.success(responseDto));
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<Void>> logout(@RequestHeader("Authorization") String bearerToken) {
        authService.logout(bearerToken);
        return ResponseEntity.ok(SuccessResponse.success(null));
    }

    /**
     * 이메일 인증 확인
     */
    @GetMapping("/verify-email")
    public ResponseEntity<SuccessResponse<Void>> verifyEmail(@RequestParam String token) {
        emailVerificationService.verify(token);
        return ResponseEntity.ok(SuccessResponse.success(SuccessCode.EMAIL_VERIFICATION_SUCCESS, null));
    }
}
