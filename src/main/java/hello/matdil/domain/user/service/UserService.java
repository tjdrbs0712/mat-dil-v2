package hello.matdil.domain.user.service;

import hello.matdil.domain.user.dto.UserRegisterRequestDto;
import hello.matdil.domain.user.dto.UserRegisterResponseDto;

public interface UserService {

    UserRegisterResponseDto register(UserRegisterRequestDto requestDto);
}
