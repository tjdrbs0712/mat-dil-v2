package hello.matdil.global.exception.translator;

import hello.matdil.domain.user.exception.UserException;
import hello.matdil.global.exception.CommonErrorCode;
import hello.matdil.domain.user.exception.UserErrorCode;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static hello.matdil.global.exception.translator.DataIntegrityExceptionTranslator.UK_USER_EMAIL;
import static hello.matdil.global.exception.translator.DataIntegrityExceptionTranslator.UK_USER_PHONE_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataIntegrityExceptionTranslatorTest {

    private final DataIntegrityExceptionTranslator translator = new DataIntegrityExceptionTranslator();

    @Test
    void 이메일_중복_제약조건이면_UserException_반환() {
        // given
        ConstraintViolationException cve = mock(ConstraintViolationException.class);
        when(cve.getConstraintName()).thenReturn(UK_USER_EMAIL);

        DataIntegrityViolationException exception = new DataIntegrityViolationException("error", cve);

        // when
        RuntimeException result = translator.translate(exception);

        // then
        assertThat(result).isInstanceOf(UserException.class);
        assertThat(((UserException) result).getErrorCode()).isEqualTo(UserErrorCode.EMAIL_DUPLICATION);
    }

    @Test
    void 전화번호_중복_제약조건이면_UserException_반환() {
        // given
        ConstraintViolationException cve = mock(ConstraintViolationException.class);
        when(cve.getConstraintName()).thenReturn(UK_USER_PHONE_NUMBER);

        DataIntegrityViolationException exception = new DataIntegrityViolationException("error", cve);

        // when
        RuntimeException result = translator.translate(exception);

        // then
        assertThat(result).isInstanceOf(UserException.class);
        assertThat(((UserException) result).getErrorCode()).isEqualTo(UserErrorCode.PHONE_DUPLICATION);
    }

    @Test
    void 알수없는_제약조건이면_INTERNAL_SERVER_ERROR_반환() {
        // given
        ConstraintViolationException cve = mock(ConstraintViolationException.class);
        when(cve.getConstraintName()).thenReturn("UNKNOWN_CONSTRAINT");

        DataIntegrityViolationException exception = new DataIntegrityViolationException("error", cve);

        // when
        RuntimeException result = translator.translate(exception);

        // then
        assertThat(result).isInstanceOf(UserException.class);
        assertThat(((UserException) result).getErrorCode()).isEqualTo(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Test
    void ConstraintViolation이_아니면_INTERNAL_SERVER_ERROR_반환() {
        // given
        DataIntegrityViolationException exception = new DataIntegrityViolationException("error", new RuntimeException());

        // when
        RuntimeException result = translator.translate(exception);

        // then
        assertThat(result).isInstanceOf(UserException.class);
        assertThat(((UserException) result).getErrorCode()).isEqualTo(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
}
