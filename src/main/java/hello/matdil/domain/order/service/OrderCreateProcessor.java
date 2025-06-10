package hello.matdil.domain.order.service;

import hello.matdil.domain.order.dto.OrderItemRequestDto;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.store.menu.reader.MenuReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderCreateProcessor {

    private final MenuReader menuReader;

    public List<OrderItem> toOrderItems(List<OrderItemRequestDto> itemDtos, Long storeId) {
        List<Long> menuIds = itemDtos.stream()
                .map(OrderItemRequestDto::menuId)
                .toList();

        List<Menu> menus = menuReader.getMenusWithStoreValidation(menuIds, storeId);

        Map<Long, Menu> menuMap = menus.stream()
                .collect(Collectors.toMap(Menu::getId, menu -> menu));

        return itemDtos.stream()
                .map(itemDto -> {
                    Menu menu = menuMap.get(itemDto.menuId());
                    return OrderItem.of(menu, itemDto.quantity());
                })
                .toList();
    }
}