package hello.matdil.domain.store.menu.service;

import hello.matdil.domain.store.menu.dto.MenuCreateRequestDto;
import hello.matdil.domain.store.menu.dto.MenuCursorRequestDto;
import hello.matdil.domain.store.menu.dto.MenuCursorResponseDto;
import hello.matdil.domain.store.menu.dto.MenuResponseDto;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;
import jakarta.validation.Valid;

public interface MenuService {
    MenuResponseDto createMenu(Long userId, UserRole userRole, Long storeId, @Valid MenuCreateRequestDto requestDto);

    SliceResponse<MenuResponseDto, MenuCursorResponseDto> getMenus(Long userId, UserRole role, Long storeId, MenuCursorRequestDto cursor);

    MenuResponseDto getMenu(Long userId, UserRole role, Long storeId, Long menuId);
}
