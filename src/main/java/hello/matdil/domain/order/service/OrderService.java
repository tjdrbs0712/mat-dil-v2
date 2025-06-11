package hello.matdil.domain.order.service;

import hello.matdil.domain.order.dto.*;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.order.entity.OrderStatus;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(Long userId, Long storeId, LocalDateTime expectedDeliveryTime
            , String requestNote, List<OrderItem> orderItems, OrderCreateRequestDto.AddressDto addressDto);

    SliceResponse<OrderSummaryDto, OrderCursorResponseDto> getUserOrders(
            Long userId, OrderCursorRequestDto cursor);

    OrderResponseDto getUserOrder(Long userId, UserRole role, Long orderId);

    OrderResponseDto getStoreOwnerOrder(Long userId, UserRole role, Long orderId);

    SliceResponse<OrderSummaryDto, OrderCursorResponseDto> getStoreOwnerOrders(
            Long userId, UserRole role, OrderCursorRequestDto cursor, Long storeId);

    void changeStoreOwnerOrderStatus(Long userId, UserRole role, Long orderId, OrderStatus newStatus);

    void changeUserOrderStatus(Long userId, UserRole role, Long orderId, OrderStatus newStatus);

    void updateOrderStatusToPaid(Long orderId);
}
