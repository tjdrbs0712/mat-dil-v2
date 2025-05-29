package hello.matdil.domain.userorderstatus.service;

import hello.matdil.domain.userorderstatus.dto.UserOrderStatusSyncContextDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static hello.matdil.global.constant.RedisUserOrderStatsKeys.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserOrderStatusCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public Set<String> getAllStatsKeys() {
        return redisTemplate.keys(KEY_PREFIX + ":*");
    }

    public Optional<UserOrderStatusSyncContextDto> buildSyncContext(String key) {
        try {
            Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
            if (map.isEmpty()) return Optional.empty();

            String[] parts = key.split(":");
            Long userId = Long.parseLong(parts[1]);
            Long storeId = Long.parseLong(parts[2]);

            int delta = Integer.parseInt(map.get(FIELD_ORDER_COUNT).toString());
            LocalDateTime lastOrderedAt = LocalDateTime.parse(map.get(FIELD_LAST_ORDERED_AT).toString());

            return Optional.of(new UserOrderStatusSyncContextDto(userId, storeId, delta, lastOrderedAt, key));
        } catch (Exception e) {
            log.warn("Redis 통계 키 파싱 실패: key={}, error={}", key, e.getMessage());
            return Optional.empty();
        }
    }
}