package hello.matdil.domain.favorite.service;

import hello.matdil.domain.favorite.entity.Favorite;
import hello.matdil.domain.favorite.execption.FavoriteErrorCode;
import hello.matdil.domain.favorite.execption.FavoriteException;
import hello.matdil.domain.favorite.repository.FavoriteRepository;
import hello.matdil.domain.store.reader.StoreReader;
import hello.matdil.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FavoriteCommandTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private FavoriteCacheService favoriteCacheService;

    @Mock
    private StoreReader storeReader;

    @InjectMocks
    private FavoriteCommand favoriteCommand;

    private final Long userId = 1L;
    private final Long storeId = 10L;
    private final UserRole role = UserRole.USER;

    @Test
    void 즐겨찾기_추가_성공() {
        // given
        given(favoriteRepository.save(any(Favorite.class)))
                .willReturn(Favorite.create(userId, storeId));

        // when
        favoriteCommand.addFavorite(userId, role, storeId);

        // then
        verify(storeReader).readByIdWithPermission(userId, storeId, role);
        verify(favoriteRepository).save(any(Favorite.class));
        verify(favoriteCacheService).deleteAll(userId);
    }

    @Test
    void 이미_즐겨찾기가_된_가게() {
        // given
        given(favoriteRepository.save(any(Favorite.class)))
                .willThrow(DataIntegrityViolationException.class);

        // when & then
        assertThatThrownBy(() -> favoriteCommand.addFavorite(userId, role, storeId))
                .isInstanceOf(FavoriteException.class)
                .hasMessageContaining(FavoriteErrorCode.ALREADY_FAVORITE.getErrorMessage());

        verify(favoriteCacheService, never()).deleteAll(any());
    }

    @Test
    void 즐겨찾기_제거_성공() {
        // given
        Favorite favorite = Favorite.create(userId, storeId);
        given(favoriteRepository.findByUserIdAndStoreId(userId, storeId))
                .willReturn(Optional.of(favorite));

        // when
        favoriteCommand.removeFavorite(userId, role, storeId);

        // then
        verify(favoriteRepository).delete(favorite);
        verify(favoriteCacheService).deleteAll(userId);
    }

    @Test
    void 즐겨찾기가_존재하지_않는_경우() {
        // given
        given(favoriteRepository.findByUserIdAndStoreId(userId, storeId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> favoriteCommand.removeFavorite(userId, role, storeId))
                .isInstanceOf(FavoriteException.class)
                .hasMessageContaining(FavoriteErrorCode.FAVORITE_NOT_FOUND.getErrorMessage());

        verify(favoriteRepository, never()).delete(any());
        verify(favoriteCacheService, never()).deleteAll(any());
    }
}
