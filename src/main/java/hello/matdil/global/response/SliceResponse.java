package hello.matdil.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class SliceResponse<T, K> {
    private List<T> content;
    private boolean hasNext;
    private K nextCursor;
}
