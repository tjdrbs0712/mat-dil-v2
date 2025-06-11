package hello.matdil.domain.delivery.service;

import hello.matdil.domain.delivery.dto.DeliveryCacheDto;
import hello.matdil.domain.delivery.entity.Delivery;
import hello.matdil.infrastructure.redis.RedisDtoCacheHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryCacheService {

    private final RedisDtoCacheHelper redisDtoCacheHelper;

    private static final String KEY_PREFIX = "delivery:detail:";
    private static final Duration TTL = Duration.ofMinutes(30);

    private String getKey(Long deliveryId) {
        return KEY_PREFIX + deliveryId;
    }

    public void put(Delivery delivery) {
        if (delivery == null) return;
        DeliveryCacheDto dto = DeliveryCacheDto.from(delivery);
        redisDtoCacheHelper.put(getKey(delivery.getId()), dto, TTL);
    }

    public void putBatch(List<Delivery> deliveries) {
        if (deliveries == null || deliveries.isEmpty()) return;

        Map<String, Object> dataToCache = deliveries.stream()
                .collect(Collectors.toMap(
                        delivery -> getKey(delivery.getId()),
                        DeliveryCacheDto::from
                ));

        redisDtoCacheHelper.multiSet(dataToCache, TTL);
    }

    public Optional<DeliveryCacheDto> get(Long deliveryId) {
        if (deliveryId == null) return Optional.empty();
        return redisDtoCacheHelper.get(getKey(deliveryId), DeliveryCacheDto.class);
    }

    public Map<Long, DeliveryCacheDto> getBatch(List<Long> deliveryIds) {
        if (deliveryIds == null || deliveryIds.isEmpty()) return Map.of();

        List<String> keys = deliveryIds.stream().map(this::getKey).toList();
        Map<String, DeliveryCacheDto> resultFromCache = redisDtoCacheHelper.multiGet(keys, DeliveryCacheDto.class);

        return resultFromCache.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> Long.parseLong(entry.getKey().replace(KEY_PREFIX, "")),
                        Map.Entry::getValue
                ));
    }

    public void evict(Long deliveryId) {
        if (deliveryId == null) return;
        redisDtoCacheHelper.delete(getKey(deliveryId));
    }
}