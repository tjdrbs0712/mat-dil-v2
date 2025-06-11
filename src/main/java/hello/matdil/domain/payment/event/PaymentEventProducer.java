package hello.matdil.domain.payment.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private static final String TOPIC_NAME = "payment-completed-topic";

    private final KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;

    public void sendPaymentCompletedEvent(PaymentCompletedEvent event) {
        String key = event.orderId().toString();
        kafkaTemplate.send(TOPIC_NAME, key, event);
    }
}
