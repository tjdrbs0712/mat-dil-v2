package hello.matdil.domain.favorite.sort;

import hello.matdil.domain.favorite.entity.FavoriteSortType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FavoriteSortStrategyFactory {

    private final Map<FavoriteSortType, FavoriteSortStrategy> strategyMap = new HashMap<>();

    public FavoriteSortStrategyFactory(List<FavoriteSortStrategy> strategies) {
        for (FavoriteSortStrategy strategy : strategies) {
            strategyMap.put(strategy.getSortType(), strategy);
        }
    }

    public FavoriteSortStrategy getStrategy(FavoriteSortType type) {
        return strategyMap.get(type);
    }
}
