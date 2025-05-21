package hello.matdil.domain.store.menu.entity;

import hello.matdil.domain.user.entity.UserRole;

public enum MenuStatus {
    AVAILABLE,  //판매중
    SOLD_OUT,   //품절
    HIDDEN,     //숨김
    DELETE;      //삭제

    public boolean isVisibleTo(UserRole role, Long userId, Long ownerId) {
        return switch (this) {
            case AVAILABLE, SOLD_OUT -> true;
            case HIDDEN -> role == UserRole.ADMIN || (role == UserRole.OWNER && userId.equals(ownerId));
            case DELETE -> role == UserRole.ADMIN;
        };
    }
}
