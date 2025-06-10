package hello.matdil.domain.store.menu.reader;

import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.store.menu.exception.MenuErrorCode;
import hello.matdil.domain.store.menu.exception.MenuException;
import hello.matdil.domain.store.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MenuReader{

    private final MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public List<Menu> getMenusWithStoreValidation(List<Long> menuIds, Long storeId) {
        List<Menu> menus = menuRepository.findAllByStoreIdAndIdIn(storeId, menuIds);

        if (menus.size() != menuIds.size()) {
            throw new MenuException(MenuErrorCode.MENU_NOT_FOUND);
        }

        return menus;
    }
}