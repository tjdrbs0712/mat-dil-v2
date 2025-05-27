package hello.matdil.domain.userorderstatus.scheduler;

import hello.matdil.domain.userorderstatus.entity.UserOrderStats;
import hello.matdil.domain.userorderstatus.repository.UserOrderStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserOrderStatsSyncScheduler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserOrderStatsRepository statsRepository;

    @Scheduled(fixedDelay = 300000) // 5분마다 실행
    @Transactional
    public void syncRedisToDb() {
        Set<String> keys = redisTemplate.keys("stats:*");
        if (keys.isEmpty()) {
            log.info("동기화할 통계 캐시 없음");
            return;
        }

        for (String key : keys) {
            try {
                Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
                if (map.isEmpty()) continue;

                Long userId = Long.parseLong(key.split(":")[1]);
                Long storeId = Long.parseLong(key.split(":")[2]);

                int orderCount = Integer.parseInt(map.get("orderCount").toString());
                LocalDateTime lastOrderedAt = LocalDateTime.parse(map.get("lastOrderedAt").toString());

                UserOrderStats stats = statsRepository.findByUserIdAndStoreId(userId, storeId)
                        .orElseGet(() -> UserOrderStats.create(userId, storeId, lastOrderedAt));

                int updatedCount = stats.getOrderCount() + orderCount;
                stats.updateOrderCount(updatedCount);
                stats.updateOrderedAt(lastOrderedAt);

                statsRepository.save(stats);
                log.info("UserOrderStats 동기화 완료: userId={}, storeId={}, count={}", userId, storeId, orderCount);

                redisTemplate.opsForHash().put(key, "orderCount", 0);

            } catch (Exception e) {
                log.error("통계 동기화 실패: key={}, error={}", key, e.getMessage(), e);
            }
        }
    }
}