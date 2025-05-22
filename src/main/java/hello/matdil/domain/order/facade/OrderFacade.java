package hello.matdil.domain.order.facade;

import hello.matdil.domain.order.dto.*;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.order.service.OrderService;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.store.menu.exception.MenuErrorCode;
import hello.matdil.domain.store.menu.exception.MenuException;
import hello.matdil.domain.store.menu.reader.MenuReader;
import hello.matdil.domain.store.reader.StoreReader;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final StoreReader storeReader;
    private final MenuReader menuReader;

    public OrderResponseDto createOrder(Long userId, UserRole role, OrderCreateRequestDto dto) {
        Store store = storeReader.getStoreWithPermission(userId, dto.getStoreId(), role);

        validateDuplicateMenuIds(dto.getOrderItems());

        List<OrderItem> orderItems = toOrderItems(dto.getOrderItems(), dto.getStoreId());
        Order order = orderService.createOrder(userId, store.getId(), dto.getExpectedDeliveryTime(),
                dto.getRequestNote(), orderItems);

        return OrderResponseDto.from(order);
    }

    private List<OrderItem> toOrderItems(List<OrderItemRequestDto> itemDtos, Long storeId) {
        return itemDtos.stream()
                .map(itemDto -> {
                    Menu menu = menuReader.getMenuWithStoreValidation(itemDto.getMenuId(), storeId);
                    return OrderItem.of(menu, itemDto.getQuantity());
                })
                .toList();
    }

    private void validateDuplicateMenuIds(List<OrderItemRequestDto> items) {
        Set<Long> menuIds = new HashSet<>();
        for (OrderItemRequestDto item : items) {
            if (!menuIds.add(item.getMenuId())) {
                throw new MenuException(MenuErrorCode.DUPLICATE_MENU_IN_ORDER);
            }
        }
    }

    public SliceResponse<OrderSummaryDto, OrderCursorResponseDto> getOrders(
            Long userId, OrderCursorRequestDto requestDto) {

        return orderService.getOrders(userId, requestDto);
    }

    public OrderResponseDto getOrder(Long userId, UserRole role, Long orderId) {
        Order order = orderService.getOrder(orderId, userId, role);
        return OrderResponseDto.from(order);
    }

}