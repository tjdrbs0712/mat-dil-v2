package hello.matdil.domain.delivery.repository;

import hello.matdil.domain.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long>, DeliveryQueryRepository{
    Boolean existsByOrderId(Long OrderId);
}
