package hello.matdil.domain.userorderstatus.consumer;

import hello.matdil.domain.order.event.OrderCreatedEvent;
import hello.matdil.domain.userorderstatus.service.UserOrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserOrderStatusConsumer {

    private final UserOrderStatusService userOrderStatusService;

    @KafkaListener(topics = "order-topic")
    public void consume(OrderCreatedEvent event) {
        userOrderStatusService.increaseOrderCount(event.getUserId(), event.getStoreId(), event.getOrderedAt());
    }
}
