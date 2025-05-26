package hello.matdil.domain.order.event;

import hello.matdil.domain.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void sendOrderCreatedEvent(Order order) {
        kafkaTemplate.send("order-created", order.getUserId().toString(), OrderCreatedEvent.from(order));
    }
}

