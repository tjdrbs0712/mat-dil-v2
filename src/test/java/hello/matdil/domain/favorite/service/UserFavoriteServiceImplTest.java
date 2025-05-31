package hello.matdil.domain.favorite.service;

import hello.matdil.domain.favorite.dto.FavoriteCursorRequestDto;
import hello.matdil.domain.favorite.dto.FavoriteStoreSummaryDto;
import hello.matdil.domain.favorite.entity.FavoriteSortType;
import hello.matdil.domain.favorite.repository.FavoriteRepository;
import hello.matdil.domain.favorite.service.user.UserFavoriteServiceImpl;
import hello.matdil.domain.store.reader.StoreReader;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.util.pagination.PageAssembler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFavoriteServiceImplTest {

    @InjectMocks
    private UserFavoriteServiceImpl favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private FavoriteCacheService favoriteCacheService;

    @Mock
    private PageAssembler pageAssembler;

    @Mock
    private StoreReader storeReader;

    private final Long userId = 1L;
    private final UserRole role = UserRole.USER;
    private final FavoriteCursorRequestDto firstPageRequest =
            new FavoriteCursorRequestDto(null, 10, null, null, null);
    private final FavoriteCursorRequestDto nextPageRequest =
            new FavoriteCursorRequestDto(
                    FavoriteSortType.MOST_ORDERED.toString(),
                    10,
                    null,
                    1,
                    1L);

//    @Test
//    void 즐겨찾기_정상_등록() {
//        // given
//        Long userId = 1L;
//        Long storeId = 10L;
//        UserRole role = UserRole.USER;
//        Store store = mock(Store.class);
//
//        given(storeReader.readByIdWithPermission(userId, storeId, role)).willReturn(store);
//        given(favoriteRepository.existsByUserIdAndStoreId(userId, storeId)).willReturn(false);
//
//        // when
//        favoriteService.addFavorite(userId, role, storeId);
//
//        // then
//        verify(favoriteRepository).save(any(Favorite.class));
//    }
//

//    @Test
//    void 즐겨찾기_중복_예외() {
//        // given
//        Long storeId = 10L;
//        Store store = mock(Store.class);
//
//        given(storeReader.readByIdWithPermission(userId, storeId, role)).willReturn(store);
//        given(favoriteRepository.existsByUserIdAndStoreId(userId, storeId)).willReturn(true);
//
//        // when & then
//        assertThatThrownBy(() -> favoriteService.addFavorite(userId, role, storeId))
//                .isInstanceOf(FavoriteException.class)
//                .hasMessage(FavoriteErrorCode.ALREADY_FAVORITE.getErrorMessage());
//    }
//
//    @Test
//    void 즐겨찾기_정상_삭제() {
//        // given
//        Long storeId = 10L;
//        Favorite favorite = Favorite.create(userId, storeId);
//
//        given(favoriteRepository.findByUserIdAndStoreId(userId, storeId))
//                .willReturn(Optional.of(favorite));
//
//        // when
//        favoriteService.removeFavorite(userId, role, storeId);
//
//        // then
//        verify(favoriteRepository).delete(favorite);
//    }
//
//    @Test
//    void 즐겨찾기_없으면_예외() {
//        // given
//        Long userId = 1L;
//        Long storeId = 10L;
//        UserRole role = UserRole.USER;
//
//        given(favoriteRepository.findByUserIdAndStoreId(userId, storeId))
//                .willReturn(Optional.empty());
//
//        // when & then
//        assertThatThrownBy(() -> favoriteService.removeFavorite(userId, role, storeId))
//                .isInstanceOf(FavoriteException.class)
//                .hasMessage(FavoriteErrorCode.FAVORITE_NOT_FOUND.getErrorMessage());
//    }

    @Test
    void 첫_페이지_요청_캐시에서_조회() {
        // given
        List<FavoriteStoreSummaryDto> cachedList = List.of(mock(FavoriteStoreSummaryDto.class));
        given(favoriteCacheService.getFavoriteStores(userId, firstPageRequest)).willReturn(cachedList);

        // when
        favoriteService.getFavoriteStores(userId, firstPageRequest);

        // then
        verify(favoriteCacheService).getFavoriteStores(userId, firstPageRequest);
        verify(favoriteRepository, never()).loadFavoriteStoreSummaries(any(), any());
        verify(pageAssembler).assemble(any(), eq(10), any(), any());
    }

    @Test
    void 두_번째_페이지_부터_DB_조회() {
        //given
        List<FavoriteStoreSummaryDto> dbList = List.of(mock(FavoriteStoreSummaryDto.class));
        given(favoriteRepository.loadFavoriteStoreSummaries(userId, nextPageRequest)).willReturn(dbList);

        // when
        favoriteService.getFavoriteStores(userId, nextPageRequest);

        // then
        verify(favoriteCacheService, never()).getFavoriteStores(any(), any());
        verify(favoriteRepository).loadFavoriteStoreSummaries(userId, nextPageRequest);
        verify(pageAssembler).assemble(any(), eq(10), any(), any());
    }
}