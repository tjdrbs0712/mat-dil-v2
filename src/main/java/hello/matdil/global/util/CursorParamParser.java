package hello.matdil.global.util;

import hello.matdil.global.exception.BusinessException;
import hello.matdil.global.exception.CommonErrorCode;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CursorParamParser {

    private static final Set<String> INTEGER_KEYS = Set.of("lastDeliveryTime", "lastReviewCount");
    private static final Set<String> LONG_KEYS = Set.of("lastStoreId");
    private static final Set<String> DOUBLE_KEYS = Set.of("lastRating");

    public static Map<String, Object> parse(Map<String, String> rawParams) {
        Map<String, Object> parsed = new HashMap<>();

        for (Map.Entry<String, String> entry : rawParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (!StringUtils.hasText(value)) continue;

            try {
                if (INTEGER_KEYS.contains(key)) {
                    parsed.put(key, Integer.valueOf(value));
                } else if (LONG_KEYS.contains(key)) {
                    parsed.put(key, Long.valueOf(value));
                } else if (DOUBLE_KEYS.contains(key)) {
                    parsed.put(key, Double.valueOf(value));
                } else {
                    parsed.put(key, value);
                }
            } catch (NumberFormatException e) {
                throw new BusinessException(CommonErrorCode.INVALID_CURSOR_VALUE);
            }

        }

        return parsed;
    }
}
