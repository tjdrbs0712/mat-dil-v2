package hello.matdil.domain.favorite.execption;

import hello.matdil.global.exception.BusinessException;
import hello.matdil.global.exception.ErrorCode;

public class FavoriteException extends BusinessException {
    public FavoriteException(ErrorCode errorCode) {
        super(errorCode);
    }
}
