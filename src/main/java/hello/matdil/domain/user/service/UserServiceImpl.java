package hello.matdil.domain.user.service;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.user.dto.UserCreateRequestDto;
import hello.matdil.domain.user.dto.UserCreateResponseDto;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.domain.user.repository.UserRepository;
import hello.matdil.global.exception.business.UserException;
import hello.matdil.global.exception.errorcode.CommonErrorCode;
import hello.matdil.global.exception.errorcode.UserErrorCode;
import hello.matdil.global.exception.translator.DataIntegrityExceptionTranslator;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DataIntegrityExceptionTranslator dataIntegrityExceptionTranslator;

    @Override
    @Transactional
    public UserCreateResponseDto createUser(UserCreateRequestDto requestDto) {

        if (userRepository.existsByEmail(requestDto.email())) {
            throw new UserException(UserErrorCode.EMAIL_DUPLICATION);
        }

        if (userRepository.existsByPhoneNumber(requestDto.phoneNumber())) {
            throw new UserException(UserErrorCode.PHONE_DUPLICATION);
        }

        Address address = new Address(requestDto.city(), requestDto.street(), requestDto.detailAddress());

        User user = User.builder()
                .email(requestDto.email())
                .password(passwordEncoder.encode(requestDto.password()))
                .name(requestDto.name())
                .phoneNumber(requestDto.phoneNumber())
                .address(address)
                .role(UserRole.USER)
                .build();

        try {
            return UserCreateResponseDto.from(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw dataIntegrityExceptionTranslator.translate(e);
        }
    }
}
