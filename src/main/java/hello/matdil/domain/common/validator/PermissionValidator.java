package hello.matdil.domain.common.validator;

import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;

public class PermissionValidator {

    // 사장님이거나 관리자
    public static void validateOwnerOrAdmin(UserRole role) {
        if (role != UserRole.ADMIN && role != UserRole.OWNER) {
            throw new StoreException(StoreErrorCode.NO_PERMISSION);
        }
    }

    // 관리자만
    public static void validateAdminOnly(UserRole role) {
        if (role != UserRole.ADMIN) {
            throw new StoreException(StoreErrorCode.NO_PERMISSION);
        }
    }

}

