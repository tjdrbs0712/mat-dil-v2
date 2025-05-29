package hello.matdil.domain.favorite.service;

import hello.matdil.domain.favorite.dto.AdminFavoriteRequestDto;
import hello.matdil.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminFavoriteServiceImpl implements AdminFavoriteService{

    private final FavoriteCommand favoriteCommand;

    @Override
    @Transactional
    public void addFavorite(UserRole role, AdminFavoriteRequestDto requestDto) {
        favoriteCommand.addFavorite(requestDto.userId(), role, requestDto.storeId());
    }
}
