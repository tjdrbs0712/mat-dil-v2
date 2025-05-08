package hello.matdil.global.exception.translator;

import hello.matdil.global.exception.BusinessException;
import hello.matdil.global.exception.CommonErrorCode;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

public abstract class AbstractDataIntegrityExceptionTranslator {

    public RuntimeException translate(DataIntegrityViolationException e) {
        if (e.getCause() instanceof ConstraintViolationException cve) {
            String constraint = cve.getConstraintName();
            return resolveException(constraint);
        }
        return defaultException();
    }

    // 자식 클래스에서 제약 조건명으로 예외를 결정
    protected abstract RuntimeException resolveException(String constraintName);

    // 기본 예외
    protected RuntimeException defaultException() {
        return new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
}
