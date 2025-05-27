package hello.matdil.global.constant;

public final class RedisUserOrderStatsKeys {
    private RedisUserOrderStatsKeys() {}

    public static final String KEY_PREFIX = "userOrderStats";
    public static final String FIELD_ORDER_COUNT = "orderCount";
    public static final String FIELD_LAST_ORDERED_AT = "lastOrderedAt";

    public static String buildKey(Long userId, Long storeId) {
        return "%s:%d:%d".formatted(KEY_PREFIX, userId, storeId);
    }
}
