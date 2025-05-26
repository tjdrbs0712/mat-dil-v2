package hello.matdil.domain.favorite.dto;

import jakarta.validation.constraints.NotNull;

public record FavoriteRequestDto(
        @NotNull(message = "가게 ID는 필수입니다.")
        Long storeId
) {
}
