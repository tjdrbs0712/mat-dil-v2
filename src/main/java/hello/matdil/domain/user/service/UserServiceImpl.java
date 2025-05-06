package hello.matdil.domain.user.service;

import hello.matdil.domain.user.dto.UserRegisterRequestDto;
import hello.matdil.domain.user.dto.UserRegisterResponseDto;
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
        User user = userFactory.create(requestDto);

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

}
