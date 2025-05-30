package hello.matdil.domain.store.menu.repository;

import hello.matdil.domain.store.menu.dto.MenuCursorRequestDto;
import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.user.entity.UserRole;

import java.util.List;
import java.util.Optional;

public interface MenuQueryRepository {
    List<Menu> findMenusByCursor(Long userId, UserRole role, Long storeId, MenuCursorRequestDto cursor);
    Optional<Menu> findByIdWithStoreFetchJoinNotDeleted(Long menuId, Long storeId);
    Optional<Menu> findByIdWithStore(Long menuId, Long storeId);
}
