package hello.matdil.domain.store.menu.dto;

import hello.matdil.domain.store.entity.StoreStatus;
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
public class MenuCreateRequestDto {

    @NotBlank
    private String name;

    @NotNull
    @Min(value = 0, message = "최소 가격은 0원 이상이어야 합니다.")
    private Integer price;

    private String description;
    private String imageUrl;

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