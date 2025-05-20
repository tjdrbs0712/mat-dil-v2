package hello.matdil.domain.store.menu.repository;

import hello.matdil.domain.store.menu.dto.MenuCursorRequestDto;
import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.user.entity.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuQueryRepository {
    List<Menu> findMenusByCursor(Long userId, UserRole role, Long storeId, MenuCursorRequestDto cursor);
}
