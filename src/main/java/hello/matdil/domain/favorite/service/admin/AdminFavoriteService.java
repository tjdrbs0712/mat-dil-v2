package hello.matdil.domain.favorite.service.admin;

import hello.matdil.domain.favorite.dto.AdminFavoriteRequestDto;

public interface AdminFavoriteService {
    void addFavorite(AdminFavoriteRequestDto requestDto);

    void removeFavorite(AdminFavoriteRequestDto request);
}
