package hello.matdil.domain.user.service;

import hello.matdil.domain.user.dto.UserInfoResponseDto;
import hello.matdil.domain.user.dto.UserRegisterRequestDto;
import hello.matdil.domain.user.dto.UserRegisterResponseDto;
import hello.matdil.domain.user.dto.UserInfoChangeRequestDto;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;
import hello.matdil.domain.user.factory.UserFactory;
import hello.matdil.domain.user.repository.UserRepository;
import hello.matdil.global.exception.translator.DataIntegrityExceptionTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final DataIntegrityExceptionTranslator dataIntegrityExceptionTranslator;

    @Override
    @Transactional
    public UserRegisterResponseDto register(UserRegisterRequestDto requestDto) {
        validateDuplicateUser(requestDto);
        User user = userFactory.from(requestDto);

        try {
            return UserRegisterResponseDto.from(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw dataIntegrityExceptionTranslator.translate(e);
        }
    }

    private void validateDuplicateUser(UserRegisterRequestDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new UserException(UserErrorCode.EMAIL_DUPLICATION);
        }

        if (userRepository.existsByPhoneNumber(dto.phoneNumber())) {
            throw new UserException(UserErrorCode.PHONE_DUPLICATION);
        }
    }

    //쿼리dsl로 유저가 ACTIVE인지 확인해야됨
    @Override
    @Transactional(readOnly = true)
    public UserInfoResponseDto getMyInfo(Long userId) {
        return userRepository.findById(userId)
                .map(UserInfoResponseDto::from)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    //회원정보 수정
    @Override
    @Transactional
    public UserInfoResponseDto updateMyInfo(Long userId, UserInfoChangeRequestDto requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        user.update(requestDto);

        return UserInfoResponseDto.from(user);
    }
}
