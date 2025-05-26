package hello.matdil.config;

import hello.matdil.domain.store.entity.StoreSortType;
import hello.matdil.domain.store.sort.*;
import hello.matdil.global.sort.SortStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class StoreSortStrategyConfig {

    @Bean
    public Map<StoreSortType, SortStrategy> storeSortStrategyMap() {
        Map<StoreSortType, SortStrategy> strategyMap = new EnumMap<>(StoreSortType.class);

        strategyMap.put(StoreSortType.RATING, new RatingSortStrategy());
        strategyMap.put(StoreSortType.REVIEW, new ReviewSortStrategy());
        strategyMap.put(StoreSortType.DELIVERY_TIME, new DeliveryTimeSortStrategy());
        strategyMap.put(StoreSortType.NAME, new NameSortStrategy());
        strategyMap.put(StoreSortType.ID, new IdSortStrategy());

        return strategyMap;
    }
}
