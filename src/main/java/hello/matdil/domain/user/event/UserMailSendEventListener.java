package hello.matdil.domain.user.event;

import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;
import hello.matdil.domain.user.repository.UserRepository;
import hello.matdil.mail.entity.EmailToken;
import hello.matdil.mail.repository.EmailTokenRepository;
import hello.matdil.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserMailSendEventListener {

    private final MailService mailService;
    private final EmailTokenRepository emailTokenRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserMailSendEvent event) {

        EmailToken token = EmailToken.generate(event.email());
        emailTokenRepository.save(token);

        mailService.sendEmailVerification(event.email(), token.getToken());
    }

}
