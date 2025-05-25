package hello.matdil.domain.store.menu.dto;

import hello.matdil.domain.store.menu.entity.MenuCategory;
import hello.matdil.domain.store.menu.entity.MenuStatus;
import hello.matdil.global.validator.EnumValid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuUpdateRequestDto {

    @NotBlank
    private String name;

    @Min(0)
    private int price;

    private String description;

    private String imageUrl;

    @Min(1)
    private int orderIndex;

    @EnumValid(message = "메뉴 상태를 제대로 입력해주세요.", enumClass = MenuStatus.class)
    private String menuStatus;

    @EnumValid(message = "메뉴 카테고리를 제대로 입력해주세요.", enumClass = MenuCategory.class)
    private String category;

    public MenuStatus toMenuStatusEnum() {
        return MenuStatus.valueOf(menuStatus.toUpperCase());
    }

    public MenuCategory toMenuCategoryEnum() {
        return MenuCategory.valueOf(category.toUpperCase());
    }
}
