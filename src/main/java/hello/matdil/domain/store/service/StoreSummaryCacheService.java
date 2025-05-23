package hello.matdil.domain.store.service;

import hello.matdil.domain.store.dto.StoreInfoDto;
import hello.matdil.infrastructure.RedisCacheHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreSummaryCacheService {

    private final RedisCacheHelper redisCacheHelper;
    private static final Duration TTL = Duration.ofDays(1);

    private String getKey(Long storeId) {
        return "store:summary:" + storeId;
    }

    public Optional<StoreInfoDto> get(Long storeId) {
        return redisCacheHelper.get(getKey(storeId), StoreInfoDto.class);
    }

    public void put(Long storeId, StoreInfoDto dto) {
        redisCacheHelper.put(getKey(storeId), dto, TTL);
    }

    public void evict(Long storeId) {
        redisCacheHelper.delete(getKey(storeId));
    }

    public Map<Long, StoreInfoDto> getBatch(List<Long> storeIds) {
        List<String> keys = storeIds.stream()
                .map(this::getKey)
                .collect(Collectors.toList());

        Map<String, StoreInfoDto> result = redisCacheHelper.multiGet(keys, StoreInfoDto.class);

        return result.entrySet().stream()
                .collect(Collectors.toMap(
                        entry ->
                                Long.parseLong(entry.getKey().replace("store:summary:", "")),
                        Map.Entry::getValue
                ));
    }
}

