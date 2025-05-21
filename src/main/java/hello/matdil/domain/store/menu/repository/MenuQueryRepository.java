package hello.matdil.domain.store.menu.repository;

import hello.matdil.domain.store.menu.dto.MenuCursorRequestDto;
import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.user.entity.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuQueryRepository {
    List<Menu> findMenusByCursor(Long userId, UserRole role, Long storeId, MenuCursorRequestDto cursor);
    Optional<Menu> findByIdWithStore(Long menuId, Long storeId);
}
