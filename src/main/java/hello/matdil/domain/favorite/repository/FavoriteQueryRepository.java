package hello.matdil.domain.favorite.repository;

import hello.matdil.domain.favorite.dto.FavoriteCursorRequestDto;
import hello.matdil.domain.favorite.dto.FavoriteStoreSummaryDto;

import java.util.List;

public interface FavoriteQueryRepository {
    List<FavoriteStoreSummaryDto> loadFavoriteStoreSummaries(Long userId, FavoriteCursorRequestDto dto);
}
