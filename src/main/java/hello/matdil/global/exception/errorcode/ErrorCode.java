package hello.matdil.global.exception.errorcode;

public interface ErrorCode {
    int getHttpStatusCode();
    String getCode();
    String getErrorMessage();
}
