package hello.matdil.domain.order.service;

import hello.matdil.domain.order.dto.cache.OrderCacheDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
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
public class OrderCacheService {

    private final RedisDtoCacheHelper redisDtoCacheHelper;

    private static final String ORDER_KEY_PREFIX = "order:";

    private static final Duration TTL = Duration.ofMinutes(5);

    private String getKey(Long orderId) {
        return ORDER_KEY_PREFIX + orderId;
    }

    public Optional<OrderCacheDto> get(Long orderId) {
        return redisDtoCacheHelper.get(getKey(orderId), OrderCacheDto.class);
    }

    public void put(Order order, StoreSummaryResponseDto storeInfo) {
        OrderCacheDto cacheRecord = OrderCacheDto.from(order, storeInfo);
        redisDtoCacheHelper.put(getKey(order.getId()), cacheRecord, TTL);
    }

    public void evict(Long orderId) {
        redisDtoCacheHelper.delete(getKey(orderId));
    }

    public Map<Long, OrderCacheDto> getBatch(List<Long> orderIds) {
        List<String> keys = orderIds.stream()
                .map(this::getKey)
                .toList();

        Map<String, OrderCacheDto> resultFromCache = redisDtoCacheHelper.multiGet(keys, OrderCacheDto.class);

        return resultFromCache.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> Long.parseLong(entry.getKey().replace(ORDER_KEY_PREFIX, "")),
                        Map.Entry::getValue
                ));
    }
}
