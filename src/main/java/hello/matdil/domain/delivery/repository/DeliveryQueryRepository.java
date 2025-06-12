package hello.matdil.domain.delivery.repository;

import hello.matdil.domain.delivery.entity.Delivery;

import java.util.Optional;

public interface DeliveryQueryRepository {
    Optional<Delivery> findAndLockById(Long deliveryId);
}
