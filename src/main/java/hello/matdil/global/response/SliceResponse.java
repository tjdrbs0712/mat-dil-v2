package hello.matdil.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class SliceResponse<T> {
    private List<T> content;
    private boolean hasNext;
    private int page;
    private int size;
}
