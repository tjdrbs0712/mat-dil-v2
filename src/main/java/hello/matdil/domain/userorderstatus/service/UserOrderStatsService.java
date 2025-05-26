package hello.matdil.domain.userorderstatus.service;

import java.time.LocalDateTime;

public interface UserOrderStatsService {
    void increaseOrderCount(Long userId, Long storeId, LocalDateTime orderedAt);
}
