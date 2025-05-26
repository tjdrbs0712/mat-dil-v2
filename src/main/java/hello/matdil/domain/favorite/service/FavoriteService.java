package hello.matdil.domain.favorite.service;

import hello.matdil.domain.user.entity.UserRole;

public interface FavoriteService {

    void addFavorite(Long userId, UserRole role, Long storeId);

    void removeFavorite(Long userId, UserRole role, Long storeId);
}
