package hello.matdil.domain.store.menu.util;

import hello.matdil.domain.order.dto.OrderItemRequestDto;
import hello.matdil.domain.store.menu.exception.MenuErrorCode;
import hello.matdil.domain.store.menu.exception.MenuException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MenuValidator {

    public static void validateNoDuplicateMenuIds(List<OrderItemRequestDto> items) {
        Set<Long> menuIds = new HashSet<>();
        for (OrderItemRequestDto item : items) {
            if (!menuIds.add(item.menuId())) {
                throw new MenuException(MenuErrorCode.DUPLICATE_MENU_IN_ORDER);
            }
        }
    }
}
