package hello.matdil.domain.store.menu.service;

import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.menu.dto.MenuCreateRequestDto;
import hello.matdil.domain.store.menu.dto.MenuCursorRequestDto;
import hello.matdil.domain.store.menu.dto.MenuCursorResponseDto;
import hello.matdil.domain.store.menu.dto.MenuResponseDto;
import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.store.menu.exception.MenuErrorCode;
import hello.matdil.domain.store.menu.exception.MenuException;
import hello.matdil.domain.store.menu.factory.MenuFactory;
import hello.matdil.domain.store.menu.repository.MenuRepository;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.store.validator.StoreReader;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.util.pagination.PageAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final MenuFactory menuFactory;
    private final PageAssembler pageAssembler;
    private final StoreReader storeReader;

    @Override
    @Transactional
    public MenuResponseDto createMenu(Long userId, UserRole role, Long storeId, MenuCreateRequestDto requestDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        Menu menu = menuFactory.createMenu(requestDto);
        store.addMenu(menu, userId, role);

        menuRepository.save(menu);
        return MenuResponseDto.from(menu);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponse<MenuResponseDto, MenuCursorResponseDto> getMenus(
            Long userId, UserRole role, Long storeId, MenuCursorRequestDto cursor) {

        storeReader.getStoreWithPermission(userId, storeId, role);

        List<Menu> menus = menuRepository.findMenusByCursor(userId, role, storeId, cursor);
        int pageSize = cursor.pageSize();

        return pageAssembler.assemble(
                menus,
                pageSize,
                last -> new MenuCursorResponseDto(pageSize, last.getOrderIndex(), last.getId()),
                MenuResponseDto::from
        );
    }

    @Override
    @Transactional(readOnly = true)
    public MenuResponseDto getMenu(Long userId, UserRole role, Long storeId, Long menuId) {

        storeReader.getStoreWithPermission(userId, storeId, role);

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(MenuErrorCode.MENU_NOT_FOUND));

        if (!menu.isVisibleTo(role, userId, menu.getStore().getOwnerId())) {
            throw new MenuException(MenuErrorCode.NO_PERMISSION);
        }

        return MenuResponseDto.from(menu);
    }
}
