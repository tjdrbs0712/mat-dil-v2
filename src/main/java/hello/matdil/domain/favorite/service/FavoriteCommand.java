package hello.matdil.domain.favorite.service;

import hello.matdil.domain.favorite.entity.Favorite;
import hello.matdil.domain.favorite.execption.FavoriteErrorCode;
import hello.matdil.domain.favorite.execption.FavoriteException;
import hello.matdil.domain.favorite.repository.FavoriteRepository;
import hello.matdil.domain.store.reader.StoreReader;
import hello.matdil.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteCommand {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteCacheService favoriteCacheService;
    private final StoreReader storeReader;

    public void addFavorite(Long userId, UserRole role, Long storeId) {
        storeReader.readByIdWithPermission(userId, storeId, role);
        validateCanAdd(userId, storeId);
        Favorite favorite = Favorite.create(userId, storeId);
        favoriteRepository.save(favorite);
        favoriteCacheService.deleteAll(userId);
    }

    private void validateCanAdd(Long userId, Long storeId) {
        if (favoriteRepository.existsByUserIdAndStoreId(userId, storeId)) {
            throw new FavoriteException(FavoriteErrorCode.ALREADY_FAVORITE);
        }
    }

}
