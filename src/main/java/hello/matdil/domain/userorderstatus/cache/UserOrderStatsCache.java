package hello.matdil.domain.userorderstatus.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserOrderStatsCache {

    private final RedisTemplate<String, Object> redisTemplate;

    private String getKey(Long userId, Long storeId) {
        return "stats:" + userId + ":" + storeId;
    }

    public void increaseOrderCount(Long userId, Long storeId, LocalDateTime orderedAt) {
        String key = getKey(userId, storeId);
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        hashOps.increment(key, "orderCount", 1);
        hashOps.put(key, "lastOrderedAt", orderedAt.toString());
    }

    public Optional<Integer> getOrderCount(Long userId, Long storeId) {
        String key = getKey(userId, storeId);
        Object value = redisTemplate.opsForHash().get(key, "orderCount");
        if (value instanceof Integer intVal) return Optional.of(intVal);
        if (value instanceof Long longVal) return Optional.of(longVal.intValue());
        if (value instanceof String strVal) return Optional.of(Integer.parseInt(strVal));
        return Optional.empty();
    }

    public Optional<LocalDateTime> getLastOrderedAt(Long userId, Long storeId) {
        String key = getKey(userId, storeId);
        Object value = redisTemplate.opsForHash().get(key, "lastOrderedAt");
        if (value instanceof String strVal) {
            return Optional.of(LocalDateTime.parse(strVal));
        }
        return Optional.empty();
    }

    public void deleteStats(Long userId, Long storeId) {
        redisTemplate.delete(getKey(userId, storeId));
    }
}
