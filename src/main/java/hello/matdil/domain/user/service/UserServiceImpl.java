package hello.matdil.domain.user.service;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.user.dto.UserCreateRequestDto;
import hello.matdil.domain.user.dto.UserCreateResponseDto;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.domain.user.factory.UserFactory;
import hello.matdil.domain.user.repository.UserRepository;
import hello.matdil.domain.user.exception.UserException;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.global.exception.translator.DataIntegrityExceptionTranslator;
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
    private final DataIntegrityExceptionTranslator dataIntegrityExceptionTranslator;

    @Override
    @Transactional
    public UserCreateResponseDto createUser(UserCreateRequestDto requestDto) {
        validateDuplicateUser(requestDto);
        User user = userFactory.create(requestDto);

        try {
            return UserCreateResponseDto.from(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw dataIntegrityExceptionTranslator.translate(e);
        }
    }

    private void validateDuplicateUser(UserCreateRequestDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new UserException(UserErrorCode.EMAIL_DUPLICATION);
        }

        if (userRepository.existsByPhoneNumber(dto.phoneNumber())) {
            throw new UserException(UserErrorCode.PHONE_DUPLICATION);
        }
    }

}
