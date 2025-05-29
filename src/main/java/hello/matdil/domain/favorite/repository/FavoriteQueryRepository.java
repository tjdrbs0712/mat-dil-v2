package hello.matdil.domain.favorite.repository;

import hello.matdil.domain.favorite.dto.FavoriteCursorRequestDto;
import hello.matdil.domain.favorite.dto.FavoriteStoreSummaryDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteQueryRepository {
    List<FavoriteStoreSummaryDto> loadFavoriteStoreSummaries(Long userId, FavoriteCursorRequestDto dto);
}
