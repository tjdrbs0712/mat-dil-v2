package hello.matdil.domain.userorderstatus.dto;

import java.time.LocalDateTime;
import java.util.Map;

import static hello.matdil.global.constant.RedisUserOrderStatsKeys.FIELD_LAST_ORDERED_AT;
import static hello.matdil.global.constant.RedisUserOrderStatsKeys.FIELD_ORDER_COUNT;

public record UserOrderStatsDto(
        int orderCount,
        LocalDateTime lastOrderedAt
) {
    public static UserOrderStatsDto from(Map<Object, Object> hash) {
        int orderCount = Integer.parseInt(hash.getOrDefault(FIELD_ORDER_COUNT, "0").toString());
        LocalDateTime lastOrderedAt = LocalDateTime.parse(
                hash.getOrDefault(FIELD_LAST_ORDERED_AT, LocalDateTime.MIN.toString()).toString()
        );
        return new UserOrderStatsDto(orderCount, lastOrderedAt);
    }
}
