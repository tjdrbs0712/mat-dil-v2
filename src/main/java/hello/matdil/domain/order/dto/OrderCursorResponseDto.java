package hello.matdil.domain.order.dto;

import java.time.LocalDateTime;

public record OrderCursorResponseDto(
        int size,
        LocalDateTime lastCreatedAt,
        Long lastOrderId
) {}