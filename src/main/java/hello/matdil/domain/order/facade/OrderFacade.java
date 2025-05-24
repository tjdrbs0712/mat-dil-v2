package hello.matdil.domain.order.facade;

import hello.matdil.domain.order.dto.*;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.order.service.OrderService;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.store.menu.reader.MenuReader;
import hello.matdil.domain.store.menu.util.MenuValidator;
import hello.matdil.domain.store.reader.StoreReader;
import hello.matdil.domain.store.reader.StoreSummaryLoader;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final StoreReader storeReader;
    private final MenuReader menuReader;
    private final StoreSummaryLoader storeSummaryLoader;

    public OrderResponseDto createOrder(Long userId, UserRole role, OrderCreateRequestDto dto) {
        Store store = storeReader.getStoreWithPermission(userId, dto.getStoreId(), role);

        MenuValidator.validateNoDuplicateMenuIds(dto.getOrderItems());

        List<OrderItem> orderItems = toOrderItems(dto.getOrderItems(), dto.getStoreId());

        return orderService.createOrder(userId, store.getId(), dto.getExpectedDeliveryTime(),
                dto.getRequestNote(), orderItems);
    }

    private List<OrderItem> toOrderItems(List<OrderItemRequestDto> itemDtos, Long storeId) {
        return itemDtos.stream()
                .map(itemDto -> {
                    Menu menu = menuReader.getMenuWithStoreValidation(itemDto.getMenuId(), storeId);
                    return OrderItem.of(menu, itemDto.getQuantity());
                })
                .toList();
    }

    public SliceResponse<OrderSummaryDto, OrderCursorResponseDto> getOrders(
            Long userId, OrderCursorRequestDto requestDto) {

        return orderService.getOrders(userId, requestDto);
    }

    public OrderResponseDto getOrder(Long userId, UserRole role, Long orderId) {
        return orderService.getOrder(userId, role, orderId);
    }

}