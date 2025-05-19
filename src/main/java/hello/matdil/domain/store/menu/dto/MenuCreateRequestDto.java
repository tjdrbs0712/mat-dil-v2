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
public class MenuCreateRequestDto {

    @NotBlank
    private String name;

    @NotNull
    @Min(value = 0, message = "최소 가격은 0원 이상이어야 합니다.")
    private Integer price;

    private String description;
    private String imageUrl;

    @NotNull
    @EnumValid(enumClass = MenuStatus.class, message = "올바른 메뉴 상태를 입력해주세요.")
    private String menuStatus;

    @NotNull
    @EnumValid(enumClass = MenuCategory.class, message = "올바른 카테고리를 입력해주세요.")
    private String category;

    public MenuStatus getParsedMenuStatus() {
        return MenuStatus.valueOf(menuStatus);
    }

    public MenuCategory getParsedCategory() {
        return MenuCategory.valueOf(category);
    }
}