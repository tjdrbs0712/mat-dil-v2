package hello.matdil.domain.user.validator;

import hello.matdil.domain.user.dto.PasswordChangeRequestDto;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;
import hello.matdil.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserException(UserErrorCode.EMAIL_DUPLICATION);
        }
    }

    public void validatePhoneNumber(String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new UserException(UserErrorCode.PHONE_DUPLICATION);
        }
    }

    public void validatePasswordChange(User user, PasswordChangeRequestDto dto) {
        if (!user.isPasswordMatch(dto.getCurrentPassword(), passwordEncoder)) {
            throw new UserException(UserErrorCode.INVALID_CURRENT_PASSWORD);
        }

        if (user.isPasswordMatch(dto.getNewPassword(), passwordEncoder)) {
            throw new UserException(UserErrorCode.SAME_AS_OLD_PASSWORD);
        }
    }

}
