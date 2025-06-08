package hello.matdil.domain.favorite.service.admin;

import hello.matdil.domain.favorite.dto.AdminFavoriteRequestDto;
import hello.matdil.domain.favorite.service.FavoriteCommand;
import hello.matdil.domain.store.validator.StoreValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminFavoriteServiceImpl implements AdminFavoriteService{

    private final FavoriteCommand favoriteCommand;
    private final StoreValidator storeValidator;

    @Override
    @Transactional
    public void addFavorite(AdminFavoriteRequestDto request) {
        storeValidator.validateAdminExists(request.storeId());
        favoriteCommand.addFavorite(request.userId(), request.storeId());
    }

    @Override
    @Transactional
    public void removeFavorite(AdminFavoriteRequestDto request) {
        storeValidator.validateAdminExists(request.storeId());
        favoriteCommand.removeFavorite(request.userId(), request.storeId());
    }
}
