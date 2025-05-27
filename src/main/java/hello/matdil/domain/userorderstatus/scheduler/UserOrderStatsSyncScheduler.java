package hello.matdil.domain.userorderstatus.scheduler;

import hello.matdil.domain.userorderstatus.service.UserOrderStatsCacheService;
import hello.matdil.domain.userorderstatus.service.UserOrderStatsSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserOrderStatsSyncScheduler {

    private final UserOrderStatsCacheService statsCacheService;
    private final UserOrderStatsSyncService statsSyncService;

    @Scheduled(fixedDelay = 300000) //5분
    @Transactional
    public void syncRedisToDb() {
        statsCacheService.getAllStatsKeys().stream()
                .map(statsCacheService::buildSyncContext)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(statsSyncService::syncToDatabase);
    }
}
