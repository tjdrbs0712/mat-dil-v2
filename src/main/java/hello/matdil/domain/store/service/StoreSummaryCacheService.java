package hello.matdil.domain.store.service;

import hello.matdil.domain.store.dto.StoreSummaryDto;
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

    public Optional<StoreSummaryDto> get(Long storeId) {
        return redisCacheHelper.get(getKey(storeId), StoreSummaryDto.class);
    }

    public void put(Long storeId, StoreSummaryDto dto) {
        redisCacheHelper.put(getKey(storeId), dto, TTL);
    }

    public void evict(Long storeId) {
        redisCacheHelper.delete(getKey(storeId));
    }

    public Map<Long, StoreSummaryDto> getBatch(List<Long> storeIds) {
        List<String> keys = storeIds.stream()
                .map(this::getKey)
                .collect(Collectors.toList());

        Map<String, StoreSummaryDto> result = redisCacheHelper.multiGet(keys, StoreSummaryDto.class);

        return result.entrySet().stream()
                .collect(Collectors.toMap(
                        entry ->
                                Long.parseLong(entry.getKey().replace("store:summary:", "")),
                        Map.Entry::getValue
                ));
    }
}

