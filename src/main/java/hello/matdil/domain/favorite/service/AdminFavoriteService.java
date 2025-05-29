package hello.matdil.domain.favorite.service;

import hello.matdil.domain.favorite.dto.AdminFavoriteRequestDto;
import hello.matdil.domain.user.entity.UserRole;

public interface AdminFavoriteService {
    void addFavorite(UserRole role, AdminFavoriteRequestDto requestDto);
}
