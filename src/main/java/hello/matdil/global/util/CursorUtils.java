package hello.matdil.global.util;

import java.util.Collections;
import java.util.List;

public class CursorUtils {

    /**
     * 리스트에서 마지막 요소를 안전하게 가져옴
     */
    public static <T> T getLast(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    /**
     * 최대 page size를 제한하면서 null-safe 하게 처리
     */
    public static int safePageSize(Integer size, int defaultSize, int maxSize) {
        if (size == null || size <= 0) return defaultSize;
        return Math.min(size, maxSize);
    }

    /**
     * 불변 리스트로 변환 (null 방지)
     */
    public static <T> List<T> nullSafeList(List<T> list) {
        return list != null ? list : Collections.emptyList();
    }
}