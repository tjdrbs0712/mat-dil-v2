package hello.matdil.domain.favorite.dto;

import hello.matdil.domain.favorite.entity.FavoriteSortType;

public record FavoriteCursorResponseDto(
        Integer size,
        Object lastValue,
        Long lastStoreId
) {
    public static FavoriteCursorResponseDto from(FavoriteStoreSummaryDto dto, FavoriteSortType sortType, int size) {
        Object lastValue = null;

        switch (sortType) {
            case LATEST_ORDER -> lastValue = dto.getLastOrderedAt();
            case MOST_ORDERED -> lastValue = dto.getOrderCount();
        }

        return new FavoriteCursorResponseDto(
                size,
                lastValue,
                dto.getStoreId()
        );
    }
}
