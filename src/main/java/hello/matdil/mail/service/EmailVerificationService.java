package hello.matdil.mail.service;

import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;
import hello.matdil.domain.user.repository.UserRepository;
import hello.matdil.mail.entity.EmailToken;
import hello.matdil.mail.repository.EmailTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationService {

    private final EmailTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public void verify(String token) {
        EmailToken tokenEntity = tokenRepository.findById(token)
                .orElseThrow(() -> new UserException(UserErrorCode.INVALID_EMAIL_TOKEN));

        if (tokenEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UserException(UserErrorCode.INVALID_EMAIL_TOKEN);
        }

        User user = userRepository.findByEmail(tokenEntity.getEmail())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        user.verifyEmail();
    }
}
