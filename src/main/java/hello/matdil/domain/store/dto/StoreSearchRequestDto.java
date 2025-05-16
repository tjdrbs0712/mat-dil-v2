package hello.matdil.domain.store.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

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
        if (lastRating != null) map.put("lastRating", lastRating);
        if (lastName != null) map.put("lastName", lastName);
        if (lastDeliveryTime != null) map.put("lastDeliveryTime", lastDeliveryTime);
        if (lastReviewCount != null) map.put("lastReviewCount", lastReviewCount);
        if (lastStoreId != null) map.put("lastStoreId", lastStoreId);
        return map;
    }
}
