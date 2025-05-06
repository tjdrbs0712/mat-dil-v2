package hello.matdil.domain.user.controller;

import hello.matdil.auth.dto.RefreshTokenRequestDto;
import hello.matdil.auth.dto.RefreshTokenResponseDto;
import hello.matdil.auth.service.AuthService;
import hello.matdil.domain.user.dto.UserLoginRequestDto;
import hello.matdil.domain.user.dto.UserLoginResponseDto;
import hello.matdil.domain.user.dto.UserRegisterRequestDto;
import hello.matdil.domain.user.dto.UserRegisterResponseDto;
import hello.matdil.domain.user.service.UserService;
import hello.matdil.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    /**
     * 회원가입 api
     */
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<UserRegisterResponseDto>> register(
            @RequestBody UserRegisterRequestDto requestDto) {

        UserRegisterResponseDto responseDto = userService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.success(responseDto));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<UserLoginResponseDto>> login(@RequestBody UserLoginRequestDto requestDto) {
        UserLoginResponseDto responseDto = authService.login(requestDto.email(), requestDto.password());
        return ResponseEntity.ok(SuccessResponse.success(responseDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<SuccessResponse<RefreshTokenResponseDto>> refresh(@RequestBody RefreshTokenRequestDto requestDto) {
        RefreshTokenResponseDto responseDto = authService.refreshAccessToken(requestDto.refreshToken());
        return ResponseEntity.ok(SuccessResponse.success(responseDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<Void>> logout(@RequestHeader("Authorization") String bearerToken) {
        authService.logout(bearerToken);
        return ResponseEntity.ok(SuccessResponse.success(null));
    }


}