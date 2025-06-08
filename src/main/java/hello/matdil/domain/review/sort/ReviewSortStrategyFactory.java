package hello.matdil.domain.review.sort;

import hello.matdil.domain.review.entity.ReviewSortType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReviewSortStrategyFactory {

    private final Map<ReviewSortType, ReviewSortStrategy> strategyMap = new HashMap<>();

    public ReviewSortStrategyFactory(List<ReviewSortStrategy> strategies) {
        for (ReviewSortStrategy strategy : strategies) {
            strategyMap.put(strategy.getSortType(), strategy);
        }
    }

    public ReviewSortStrategy getStrategy(ReviewSortType sortType) {
        return strategyMap.get(sortType);
    }

}
