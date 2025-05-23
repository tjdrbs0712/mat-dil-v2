package hello.matdil.domain.store.menu.exception;

import hello.matdil.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuErrorCode implements ErrorCode {
    NO_PERMISSION(403, "NO_PERMISSION", "해당 메뉴에 대한 권한이 없습니다."),
    MENU_NOT_FOUND(404, "STORE_NOT_FOUND", "해당 메뉴를 찾을 수 없습니다."),
    ALREADY_DELETED(400, "ALREADY_DELETED", "이미 삭제된 메뉴입니다."),
    UNAVAILABLE_MENU(400, "UNAVAILABLE_MENU", "판매 불가능한 메뉴입니다."),
    DUPLICATE_MENU_IN_ORDER(400, "DUPLICATE_MENU_IN_ORDER", "주문 항목에 중복된 메뉴가 포함되어 있습니다.")
    ;

    private final int httpStatusCode;
    private final String code;
    private final String errorMessage;
}
