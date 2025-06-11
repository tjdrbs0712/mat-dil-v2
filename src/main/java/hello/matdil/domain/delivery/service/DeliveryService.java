package hello.matdil.domain.delivery.service;

import hello.matdil.domain.delivery.entity.Delivery;
import hello.matdil.domain.delivery.entity.DeliveryStatus;
import hello.matdil.domain.delivery.event.DeliveryCreatedEvent;
import hello.matdil.domain.delivery.event.DeliveryEventProducer;
import hello.matdil.domain.delivery.repository.DeliveryRepository;
import hello.matdil.domain.order.event.OrderReadyForDispatchEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryEventProducer deliveryEventProducer;

    @KafkaListener(topics = "order-ready-for-dispatch", groupId = "delivery-service-group")
    @Transactional
    public void createDelivery(OrderReadyForDispatchEvent event) {
        if (deliveryRepository.existsByOrderId(event.orderId())) {
            log.warn("이미 배달이 생성된 주문입니다. orderId={}", event.orderId());
            return;
        }

        Delivery newDelivery = Delivery.create(
                event.orderId(),
                event.address(),
                event.deliveryFee(),
                DeliveryStatus.READY
        );

        Delivery savedDelivery = deliveryRepository.save(newDelivery);
        deliveryEventProducer.sendDeliveryCreatedEvent(DeliveryCreatedEvent.from(savedDelivery));


    }

}
