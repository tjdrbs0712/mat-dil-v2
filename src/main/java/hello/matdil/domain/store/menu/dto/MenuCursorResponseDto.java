package hello.matdil.domain.store.menu.dto;

public record MenuCursorResponseDto(
        int size,
        Integer lastOrderIndex,
        Long lastMenuId
) {
}
