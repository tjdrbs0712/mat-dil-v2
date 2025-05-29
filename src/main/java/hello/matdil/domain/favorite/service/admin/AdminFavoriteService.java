package hello.matdil.domain.favorite.service.admin;

import hello.matdil.domain.favorite.dto.AdminFavoriteRequestDto;
import hello.matdil.domain.user.entity.UserRole;
import jakarta.validation.Valid;

public interface AdminFavoriteService {
    void addFavorite(UserRole role, AdminFavoriteRequestDto requestDto);

    void removeFavorite(UserRole role, @Valid AdminFavoriteRequestDto request);
}
