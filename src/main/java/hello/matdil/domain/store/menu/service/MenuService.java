package hello.matdil.domain.store.menu.service;

import hello.matdil.domain.store.menu.dto.MenuCreateRequestDto;
import hello.matdil.domain.store.menu.dto.MenuResponseDto;
import hello.matdil.domain.user.entity.UserRole;
import jakarta.validation.Valid;

public interface MenuService {
    MenuResponseDto createMenu(Long userId, UserRole userRole, Long storeId, @Valid MenuCreateRequestDto requestDto);
}
