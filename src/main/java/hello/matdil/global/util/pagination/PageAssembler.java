package hello.matdil.global.util.pagination;

import hello.matdil.global.response.SliceResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class PageAssembler {

    /**
     * 커서 기반 페이징 응답 생성
     */
    public <E, D, C> SliceResponse<D, C> assemble(
            List<E> fullResult,
            int pageSize,
            Function<E, C> cursorCreator,
            Function<E, D> mapper
    ) {
        boolean hasNext = CursorPaginationHelper.hasNext(fullResult, pageSize);
        List<E> sliced = CursorPaginationHelper.trimResult(fullResult, pageSize);
        List<D> content = sliced.stream().map(mapper).toList();

        E last = CursorPaginationHelper.getLast(sliced);
        C nextCursor = (hasNext && last != null) ? cursorCreator.apply(last) : null;

        return SliceResponse.of(content, hasNext, nextCursor);
    }
}
