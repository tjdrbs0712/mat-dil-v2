package hello.matdil.global.exception;

public interface ErrorCode {
    int getHttpStatusCode();
    String getCode();
    String getErrorMessage();
}
