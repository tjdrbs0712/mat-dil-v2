package hello.matdil.domain.userorderstatus.service;

import java.time.LocalDateTime;

public interface UserOrderStatusService {
    void increaseOrderCount(Long userId, Long storeId, LocalDateTime orderedAt);
}
