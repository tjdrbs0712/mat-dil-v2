package hello.matdil.domain.store.menu.factory;

import hello.matdil.domain.store.menu.dto.MenuCreateRequestDto;
import hello.matdil.domain.store.menu.entity.Menu;
import org.springframework.stereotype.Component;

@Component
public class MenuFactory {

    public Menu createMenu(MenuCreateRequestDto dto){
        return Menu.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .category(dto.getCategory())
                .menuStatus(dto.getMenuStatus())
                .build();
    }
}
