package hello.matdil.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuccessResponse<T> {
    private String code;
    private String message;
    private T data;

    public static <T> SuccessResponse<T> success(T data) {
        return new SuccessResponse<>(SuccessCode.SUCCESS.getCode(), SuccessCode.SUCCESS.getMessage(), data);
    }

    public static <T> SuccessResponse<T> success(SuccessCode successCode, T data) {
        return new SuccessResponse<>(successCode.getCode(), successCode.getMessage(), data);
    }
}
