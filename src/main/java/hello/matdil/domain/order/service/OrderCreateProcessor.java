package hello.matdil.domain.order.service;

import hello.matdil.domain.order.dto.OrderItemRequestDto;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.store.menu.reader.MenuReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderCreateProcessor {

    private final MenuReader menuReader;

    public List<OrderItem> toOrderItems(List<OrderItemRequestDto> itemDtos, Long storeId) {
        return itemDtos.stream()
                .map(itemDto -> {
                    Menu menu = menuReader.getMenuWithStoreValidation(itemDto.getMenuId(), storeId);
                    return OrderItem.of(menu, itemDto.getQuantity());
                })
                .toList();
    }
}