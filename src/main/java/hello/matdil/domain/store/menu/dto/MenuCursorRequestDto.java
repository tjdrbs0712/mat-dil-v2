package hello.matdil.domain.store.menu.dto;

public record MenuCursorRequestDto(
        Integer size,
        Integer lastOrderIndex,
        Long lastMenuId
) {
    public int pageSize() {
        return (size != null && size > 0) ? Math.min(size, 100) : 5;
    }

    public boolean hasCursor() {
        return lastOrderIndex != null && lastMenuId != null;
    }
}