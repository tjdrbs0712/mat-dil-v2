package hello.matdil.domain.store.dto;

import hello.matdil.domain.store.entity.StoreSortType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import static hello.matdil.global.util.CursorKey.*;

@Getter
@Setter
@NoArgsConstructor
public class StoreSearchRequestDto {

    @Size(max = 100, message = "주소는 최대 100자까지 입력할 수 있습니다.")
    private String address;

    @Size(max = 50, message = "가게 이름은 최대 50자까지 입력할 수 있습니다.")
    private String name;

    @Pattern(
            regexp = "rating|name|review|delivery_time",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "정렬 기준은 rating, name, review, delivery_time 중 하나여야 합니다."
    )
    @NotNull(message = "정렬 기준은 필수입니다.")
    private StoreSortType sort = StoreSortType.RATING;

    @Min(value = 1, message = "최소 1개 이상 조회해야 합니다.")
    @Max(value = 100, message = "최대 100개까지 조회할 수 있습니다.")
    private int size = 10;

    // 커서 파라미터들 (nullable 가능성 있음)

    @DecimalMin(value = "0.0", inclusive = true, message = "평점은 0 이상이어야 합니다.")
    private Double lastRating;

    @Size(max = 50, message = "이름 커서는 최대 50자까지 가능합니다.")
    private String lastName;

    @Min(value = 0, message = "예상 배달 시간은 0 이상이어야 합니다.")
    private Integer lastDeliveryTime;

    @Min(value = 0, message = "리뷰 수는 0 이상이어야 합니다.")
    private Integer lastReviewCount;

    @Min(value = 1, message = "가게 ID는 1 이상이어야 합니다.")
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
