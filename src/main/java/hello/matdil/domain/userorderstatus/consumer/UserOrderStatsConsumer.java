package hello.matdil.domain.userorderstatus.consumer;

import hello.matdil.domain.order.event.OrderCreatedEvent;
import hello.matdil.domain.userorderstatus.service.UserOrderStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserOrderStatsConsumer {

    private final UserOrderStatsService userOrderStatsService;

    @KafkaListener(topics = "order.created", groupId = "user-order-stats-group")
    public void consume(OrderCreatedEvent event) {
        userOrderStatsService.increaseOrderCount(event.getUserId(), event.getStoreId(), event.getOrderedAt());
    }
}
