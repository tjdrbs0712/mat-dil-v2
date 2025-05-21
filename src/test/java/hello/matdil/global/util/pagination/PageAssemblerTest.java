package hello.matdil.global.util.pagination;

import hello.matdil.global.response.SliceResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageAssemblerTest {

    private final PageAssembler pageAssembler = new PageAssembler();

    @Test
    void hasNext_있고_커서_생성되는_경우() {
        // given
        List<String> fullResult = List.of("A", "B", "C", "D");
        int pageSize = 3;

        // when
        SliceResponse<String, String> response = pageAssembler.assemble(
                fullResult,
                pageSize,
                last -> "cursor:" + last,
                value -> value
        );

        // then
        assertThat(response.getContent()).containsExactly("A", "B", "C");
        assertThat(response.isHasNext()).isTrue();
        assertThat(response.getNextCursor()).isEqualTo("cursor:C");
    }

    @Test
    void hasNext_없고_커서_null_인_경우() {
        // given
        List<String> fullResult = List.of("A", "B");
        int pageSize = 3;

        // when
        SliceResponse<String, String> response = pageAssembler.assemble(
                fullResult,
                pageSize,
                last -> "cursor:" + last,
                value -> value
        );

        // then
        assertThat(response.getContent()).containsExactly("A", "B");
        assertThat(response.isHasNext()).isFalse();
        assertThat(response.getNextCursor()).isNull();
    }
}