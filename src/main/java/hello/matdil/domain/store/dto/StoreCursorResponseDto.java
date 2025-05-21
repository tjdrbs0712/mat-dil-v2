package hello.matdil.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "from")
public class StoreCursorResponseDto {
    private Object value;     // lastRating, lastName 등
    private Long storeId;     // tie-breaker
}