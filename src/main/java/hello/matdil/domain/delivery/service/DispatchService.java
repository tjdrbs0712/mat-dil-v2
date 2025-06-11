package hello.matdil.domain.delivery.service;

import hello.matdil.domain.delivery.event.DeliveryCreatedEvent;
import hello.matdil.infrastructure.redis.RedisGeoHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DispatchService {
    private final RedisGeoHelper geoHelper;
    private static final String DELIVERY_GEO_KEY = "deliveries:ready";

    @KafkaListener(topics = "delivery-created-topic", groupId = "delivery-service-group")
    public void addDeliveryToGeoIndex(DeliveryCreatedEvent event) {
        Point pickupLocation = new Point(event.address().getLongitude(), event.address().getLatitude());
        String deliveryId = event.deliveryId().toString();

        geoHelper.geoAdd(DELIVERY_GEO_KEY, pickupLocation, deliveryId);
    }
}