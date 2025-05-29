package hello.matdil.domain.favorite.service;

import hello.matdil.domain.favorite.dto.FavoriteCursorRequestDto;
import hello.matdil.domain.favorite.dto.FavoriteStoreSummaryDto;
import hello.matdil.domain.favorite.entity.FavoriteSortType;
import hello.matdil.domain.favorite.repository.FavoriteRepository;
import hello.matdil.infrastructure.RedisListJsonCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteCacheServiceTest {

    @Mock
    private RedisListJsonCache redisCache;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteCacheService favoriteCacheService;

    private final Long userId = 1L;
    private final FavoriteCursorRequestDto dto = new FavoriteCursorRequestDto(
            FavoriteSortType.LATEST_ORDER.toString(), 10, null, null, null);

    @Test
    void 캐시에_데이터가_있을_경우() {
        List<FavoriteStoreSummaryDto> cachedList = List.of(mock(FavoriteStoreSummaryDto.class));
        given(redisCache.getList(anyString(), eq(FavoriteStoreSummaryDto.class)))
                .willReturn(Optional.of(cachedList));

        List<FavoriteStoreSummaryDto> result = favoriteCacheService.getFavoriteStores(userId, dto);

        assertThat(result).isEqualTo(cachedList);
        verify(favoriteRepository, never()).loadFavoriteStoreSummaries(any(), any());
    }

    @Test
    void 캐시에_데이터가_없을_경우() {
        List<FavoriteStoreSummaryDto> dbResult = List.of(mock(FavoriteStoreSummaryDto.class));
        given(redisCache.getList(anyString(), eq(FavoriteStoreSummaryDto.class))).willReturn(Optional.empty());
        given(favoriteRepository.loadFavoriteStoreSummaries(userId, dto)).willReturn(dbResult);

        List<FavoriteStoreSummaryDto> result = favoriteCacheService.getFavoriteStores(userId, dto);

        assertThat(result).isEqualTo(dbResult);
        verify(redisCache).putList(anyString(), eq(dbResult), any());
    }
}
