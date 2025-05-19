package hello.matdil.domain.store.menu.service;

import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.menu.MenuFactory;
import hello.matdil.domain.store.menu.dto.MenuCreateRequestDto;
import hello.matdil.domain.store.menu.dto.MenuResponseDto;
import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.store.menu.repository.MenuRepository;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.validator.PermissionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final MenuFactory menuFactory;

    @Override
    @Transactional
    public MenuResponseDto createMenu(Long userId, UserRole userRole, Long storeId, MenuCreateRequestDto requestDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));
        PermissionValidator.validateOwnerOrAdmin(userId, store.getOwnerId(), userRole);

        Menu menu = menuFactory.createMenu(requestDto);
        store.addMenu(menu);

        menuRepository.save(menu);
        return MenuResponseDto.from(menu);
    }
}
