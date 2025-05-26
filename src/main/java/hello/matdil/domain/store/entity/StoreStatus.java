package hello.matdil.domain.store.entity;

import hello.matdil.domain.user.entity.UserRole;

public enum StoreStatus {
    OPEN,       // 영업 중
    CLOSED,     // 휴무
    INACTIVE,   // 비활성화
    DELETED;    // 삭제

    public boolean isVisibleTo(UserRole role, Long userId, Long ownerId) {
        return switch (this) {
            case OPEN, CLOSED -> true;
            case INACTIVE -> role == UserRole.ADMIN || (role == UserRole.OWNER && userId.equals(ownerId));
            case DELETED -> role == UserRole.ADMIN;
        };
    }
}
