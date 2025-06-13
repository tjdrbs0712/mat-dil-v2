package hello.matdil.domain.delivery.dto;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.delivery.entity.Delivery;
import hello.matdil.domain.delivery.entity.DeliveryStatus;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;

import java.util.List;

public record DeliveryAssignmentResponseDto(
        Long deliveryId,
        Long orderId,
        DeliveryStatus deliveryStatus,

        String storeName,
        Address storeAddress,

        Address deliveryAddress,
        String requestNote,

        List<OrderItemDto> orderItems,
        int totalPrice
) {

    public record OrderItemDto(String menuName, int quantity) {
        public static OrderItemDto from(OrderItem orderItem) {
            return new OrderItemDto(orderItem.getMenuName(), orderItem.getQuantity());
        }
    }

    public static DeliveryAssignmentResponseDto from(
            Delivery delivery,
            Order order,
            StoreSummaryResponseDto storeSummary
    ) {
        List<OrderItemDto> itemDtos = order.getOrderItems().stream()
                .map(OrderItemDto::from)
                .toList();

        return new DeliveryAssignmentResponseDto(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getDeliveryStatus(),
                storeSummary.getName(),
                storeSummary.getAddress(),
                delivery.getAddress(),
                order.getRequestNote(),
                itemDtos,
                order.getTotalPrice()
        );
    }
}