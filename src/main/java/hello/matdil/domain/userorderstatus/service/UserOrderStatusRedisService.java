package hello.matdil.domain.userorderstatus.service;

import hello.matdil.domain.userorderstatus.dto.UserOrderStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static hello.matdil.global.constant.RedisUserOrderStatsKeys.*;

@Service
@RequiredArgsConstructor
public class UserOrderStatusRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private String getKey(Long userId, Long storeId) {
        return KEY_PREFIX + ":" + userId + ":" + storeId;
    }

    public void increaseOrderCount(Long userId, Long storeId, LocalDateTime orderedAt) {
        String key = getKey(userId, storeId);
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        hashOps.increment(key, FIELD_ORDER_COUNT, 1);
        hashOps.put(key, FIELD_LAST_ORDERED_AT, orderedAt.toString());

        redisTemplate.expire(key, Duration.ofDays(1));
    }

    public Optional<UserOrderStatusDto> getStats(Long userId, Long storeId) {
        String key = getKey(userId, storeId);
        Map<Object, Object> hash = redisTemplate.opsForHash().entries(key);

        if (hash.isEmpty()) return Optional.empty();

        try {
            return Optional.of(UserOrderStatusDto.from(hash));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void deleteStats(Long userId, Long storeId) {
        redisTemplate.delete(getKey(userId, storeId));
    }
}
