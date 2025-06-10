package hello.matdil.domain.delivery.service;

import hello.matdil.domain.delivery.entity.Delivery;
import hello.matdil.domain.delivery.entity.DeliveryStatus;
import hello.matdil.domain.delivery.exception.DeliveryErrorCode;
import hello.matdil.domain.delivery.exception.DeliveryException;
import hello.matdil.domain.delivery.repository.DeliveryRepository;
import hello.matdil.domain.order.event.OrderReadyForDispatchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @KafkaListener(topics = "order-ready-for-dispatch", groupId = "delivery-service-group")
    @Transactional
    public void createDelivery(OrderReadyForDispatchEvent event) {
        if (deliveryRepository.existsByOrderId(event.orderId())) {
            throw new DeliveryException(DeliveryErrorCode.DELIVERY_ALREADY_EXISTS);
        }

        Delivery newDelivery = Delivery.create(
                event.orderId(),
                event.address(),
                event.deliveryFee(),
                DeliveryStatus.READY
        );

        Delivery savedDelivery = deliveryRepository.save(newDelivery);

        //배달 생성 이벤트 발행 (라이더 배차 시스템 연동)
    }

}
