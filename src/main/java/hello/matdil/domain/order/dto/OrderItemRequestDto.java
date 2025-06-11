package hello.matdil.domain.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public record OrderItemRequestDto(
        @NotNull
        Long menuId,

        @Min(1)
        int quantity
) {}
