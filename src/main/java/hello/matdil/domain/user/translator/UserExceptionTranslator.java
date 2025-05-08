package hello.matdil.domain.user.translator;

import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;
import hello.matdil.global.exception.translator.AbstractDataIntegrityExceptionTranslator;
import org.springframework.stereotype.Component;

@Component
public class UserExceptionTranslator extends AbstractDataIntegrityExceptionTranslator {

    public static final String UK_USER_EMAIL = "UK_user_email";
    public static final String UK_USER_PHONE = "UK_user_phone_number";

    @Override
    protected RuntimeException resolveException(String constraintName) {
        return switch (constraintName) {
            case UK_USER_EMAIL -> new UserException(UserErrorCode.EMAIL_DUPLICATION);
            case UK_USER_PHONE -> new UserException(UserErrorCode.PHONE_DUPLICATION);
            default -> defaultException();
        };
    }
}
