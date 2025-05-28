package hello.matdil.domain.userorderstatus.scheduler;

import hello.matdil.domain.userorderstatus.service.UserOrderStatusCacheService;
import hello.matdil.domain.userorderstatus.service.UserOrderStatusSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserOrderStatsSyncScheduler {

    private final UserOrderStatusCacheService statsCacheService;
    private final UserOrderStatusSyncService statsSyncService;

    @Scheduled(fixedDelay = 300 * 1000)
    @Transactional
    public void syncRedisToDb() {
        statsCacheService.getAllStatsKeys().stream()
                .map(statsCacheService::buildSyncContext)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(statsSyncService::syncToDatabase);
    }
}
