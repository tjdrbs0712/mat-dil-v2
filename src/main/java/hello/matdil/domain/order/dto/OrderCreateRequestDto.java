package hello.matdil.domain.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequestDto {

    @NotNull
    private Long storeId;

    @NotNull
    private LocalDateTime expectedDeliveryTime;

    private String requestNote;

    @NotEmpty
    private List<OrderItemRequestDto> orderItems;
}