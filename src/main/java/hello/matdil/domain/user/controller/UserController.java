package hello.matdil.domain.user.controller;

import hello.matdil.domain.user.dto.UserRegisterRequestDto;
import hello.matdil.domain.user.dto.UserRegisterResponseDto;
import hello.matdil.domain.user.service.UserService;
import hello.matdil.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 api
     */
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<UserRegisterResponseDto>>  register(
            @RequestBody UserRegisterRequestDto requestDto) {

        UserRegisterResponseDto responseDto = userService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.success(responseDto));
    }
}
