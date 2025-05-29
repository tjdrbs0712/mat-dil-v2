package hello.matdil.domain.favorite.dto;


import jakarta.validation.constraints.NotNull;

public record AdminFavoriteRequestDto(
        @NotNull(message = "가게 ID는 필수입니다.")
        Long userId,

        @NotNull(message = "유저 ID는 필수입니다.")
        Long storeId
) {
}
