package hello.matdil.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class Cursor {
    private Object value;     // lastRating, lastName 등
    private Long storeId;     // tie-breaker
}