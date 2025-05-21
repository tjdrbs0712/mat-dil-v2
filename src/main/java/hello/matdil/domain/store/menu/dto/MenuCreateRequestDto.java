package hello.matdil.domain.store.menu.dto;

import hello.matdil.domain.store.menu.entity.MenuCategory;
import hello.matdil.domain.store.menu.entity.MenuStatus;
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
    private MenuStatus menuStatus;

    @NotNull
    private MenuCategory category;

}