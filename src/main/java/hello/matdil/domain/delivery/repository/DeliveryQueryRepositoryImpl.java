package hello.matdil.domain.delivery.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.matdil.domain.delivery.entity.Delivery;
import hello.matdil.domain.delivery.entity.QDelivery;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeliveryQueryRepositoryImpl implements DeliveryQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Delivery> findAndLockById(Long deliveryId) {
        QDelivery delivery = QDelivery.delivery;

        Delivery result = queryFactory
                .selectFrom(delivery)
                .where(delivery.id.eq(deliveryId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
