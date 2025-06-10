package hello.matdil.domain.order.event;

import hello.matdil.domain.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private static final String TOPIC_NAME = "order-topic";

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void sendOrderCreatedEvent(Order order) {
        String key = order.getId().toString();
        kafkaTemplate.send(TOPIC_NAME, key, OrderCreatedEvent.from(order));
    }
}

