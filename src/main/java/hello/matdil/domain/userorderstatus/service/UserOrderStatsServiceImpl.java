package hello.matdil.domain.userorderstatus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserOrderStatsServiceImpl implements UserOrderStatsService{

    private final UserOrderStatsRedisService userOrderStatsRedisService;

    @Transactional
    public void increaseOrderCount(Long userId, Long storeId, LocalDateTime orderedAt) {
        userOrderStatsRedisService.increaseOrderCount(userId, storeId, orderedAt);
    }
}
