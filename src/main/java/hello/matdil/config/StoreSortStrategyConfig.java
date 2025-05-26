package hello.matdil.config;

import hello.matdil.domain.store.entity.StoreSortType;
import hello.matdil.domain.store.sort.*;
import hello.matdil.global.sort.StoreSortStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class StoreSortStrategyConfig {

    @Bean
    public Map<StoreSortType, StoreSortStrategy> storeSortStrategyMap() {
        Map<StoreSortType, StoreSortStrategy> strategyMap = new EnumMap<>(StoreSortType.class);

        strategyMap.put(StoreSortType.RATING, new RatingStoreSortStrategy());
        strategyMap.put(StoreSortType.REVIEW, new ReviewStoreSortStrategy());
        strategyMap.put(StoreSortType.DELIVERY_TIME, new DeliveryTimeStoreSortStrategy());
        strategyMap.put(StoreSortType.NAME, new NameStoreSortStrategy());
        strategyMap.put(StoreSortType.ID, new IdStoreSortStrategy());

        return strategyMap;
    }
}
