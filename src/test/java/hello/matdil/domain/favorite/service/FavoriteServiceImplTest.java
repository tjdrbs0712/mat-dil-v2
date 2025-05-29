package hello.matdil.domain.favorite.service;

import hello.matdil.domain.favorite.entity.Favorite;
import hello.matdil.domain.favorite.execption.FavoriteErrorCode;
import hello.matdil.domain.favorite.execption.FavoriteException;
import hello.matdil.domain.favorite.repository.FavoriteRepository;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.reader.StoreReader;
import hello.matdil.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StoreReader storeReader;

    @Test
    void 즐겨찾기_정상_등록() {
        // given
        Long userId = 1L;
        Long storeId = 10L;
        UserRole role = UserRole.USER;
        Store store = mock(Store.class);

        given(storeReader.readByIdWithPermission(userId, storeId, role)).willReturn(store);
        given(favoriteRepository.existsByUserIdAndStoreId(userId, storeId)).willReturn(false);

        // when
        favoriteService.addFavorite(userId, role, storeId);

        // then
        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void 즐겨찾기_중복_예외() {
        // given
        Long userId = 1L;
        Long storeId = 10L;
        UserRole role = UserRole.USER;
        Store store = mock(Store.class);

        given(storeReader.readByIdWithPermission(userId, storeId, role)).willReturn(store);
        given(favoriteRepository.existsByUserIdAndStoreId(userId, storeId)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> favoriteService.addFavorite(userId, role, storeId))
                .isInstanceOf(FavoriteException.class)
                .hasMessage(FavoriteErrorCode.ALREADY_FAVORITE.getErrorMessage());
    }

    @Test
    void 즐겨찾기_정상_삭제() {
        // given
        Long userId = 1L;
        Long storeId = 10L;
        UserRole role = UserRole.USER;
        Favorite favorite = Favorite.create(userId, storeId);

        given(favoriteRepository.findByUserIdAndStoreId(userId, storeId))
                .willReturn(Optional.of(favorite));

        // when
        favoriteService.removeFavorite(userId, role, storeId);

        // then
        verify(favoriteRepository).delete(favorite);
    }

    @Test
    void 즐겨찾기_없으면_예외() {
        // given
        Long userId = 1L;
        Long storeId = 10L;
        UserRole role = UserRole.USER;

        given(favoriteRepository.findByUserIdAndStoreId(userId, storeId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> favoriteService.removeFavorite(userId, role, storeId))
                .isInstanceOf(FavoriteException.class)
                .hasMessage(FavoriteErrorCode.FAVORITE_NOT_FOUND.getErrorMessage());
    }
}