package hello.matdil.domain.userorderstatus.dto;

import java.time.LocalDateTime;

public record UserOrderStatusSyncContextDto(
        Long userId,
        Long storeId,
        int orderDelta,
        LocalDateTime lastOrderedAt,
        String redisKey
) {}