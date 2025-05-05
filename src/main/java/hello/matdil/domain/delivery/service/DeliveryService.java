package hello.matdil.domain.delivery.service;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.delivery.entity.Delivery;
import hello.matdil.domain.delivery.entity.DeliveryStatus;
import hello.matdil.domain.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public void createDelivery(Long orderId, Address address) {
        Delivery delivery = Delivery.builder()
                .orderId(orderId)
                .address(address)
                .deliveryStatus(DeliveryStatus.READY)
                .build();

        deliveryRepository.save(delivery);
    }

}
