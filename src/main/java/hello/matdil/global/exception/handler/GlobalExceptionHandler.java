package hello.matdil.global.exception.handler;

import hello.matdil.global.exception.business.BusinessException;
import hello.matdil.global.exception.errocode.CommonErrorCode;
import hello.matdil.global.exception.errocode.ErrorCode;
import hello.matdil.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(ApiResponse.error(
                        errorCode.getCode(),
                        errorCode.getErrorMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedException(Exception e) {
        CommonErrorCode code = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(code.getHttpStatusCode())
                .body(ApiResponse.error(
                        code.getCode(),
                        code.getErrorMessage()
                ));
    }
}