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
public class MenuUpdateRequestDto {

    @NotBlank
    private String name;

    @Min(0)
    private int price;

    private String description;

    private String imageUrl;

    @Min(1)
    private int orderIndex;

    @NotNull
    private MenuCategory category;

    @NotNull
    private MenuStatus menuStatus;
}
