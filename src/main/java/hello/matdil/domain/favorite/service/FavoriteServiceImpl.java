package hello.matdil.domain.favorite.service;

import hello.matdil.domain.favorite.entity.Favorite;
import hello.matdil.domain.favorite.execption.FavoriteErrorCode;
import hello.matdil.domain.favorite.execption.FavoriteException;
import hello.matdil.domain.favorite.repository.FavoriteRepository;
import hello.matdil.domain.store.reader.StoreReader;
import hello.matdil.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService{

    private final FavoriteRepository favoriteRepository;
    private final StoreReader storeReader;

    @Override
    @Transactional
    public void addFavorite(Long userId, UserRole role, Long storeId) {
        storeReader.readByIdWithPermission(userId, storeId, role);
        validateCanAdd(userId, storeId);
        Favorite favorite = Favorite.create(userId, storeId);
        favoriteRepository.save(favorite);
    }

    private void validateCanAdd(Long userId, Long storeId) {
        if (favoriteRepository.existsByUserIdAndStoreId(userId, storeId)) {
            throw new FavoriteException(FavoriteErrorCode.ALREADY_FAVORITE);
        }
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, UserRole role, Long storeId) {
        Favorite favorite = favoriteRepository.findByUserIdAndStoreId(userId, storeId)
                .orElseThrow(() -> new FavoriteException(FavoriteErrorCode.FAVORITE_NOT_FOUND));

        favoriteRepository.delete(favorite);
    }

}
