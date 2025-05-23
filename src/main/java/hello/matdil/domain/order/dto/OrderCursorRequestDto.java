package hello.matdil.domain.order.dto;

import hello.matdil.global.util.CursorUtils;

import java.time.LocalDateTime;

public record OrderCursorRequestDto(
        Integer size,
        LocalDateTime lastCreatedAt,
        Long lastOrderId
) {
    public int pageSize() {
        return CursorUtils.safePageSize(size, 10, 100);
    }

    public boolean hasCursor() {
        return lastCreatedAt != null && lastOrderId != null;
    }
}