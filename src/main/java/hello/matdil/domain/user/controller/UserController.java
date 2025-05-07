package hello.matdil.domain.user.controller;

import hello.matdil.auth.dto.RefreshTokenRequestDto;
import hello.matdil.auth.dto.RefreshTokenResponseDto;
import hello.matdil.auth.security.UserDetailsImpl;
import hello.matdil.auth.service.AuthService;
import hello.matdil.domain.user.dto.*;
import hello.matdil.domain.user.service.UserService;
import hello.matdil.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
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

    //회원정보 조회
    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<UserInfoResponseDto>> getMyInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserInfoResponseDto responseDto = userService.getMyInfo(userDetails.getUserId());
        return ResponseEntity.ok(SuccessResponse.success(responseDto));
    }

    //회원정보 수정
    @PutMapping("/me")
    public ResponseEntity<SuccessResponse<UserInfoResponseDto>> updateMyInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserInfoChangeRequestDto requestDto) {

        UserInfoResponseDto responseDto = userService.updateMyInfo(userDetails.getUserId(), requestDto);
        return ResponseEntity.ok(SuccessResponse.success(responseDto));
    }



}