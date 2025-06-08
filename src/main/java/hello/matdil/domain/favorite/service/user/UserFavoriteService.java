package hello.matdil.domain.favorite.service.user;

import hello.matdil.domain.favorite.dto.FavoriteCursorRequestDto;
import hello.matdil.domain.favorite.dto.FavoriteCursorResponseDto;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.global.response.SliceResponse;

public interface UserFavoriteService {

    void addFavorite(Long userId, Long storeId);

    void removeFavorite(Long userId, Long storeId);

    SliceResponse<StoreSummaryResponseDto, FavoriteCursorResponseDto> getFavoriteStores(
            Long userId, FavoriteCursorRequestDto request);
}
