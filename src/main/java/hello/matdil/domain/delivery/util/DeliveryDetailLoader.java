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

    public Map<Long, DeliverySearchResponseDto> loadByDeliveryIds(List<Long> deliveryIds) {
        Map<Long, DeliveryCacheDto> deliveriesMap = loadDeliveryDetails(deliveryIds);

        List<Long> orderIds = deliveriesMap.values().stream()
                .map(DeliveryCacheDto::orderId).distinct().toList();

        Map<Long, Order> ordersMap = orderReader.findAllIn(orderIds).stream()
                .collect(Collectors.toMap(Order::getId, Function.identity()));

        List<Long> storeIds = ordersMap.values().stream()
                .map(Order::getStoreId).distinct().toList();

        Map<Long, StoreSummaryResponseDto> storeSummariesMap = storeSummaryLoader.loadWithCacheFallback(storeIds);

        return deliveryIds.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        deliveryId -> {
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