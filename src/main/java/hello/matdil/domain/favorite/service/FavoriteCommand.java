package hello.matdil.domain.favorite.service;

import hello.matdil.domain.favorite.entity.Favorite;
import hello.matdil.domain.favorite.execption.FavoriteErrorCode;
import hello.matdil.domain.favorite.execption.FavoriteException;
import hello.matdil.domain.favorite.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteCommand {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteCacheService favoriteCacheService;

    public void addFavorite(Long userId, Long storeId) {

        try {
            favoriteRepository.save(Favorite.create(userId, storeId));
        } catch (DataIntegrityViolationException e) {
            throw new FavoriteException(FavoriteErrorCode.ALREADY_FAVORITE);
        }

        favoriteCacheService.deleteAll(userId);
    }

    public void removeFavorite(Long userId, Long storeId) {
        Favorite favorite = favoriteRepository.findByUserIdAndStoreId(userId, storeId)
                .orElseThrow(() -> new FavoriteException(FavoriteErrorCode.FAVORITE_NOT_FOUND));

        favoriteRepository.delete(favorite);
        favoriteCacheService.deleteAll(userId);
    }
}
