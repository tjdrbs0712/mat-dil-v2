package hello.matdil.domain.store.menu.validator;

import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.store.menu.exception.MenuErrorCode;
import hello.matdil.domain.store.menu.exception.MenuException;
import hello.matdil.domain.store.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuReader{

    private final MenuRepository menuRepository;

    public Menu getMenuWithStoreValidation(Long menuId, Long storeId) {
        return menuRepository.findByIdWithStore(menuId, storeId)
                .orElseThrow(() -> new MenuException(MenuErrorCode.MENU_NOT_FOUND));
    }
}