package hello.matdil.domain.delivery.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryEventProducer {

    private static final String TOPIC = "delivery-created-topic";
    private final KafkaTemplate<String, DeliveryCreatedEvent> kafkaTemplate;

    public void sendDeliveryCreatedEvent(DeliveryCreatedEvent event) {
        String key = event.deliveryId().toString();
        kafkaTemplate.send(TOPIC, key, event);
    }
}