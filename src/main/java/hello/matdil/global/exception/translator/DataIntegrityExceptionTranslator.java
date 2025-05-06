package hello.matdil.global.exception.translator;

import hello.matdil.domain.user.exception.UserException;
import hello.matdil.global.exception.CommonErrorCode;
import hello.matdil.domain.user.exception.UserErrorCode;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class DataIntegrityExceptionTranslator {

    public static String UK_USER_EMAIL = "UK_user_email";
    public static String UK_USER_PHONE_NUMBER = "UK_user_phone_number";

    public RuntimeException translate(DataIntegrityViolationException e) {
        if (e.getCause() instanceof ConstraintViolationException cve) {
            String constraintName = cve.getConstraintName();

            if (UK_USER_EMAIL.equals(constraintName)) {
                return new UserException(UserErrorCode.EMAIL_DUPLICATION);
            }
            if (UK_USER_PHONE_NUMBER.equals(constraintName)) {
                return new UserException(UserErrorCode.PHONE_DUPLICATION);
            }
        }
        return new UserException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
}
