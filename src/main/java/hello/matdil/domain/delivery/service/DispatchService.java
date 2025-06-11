package hello.matdil.domain.delivery.service;

import hello.matdil.domain.delivery.event.DeliveryCreatedEvent;
import hello.matdil.infrastructure.RedisCacheHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DispatchService {
    private final RedisCacheHelper redisCacheHelper;
    private static final String DELIVERY_GEO_KEY = "deliveries:ready";

    @KafkaListener(topics = "delivery-created-topic", groupId = "delivery-service-group")
    public void addDeliveryToGeoIndex(DeliveryCreatedEvent event) {
        Point pickupLocation = new Point(event.getLongitude(), event.getLatitude());
        String deliveryId = event.getDeliveryId().toString();

        // ✨ RedisTemplate 대신 헬퍼 사용
        redisCacheHelper.geoAdd(DELIVERY_GEO_KEY, pickupLocation, deliveryId);
    }
}