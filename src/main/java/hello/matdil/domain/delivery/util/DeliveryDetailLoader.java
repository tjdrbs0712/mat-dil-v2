package hello.matdil.domain.delivery.util;

import hello.matdil.domain.delivery.dto.DeliveryCacheDto;
import hello.matdil.domain.delivery.dto.DeliverySearchResponseDto;
import hello.matdil.domain.delivery.entity.Delivery;
import hello.matdil.domain.delivery.repository.DeliveryRepository;
import hello.matdil.domain.delivery.service.DeliveryCacheService;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.reader.OrderReader;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.domain.store.reader.StoreSummaryLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DeliveryDetailLoader {

    private final DeliveryCacheService deliveryCacheService;
    private final DeliveryRepository deliveryRepository;
    private final OrderReader orderReader;
    private final StoreSummaryLoader storeSummaryLoader;

    /**
     * 배달 ID 목록을 받아, 상세 정보를 조합하여 DTO 맵을 반환합니다.
     */
    public Map<Long, DeliverySearchResponseDto> loadByDeliveryIds(List<Long> deliveryIds) {
        // 1. Cache-Aside 패턴으로 Delivery 상세 정보 조회
        Map<Long, DeliveryCacheDto> deliveriesMap = loadDeliveryDetails(deliveryIds);

        // 2. Delivery 정보에서 orderId 목록 추출
        List<Long> orderIds = deliveriesMap.values().stream()
                .map(DeliveryCacheDto::orderId).distinct().toList();

        // 3. Order 정보 조회
        Map<Long, Order> ordersMap = orderReader.findAllIn(orderIds).stream()
                .collect(Collectors.toMap(Order::getId, Function.identity()));

        // 4. Order 정보에서 storeId 목록 추출
        List<Long> storeIds = ordersMap.values().stream()
                .map(Order::getStoreId).distinct().toList();

        // 5. StoreSummaryLoader를 사용하여 Store 정보 조회
        Map<Long, StoreSummaryResponseDto> storeSummariesMap = storeSummaryLoader.loadWithCacheFallback(storeIds);

        // 6. 모든 정보를 조합하여 최종 DTO 맵 생성
        return deliveryIds.stream()
                .collect(Collectors.toMap(
                        Function.identity(), // key는 deliveryId
                        deliveryId -> { // value는 조합된 DTO
                            DeliveryCacheDto deliveryCache = deliveriesMap.get(deliveryId);
                            if (deliveryCache == null) return null;

                            Order order = ordersMap.get(deliveryCache.orderId());
                            if (order == null) return null;

                            StoreSummaryResponseDto storeSummary = storeSummariesMap.get(order.getStoreId());
                            if (storeSummary == null) return null;

                            return DeliverySearchResponseDto.fromCache(deliveryCache, storeSummary);
                        }
                ));
    }

    private Map<Long, DeliveryCacheDto> loadDeliveryDetails(List<Long> deliveryIds) {
        Map<Long, DeliveryCacheDto> cachedDeliveries = deliveryCacheService.getBatch(deliveryIds);
        List<Long> missingIds = deliveryIds.stream()
                .filter(id -> !cachedDeliveries.containsKey(id))
                .toList();

        if (!missingIds.isEmpty()) {
            List<Delivery> deliveriesFromDb = deliveryRepository.findAllById(missingIds);
            deliveryCacheService.putBatch(deliveriesFromDb);
            deliveriesFromDb.forEach(d -> cachedDeliveries.put(d.getId(), DeliveryCacheDto.from(d)));
        }
        return cachedDeliveries;
    }
}