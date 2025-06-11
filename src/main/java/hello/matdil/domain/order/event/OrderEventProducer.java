package hello.matdil.domain.order.event;

import hello.matdil.domain.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private static final String TOPIC_NAME = "order-topic";
    private static final String ORDER_READY = "order-ready-for-dispatch";

    private final KafkaTemplate<String, OrderCreatedEvent> orderCreatedEventKafkaTemplate;
    private final KafkaTemplate<String, OrderReadyForDispatchEvent> dispatchEventKafkaTemplate;


    public void sendOrderCreatedEvent(Order order) {
        String key = order.getId().toString();
        orderCreatedEventKafkaTemplate.send(TOPIC_NAME, key, OrderCreatedEvent.from(order));
    }

    public void sendOrderReadyForDispatch(Order order) {
        String key = order.getId().toString();
        OrderReadyForDispatchEvent event = OrderReadyForDispatchEvent.from(order);
        dispatchEventKafkaTemplate.send(ORDER_READY, key, event);
    }


}

