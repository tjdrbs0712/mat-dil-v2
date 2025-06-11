package hello.matdil.domain.delivery.facade;

import hello.matdil.domain.delivery.dto.DeliverySearchResponseDto;
import hello.matdil.domain.delivery.service.DeliveryGeoService;
import hello.matdil.domain.delivery.util.DeliveryDetailLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor

public class DeliverySearchFacade {

    private final DeliveryGeoService deliveryGeoService;
    private final DeliveryDetailLoader deliveryDetailLoader;

    @Transactional(readOnly = true)
    public List<DeliverySearchResponseDto> getDeliveries(double latitude, double longitude, double radiusKm) {
        Point riderLocation = new Point(longitude, latitude);
        Distance radius = new Distance(radiusKm, Metrics.KILOMETERS);
        List<Long> deliveryIds = deliveryGeoService.findNearbyDeliveryIds(riderLocation, radius);

        if (deliveryIds.isEmpty()) {
            return List.of();
        }

        Map<Long, DeliverySearchResponseDto> detailsMap = deliveryDetailLoader.loadByDeliveryIds(deliveryIds);

        return deliveryIds.stream()
                .map(detailsMap::get)
                .filter(Objects::nonNull)
                .toList();
    }
}
