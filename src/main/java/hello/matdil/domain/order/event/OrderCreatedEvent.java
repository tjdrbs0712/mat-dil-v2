package hello.matdil.domain.order.event;

import hello.matdil.domain.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long userId;
    private Long storeId;
    private LocalDateTime orderedAt;

    public static OrderCreatedEvent from(Order order) {
        return new OrderCreatedEvent(order.getUserId(), order.getStoreId(), order.getCreatedAt());
    }
}
