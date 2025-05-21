package hello.matdil.global.util.pagination;

import java.util.Collections;
import java.util.List;

public class CursorPaginationHelper {

    public static <T> List<T> trimResult(List<T> fullResult, int pageSize) {
        if (fullResult.size() <= pageSize) return fullResult;
        return fullResult.subList(0, pageSize);
    }

    public static <T> boolean hasNext(List<T> fullResult, int pageSize) {
        return fullResult.size() > pageSize;
    }

    public static <T> T getLast(List<T> list) {
        if (list == null || list.isEmpty()) return null;
        return list.get(list.size() - 1);
    }

    public static <T> List<T> nullSafe(List<T> list) {
        return list != null ? list : Collections.emptyList();
    }
}