package hello.matdil.domain.userorderstatus.service;

import hello.matdil.domain.userorderstatus.entity.UserOrderStats;
import hello.matdil.domain.userorderstatus.repository.UserOrderStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserOrderStatsServiceImpl implements UserOrderStatsService{

    private final UserOrderStatsRepository statsRepository;

    @Transactional
    public void increaseOrderCount(Long userId, Long storeId, LocalDateTime orderedAt) {
        UserOrderStats stats = statsRepository.findByUserIdAndStoreId(userId, storeId)
                .orElseGet(() -> UserOrderStats.create(userId, storeId, orderedAt));

        stats.incrementOrderCount();

        statsRepository.save(stats);
    }
}
