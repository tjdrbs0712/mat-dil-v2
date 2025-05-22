package hello.matdil.domain.order.service;

import hello.matdil.domain.order.dto.OrderCursorRequestDto;
import hello.matdil.domain.order.dto.OrderCursorResponseDto;
import hello.matdil.domain.order.dto.OrderSummaryDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    Order createOrder(Long userId, Long storeId, LocalDateTime expectedDeliveryTime
            , String requestNote, List<OrderItem> orderItems);

    SliceResponse<OrderSummaryDto, OrderCursorResponseDto> getOrders(
            Long userId, OrderCursorRequestDto cursor);

    Order getOrder(Long orderId, Long userId, UserRole role);

}
