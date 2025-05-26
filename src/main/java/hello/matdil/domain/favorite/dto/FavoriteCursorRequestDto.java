package hello.matdil.domain.favorite.dto;

import hello.matdil.domain.favorite.entity.FavoriteSortType;
import hello.matdil.global.util.CursorUtils;
import hello.matdil.global.validator.EnumValid;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public record FavoriteCursorRequestDto(
        @EnumValid(message = "정렬 기준을 제대로 입력해주세요.", enumClass = FavoriteSortType.class)
        String sort,

        Integer size,

        // 최신 주문순 정렬
        LocalDateTime lastOrderedAt,

        // 주문 많은 순 정렬
        Integer lastOrderCount,

        // 공통 커서 필드 (tie breaker)
        Long lastStoreId
) {

    public Map<String, Object> toCursorParamMap() {
        Map<String, Object> cursor = new HashMap<>();
        if (lastOrderedAt != null) cursor.put("lastOrderedAt", lastOrderedAt);
        if (lastOrderCount != null) cursor.put("lastOrderCount", lastOrderCount);
        if (lastStoreId != null) cursor.put("lastStoreId", lastStoreId);
        return cursor;
    }

    public int pageSize() {
        return CursorUtils.safePageSize(size, 10, 100);
    }

//    public boolean hasCursor() {
//        return lastOrderIndex != null && lastMenuId != null;
//    }

    public FavoriteSortType toEnum() {
        return FavoriteSortType.valueOf(sort.toUpperCase());
    }
}