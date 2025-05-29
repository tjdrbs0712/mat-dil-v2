package hello.matdil.domain.favorite.service;

import hello.matdil.domain.favorite.dto.FavoriteCursorRequestDto;
import hello.matdil.domain.favorite.dto.FavoriteCursorResponseDto;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;

public interface FavoriteService {

    void addFavorite(Long userId, UserRole role, Long storeId);

    void removeFavorite(Long userId, UserRole role, Long storeId);

    SliceResponse<StoreSummaryResponseDto, FavoriteCursorResponseDto> getFavoriteStores(
            Long userId, UserRole role, FavoriteCursorRequestDto request);
}
