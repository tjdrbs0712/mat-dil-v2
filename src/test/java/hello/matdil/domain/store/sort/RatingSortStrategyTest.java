package hello.matdil.domain.store.sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.store.entity.QStore;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RatingSortStrategyTest {

    private final RatingSortStrategy ratingSortStrategy = new RatingSortStrategy();
    private final QStore store = QStore.store;

    @Test
    void 정상적인_정렬_조건() {
        // when
        OrderSpecifier<?>[] orderSpecifiers = ratingSortStrategy.getOrderSpecifiers(store);

        // then
        assertThat(orderSpecifiers).hasSize(2);
        assertThat(orderSpecifiers[0].toString()).contains("store.rating DESC");
        assertThat(orderSpecifiers[1].toString()).contains("store.id DESC");
    }

    @Test
    void 커서_조건이_정상적일_경우(){
        //given
        Map<String, Object> cursor = Map.of("lastRating", 4.5, "lastStoreId", 123L);
        // when
        BooleanExpression expression = ratingSortStrategy.buildCursorPredicate(store,cursor);
        //then
        String actual = expression.toString();
        System.out.println(actual);
        assertThat(actual).contains("store.rating <").contains("store.rating =").contains("store.id <");
    }

    @Test
    void 커서_조건이_정상적이지_않을_경우(){
        //given
        Map<String, Object> cursor = Map.of("lastRating", 4.5);
        // when
        BooleanExpression expression = ratingSortStrategy.buildCursorPredicate(store,cursor);
        //then
        assertThat(expression).isNull();
    }

}