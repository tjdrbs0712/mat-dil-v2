package hello.matdil.domain.userorderstatus.service;

import hello.matdil.domain.userorderstatus.cache.UserOrderStatsCache;
import hello.matdil.domain.userorderstatus.repository.UserOrderStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserOrderStatsServiceImpl implements UserOrderStatsService{

    private final UserOrderStatsCache userOrderStatsCache;

    @Transactional
    public void increaseOrderCount(Long userId, Long storeId, LocalDateTime orderedAt) {
        userOrderStatsCache.increaseOrderCount(userId, storeId, orderedAt);
    }
}
