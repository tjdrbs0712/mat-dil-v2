package hello.matdil.domain.common.validator;

import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;

public class PermissionValidator {

    // 사장님이거나 관리자
    public static void validateOwnerOrAdmin(Long loginUserId, Long ownerId, UserRole role) {
        if (!loginUserId.equals(ownerId) && role != UserRole.ADMIN) {
            throw new StoreException(StoreErrorCode.NO_PERMISSION);
        }
    }

    // 관리자만
    public static void validateAdminOnly(UserRole role) {
        if (role != UserRole.ADMIN) {
            throw new StoreException(StoreErrorCode.NO_PERMISSION);
        }
    }

    // 사장님만 (자기 가게만)
    public static void validateOwnerOnly(Long loginUserId, Long ownerId) {
        if (!loginUserId.equals(ownerId)) {
            throw new StoreException(StoreErrorCode.NO_PERMISSION);
        }
    }

    // 특정 유저 본인만 접근 가능
    public static void validateSelfOnly(Long loginUserId, Long targetUserId) {
        if (!loginUserId.equals(targetUserId)) {
            throw new UserException(UserErrorCode.NO_PERMISSION);
        }
    }
}

