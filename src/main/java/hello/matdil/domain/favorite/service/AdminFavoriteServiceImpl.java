package hello.matdil.domain.favorite.service;

import hello.matdil.domain.favorite.dto.AdminFavoriteRequestDto;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.domain.user.reader.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminFavoriteServiceImpl implements AdminFavoriteService{

    private final FavoriteCommand favoriteCommand;
    private final UserReader userReader;

    @Override
    @Transactional
    public void addFavorite(Long userId, UserRole role, AdminFavoriteRequestDto requestDto) {
        User user = userReader.getUser(userId);
        user.adminChecker(role);
        favoriteCommand.addFavorite(requestDto.userId(), role, requestDto.storeId());
    }
}
