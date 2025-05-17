package hello.matdil.domain.store.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import static hello.matdil.global.util.CursorKey.*;

@Getter
@Setter
public class StoreSearchRequestDto {

    private String address;
    private String name;
    private String sort = "rating";
    private int size = 10;

    private Double lastRating;
    private String lastName;
    private Integer lastDeliveryTime;
    private Integer lastReviewCount;
    private Long lastStoreId;

    public Map<String, Object> toCursorParamMap() {
        Map<String, Object> map = new HashMap<>();
        if (lastRating != null) map.put(LAST_RATING, lastRating);
        if (lastName != null) map.put(LAST_NAME, lastName);
        if (lastDeliveryTime != null) map.put(LAST_DELIVERY_TIME, lastDeliveryTime);
        if (lastReviewCount != null) map.put(LAST_REVIEW_COUNT, lastReviewCount);
        if (lastStoreId != null) map.put(LAST_STORE_ID, lastStoreId);
        return map;
    }
}
