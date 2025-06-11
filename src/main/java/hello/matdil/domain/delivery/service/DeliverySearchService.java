package hello.matdil.domain.delivery.service;

import hello.matdil.domain.delivery.dto.DeliveryCacheDto;
import hello.matdil.domain.delivery.dto.DeliverySearchResponseDto;
import hello.matdil.domain.delivery.entity.Delivery;
import hello.matdil.domain.delivery.repository.DeliveryRepository;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.reader.OrderReader;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.domain.store.reader.StoreSummaryLoader;
import hello.matdil.infrastructure.redis.RedisGeoHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliverySearchService {

    private final RedisGeoHelper geoHelper;
    private final DeliveryCacheService deliveryCacheService;
    private final DeliveryRepository deliveryRepository;
    private final OrderReader orderReader;
    private final StoreSummaryLoader storeSummaryLoader;

    private static final String DELIVERY_GEO_KEY = "deliveries:ready";

    @Transactional(readOnly = true)
    public List<DeliverySearchResponseDto> findNearbyDeliveries(double latitude, double longitude, double radiusKm) {
        List<Long> deliveryIds = getNearbyDeliveryIds(latitude, longitude, radiusKm);
        if (deliveryIds.isEmpty()) {
            return List.of();
        }
        Map<Long, DeliveryCacheDto> cachedDeliveries = deliveryCacheService.getBatch(deliveryIds);
        List<Long> missingIds = deliveryIds.stream()
                .filter(id -> !cachedDeliveries.containsKey(id))
                .toList();
        if (!missingIds.isEmpty()) {
            List<Delivery> deliveriesFromDb = deliveryRepository.findAllById(missingIds);
            deliveryCacheService.putBatch(deliveriesFromDb);
            // DB에서 읽은 데이터를 캐시 DTO 맵에 추가하여 데이터 일관성 유지
            deliveriesFromDb.forEach(d -> cachedDeliveries.put(d.getId(), DeliveryCacheDto.from(d)));
        }
        List<Long> orderIds = cachedDeliveries.values().stream()
                .map(DeliveryCacheDto::orderId)
                .distinct()
                .toList();

        Map<Long, Order> ordersMap = orderReader.findAllIn(orderIds).stream()
                .collect(Collectors.toMap(Order::getId, Function.identity()));

        List<Long> storeIds = ordersMap.values().stream()
                .map(Order::getStoreId)
                .distinct()
                .toList();

        Map<Long, StoreSummaryResponseDto> storeSummariesMap = storeSummaryLoader.loadWithCacheFallback(storeIds);

        return deliveryIds.stream()
                .map(deliveryId -> {
                    DeliveryCacheDto deliveryCache = cachedDeliveries.get(deliveryId);
                    if (deliveryCache == null) return null;

                    Order order = ordersMap.get(deliveryCache.orderId());
                    if (order == null) return null;

                    StoreSummaryResponseDto storeSummary = storeSummariesMap.get(order.getStoreId());
                    if (storeSummary == null) return null;

                    return DeliverySearchResponseDto.fromCache(deliveryCache, storeSummary);
                })
                .filter(Objects::nonNull)
                .toList();
    }

    // Redis GEO 검색 로직을 별도 메서드로 분리
    private List<Long> getNearbyDeliveryIds(double latitude, double longitude, double radiusKm) {
        Point riderLocation = new Point(longitude, latitude);
        Distance radius = new Distance(radiusKm, Metrics.KILOMETERS);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance().sortAscending().limit(50);
        return geoHelper.geoRadius(DELIVERY_GEO_KEY, riderLocation, radius, args)
                .stream().map(Long::parseLong).toList();
    }
}