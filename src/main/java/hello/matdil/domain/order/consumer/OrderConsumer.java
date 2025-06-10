package hello.matdil.domain.order.consumer;

import hello.matdil.domain.order.service.OrderService;
import hello.matdil.domain.payment.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "payment-completed-topic", groupId = "order-service-group")
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        orderService.updateOrderStatusToPaid(event.orderId());
    }

}
