package hello.matdil.domain.store.menu.dto;

import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.store.menu.entity.MenuCategory;
import hello.matdil.domain.store.menu.entity.MenuStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuResponseDto {

    private Long id;
    private String name;
    private int price;
    private String description;
    private String imageUrl;
    private MenuCategory  category;
    private MenuStatus menuStatus;

    public static MenuResponseDto from(Menu menu) {
        return MenuResponseDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .imageUrl(menu.getImageUrl())
                .category(menu.getCategory())
                .menuStatus(menu.getMenuStatus())
                .build();
    }
}
