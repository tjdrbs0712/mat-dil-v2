package hello.matdil.domain.user.controller;

import hello.matdil.auth.security.UserDetailsImpl;
import hello.matdil.domain.user.dto.PasswordChangeRequestDto;
import hello.matdil.domain.user.dto.UserInfoChangeRequestDto;
import hello.matdil.domain.user.dto.UserInfoResponseDto;
import hello.matdil.domain.user.service.UserService;
import hello.matdil.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * 내 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<UserInfoResponseDto>> getMyInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserInfoResponseDto responseDto = userService.getMyInfo(userDetails.getUserId());
        return ResponseEntity.ok(SuccessResponse.success(responseDto));
    }

    /**
     * 내 정보 수정
     */
    @PutMapping("/me")
    public ResponseEntity<SuccessResponse<UserInfoResponseDto>> updateMyInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserInfoChangeRequestDto requestDto) {
        UserInfoResponseDto responseDto = userService.updateMyInfo(userDetails.getUserId(), requestDto);
        return ResponseEntity.ok(SuccessResponse.success(responseDto));
    }

    /**
     * 비밀번호 변경
     */
    @PatchMapping("/password")
    public ResponseEntity<SuccessResponse<Void>> changePassword(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody PasswordChangeRequestDto requestDto) {
        userService.changePassword(userDetails.getUserId(), requestDto);
        return ResponseEntity.ok(SuccessResponse.success(null));
    }

    /**
     * 회원 탈퇴
     */
    @PatchMapping("/me")
    public ResponseEntity<SuccessResponse<Void>> withdraw(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.withdrawUser(userDetails.getUserId());
        return ResponseEntity.ok(SuccessResponse.success(null));
    }
}
