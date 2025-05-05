package hello.matdil.domain.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderCreateDto(
        Long userId,
        Long storeId,
        List<OrderItemCreateDto> orderItemCreateDtoList,
        String requestNote,
        LocalDateTime expectedDeliveryTime) { }
