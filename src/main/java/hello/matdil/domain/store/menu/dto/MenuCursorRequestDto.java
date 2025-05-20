package hello.matdil.domain.store.menu.dto;

import hello.matdil.global.util.CursorUtils;

public record MenuCursorRequestDto(
        Integer size,
        Integer lastOrderIndex,
        Long lastMenuId
) {
    public int pageSize() {
        return CursorUtils.safePageSize(size, 10, 100);
    }

    public boolean hasCursor() {
        return lastOrderIndex != null && lastMenuId != null;
    }
}