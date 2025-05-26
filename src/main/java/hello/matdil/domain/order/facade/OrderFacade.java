package hello.matdil.domain.order.facade;

import hello.matdil.domain.order.dto.*;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.order.entity.OrderStatus;
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

    public OrderResponseDto createOrder(Long userId, OrderCreateRequestDto dto) {
        Store store = storeReader.readWithOpen(dto.getStoreId());

        MenuValidator.validateNoDuplicateMenuIds(dto.getOrderItems());
        List<OrderItem> orderItems = orderCreateProcessor.toOrderItems(dto.getOrderItems(), dto.getStoreId());

        return orderService.createOrder(userId, store.getId(), dto.getExpectedDeliveryTime(),
                dto.getRequestNote(), orderItems);
    }

    public SliceResponse<OrderSummaryDto, OrderCursorResponseDto> getUserOrders(
            Long userId, OrderCursorRequestDto cursor) {

        return orderService.getUserOrders(userId, cursor);
    }

    public OrderResponseDto getUserOrder(Long userId, UserRole role, Long orderId) {
        return orderService.getUserOrder(userId, role, orderId);
    }

    public OrderResponseDto getStoreOwnerOrder(Long userId, UserRole role, Long orderId) {
        return orderService.getStoreOwnerOrder(userId, role, orderId);
    }

    public SliceResponse<OrderSummaryDto, OrderCursorResponseDto> getStoreOwnerOrders(
            Long userId, UserRole role, OrderCursorRequestDto cursor, Long storeId) {
        return orderService.getStoreOwnerOrders(userId, role, cursor, storeId);
    }

    public void changeStoreOwnerOrderStatus(Long userId, UserRole role, Long orderId, OrderStatus status) {
        orderService.changeStoreOwnerOrderStatus(userId, role, orderId, status);
    }

    public void changeUserOrderStatus(Long userId, UserRole role, Long orderId, OrderStatus status) {
        orderService.changeUserOrderStatus(userId, role, orderId, status);
    }
}