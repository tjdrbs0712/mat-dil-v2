package hello.matdil.domain.userorderstatus.service;

import hello.matdil.domain.userorderstatus.dto.UserOrderStatusSyncContextDto;
import hello.matdil.domain.userorderstatus.entity.UserOrderStatus;
import hello.matdil.domain.userorderstatus.repository.UserOrderStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static hello.matdil.global.constant.RedisUserOrderStatsKeys.FIELD_ORDER_COUNT;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserOrderStatusSyncService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserOrderStatusRepository statusRepository;

    public void syncToDatabase(UserOrderStatusSyncContextDto dto) {
        try {
            UserOrderStatus stats = statusRepository.findByUserIdAndStoreId(dto.userId(), dto.storeId())
                    .orElseGet(() -> UserOrderStatus.create(dto.userId(), dto.storeId(), dto.lastOrderedAt()));

            int updatedCount = stats.getOrderCount() + dto.orderDelta();
            stats.updateOrderCount(updatedCount);
            stats.updateOrderedAt(dto.lastOrderedAt());

            statusRepository.save(stats);
            redisTemplate.opsForHash().put(dto.redisKey(), FIELD_ORDER_COUNT, 0);

            log.info("동기화 완료: userId={}, storeId={}, 누적 count={}", dto.userId(), dto.storeId(), updatedCount);
        } catch (Exception e) {
            log.error("DB 저장 실패: userId={}, storeId={}, error={}", dto.userId(), dto.storeId(), e.getMessage(), e);
        }
    }
}