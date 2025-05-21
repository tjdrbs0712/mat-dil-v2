package hello.matdil.domain.user.service;

import hello.matdil.domain.user.dto.*;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.entity.UserStatus;
import hello.matdil.domain.user.event.UserMailSendEvent;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;
import hello.matdil.domain.user.factory.UserFactory;
import hello.matdil.domain.user.reader.UserReader;
import hello.matdil.domain.user.repository.UserRepository;
import hello.matdil.domain.user.translator.UserExceptionTranslator;
import hello.matdil.domain.user.validator.UserValidator;
import hello.matdil.event.GenericEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final UserExceptionTranslator userExceptionTranslator;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final GenericEventPublisher genericEventPublisher;
    private final UserReader userReader;

    @Override
    @Transactional
    public UserRegisterResponseDto register(UserRegisterRequestDto requestDto) {

        User user = userFactory.createUser(requestDto);
        UserRegisterResponseDto responseDto;
        try {
            responseDto = UserRegisterResponseDto.from(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw userExceptionTranslator.translate(e);
        }

        genericEventPublisher.publish(new UserMailSendEvent(user.getEmail()));
        return responseDto;
    }

    @Override
    public void checkEmailDuplicate(String email) {
        userValidator.validateEmail(email);
    }

    @Override
    public void checkPhoneDuplicate(String phoneNumber) {
        userValidator.validatePhoneNumber(phoneNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoResponseDto getMyInfo(Long userId) {
        return userRepository.findByIdAndUserStatus(userId, UserStatus.ACTIVE)
                .map(UserInfoResponseDto::from)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }


    //회원정보 수정
    @Override
    @Transactional
    public UserInfoResponseDto updateMyInfo(Long userId, UserInfoChangeRequestDto requestDto) {
        User user = userReader.getActiveUser(userId);
        user.update(requestDto);
        return UserInfoResponseDto.from(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, PasswordChangeRequestDto dto) {
        User user = userReader.getActiveUser(userId);
        userValidator.validatePasswordChange(user, dto);
        String newPassword = passwordEncoder.encode(dto.getNewPassword());
        user.changePassword(newPassword);
    }

    @Override
    @Transactional
    public void withdrawUser(Long userId) {
        User user = userReader.getActiveUser(userId);
        user.userStatusWithdraw();
    }
}
