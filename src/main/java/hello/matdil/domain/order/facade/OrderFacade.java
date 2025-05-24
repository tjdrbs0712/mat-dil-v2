package hello.matdil.domain.order.facade;

import hello.matdil.domain.order.dto.*;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.order.service.OrderCreateProcessor;
import hello.matdil.domain.order.service.OrderService;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.menu.util.MenuValidator;
import hello.matdil.domain.store.reader.StoreReader;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final OrderCreateProcessor orderCreateProcessor;
    private final StoreReader storeReader;

    public OrderResponseDto createOrder(Long userId, UserRole role, OrderCreateRequestDto dto) {
        Store store = storeReader.getStoreVisibleToUser(userId, dto.getStoreId(), role);

        MenuValidator.validateNoDuplicateMenuIds(dto.getOrderItems());
        List<OrderItem> orderItems = orderCreateProcessor.toOrderItems(dto.getOrderItems(), dto.getStoreId());

        return orderService.createOrder(userId, store.getId(), dto.getExpectedDeliveryTime(),
                dto.getRequestNote(), orderItems);
    }

    public SliceResponse<OrderSummaryDto, OrderCursorResponseDto> getOrders(
            Long userId, OrderCursorRequestDto cursor) {

        return orderService.getOrders(userId, cursor);
    }

    public OrderResponseDto getOrder(Long userId, UserRole role, Long orderId) {
        return orderService.getOrder(userId, role, orderId);
    }

    public OrderResponseDto getOwnerOrder(Long userId, UserRole role, Long orderId) {
        return orderService.getOwnerOrder(userId, role, orderId);
    }

    public SliceResponse<OrderSummaryDto, OrderCursorResponseDto> getOwnerOrders(
            Long userId, UserRole role, OrderCursorRequestDto cursor, Long storeId) {
        return orderService.getOwnerOrders(userId, role, cursor, storeId);
    }
}