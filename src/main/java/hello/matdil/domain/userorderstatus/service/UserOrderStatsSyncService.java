package hello.matdil.domain.userorderstatus.service;

import hello.matdil.domain.userorderstatus.dto.UserOrderStatsSyncContextDto;
import hello.matdil.domain.userorderstatus.entity.UserOrderStats;
import hello.matdil.domain.userorderstatus.repository.UserOrderStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static hello.matdil.global.constant.RedisUserOrderStatsKeys.FIELD_ORDER_COUNT;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserOrderStatsSyncService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserOrderStatsRepository statsRepository;

    public void syncToDatabase(UserOrderStatsSyncContextDto ctx) {
        try {
            UserOrderStats stats = statsRepository.findByUserIdAndStoreId(ctx.userId(), ctx.storeId())
                    .orElseGet(() -> UserOrderStats.create(ctx.userId(), ctx.storeId(), ctx.lastOrderedAt()));

            int updatedCount = stats.getOrderCount() + ctx.orderDelta();
            stats.updateOrderCount(updatedCount);
            stats.updateOrderedAt(ctx.lastOrderedAt());

            statsRepository.save(stats);
            redisTemplate.opsForHash().put(ctx.redisKey(), FIELD_ORDER_COUNT, 0);

            log.info("동기화 완료: userId={}, storeId={}, 누적 count={}", ctx.userId(), ctx.storeId(), updatedCount);
        } catch (Exception e) {
            log.error("DB 저장 실패: userId={}, storeId={}, error={}", ctx.userId(), ctx.storeId(), e.getMessage(), e);
        }
    }
}