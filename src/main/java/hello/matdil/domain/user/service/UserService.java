package hello.matdil.domain.user.service;

import hello.matdil.domain.user.dto.*;

public interface UserService {

    UserRegisterResponseDto register(UserRegisterRequestDto requestDto);

    UserInfoResponseDto getMyInfo(Long userId);

    UserInfoResponseDto updateMyInfo(Long userId, UserInfoChangeRequestDto requestDto);

    void changePassword(Long userId, PasswordChangeRequestDto requestDto);
}
