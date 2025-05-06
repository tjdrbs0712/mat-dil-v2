package hello.matdil.global.response;

import hello.matdil.global.exception.errorcode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String code;
    private final String message;
    private final Map<String, String> errors; // Optional, validation용

    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code.getCode(), code.getErrorMessage(), null);
    }

    public static ErrorResponse of(ErrorCode code, Map<String, String> errors) {
        return new ErrorResponse(code.getCode(), code.getErrorMessage(), errors);
    }

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, null);
    }
}
