package hello.matdil.global.exception.errocode;

public interface ErrorCode {
    int getHttpStatusCode();
    String getCode();
    String getErrorMessage();
}
