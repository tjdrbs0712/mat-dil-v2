package hello.matdil.domain.user.service;

import hello.matdil.domain.user.dto.UserCreateRequestDto;
import hello.matdil.domain.user.dto.UserCreateResponseDto;

public interface UserService {

    UserCreateResponseDto createUser(UserCreateRequestDto requestDto);

}
