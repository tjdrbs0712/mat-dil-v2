package hello.matdil.domain.store.service;

import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
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
    private static final String STORE_SUMMARY_KEY_PREFIX = "store:summary:";
    private static final Duration TTL = Duration.ofMinutes(10);

    private String getKey(Long storeId) {
        return STORE_SUMMARY_KEY_PREFIX + storeId;
    }

    public Optional<StoreSummaryResponseDto> get(Long storeId) {
        return redisCacheHelper.get(getKey(storeId), StoreSummaryResponseDto.class);
    }

    public void put(Long storeId, StoreSummaryResponseDto dto) {
        redisCacheHelper.put(getKey(storeId), dto, TTL);
    }

    public void evict(Long storeId) {
        redisCacheHelper.delete(getKey(storeId));
    }

    public Map<Long, StoreSummaryResponseDto> getBatch(List<Long> storeIds) {
        List<String> keys = storeIds.stream()
                .map(this::getKey)
                .collect(Collectors.toList());

        Map<String, StoreSummaryResponseDto> result = redisCacheHelper.multiGet(keys, StoreSummaryResponseDto.class);

        return result.entrySet().stream()
                .collect(Collectors.toMap(
                        entry ->
                                Long.parseLong(entry.getKey().replace(STORE_SUMMARY_KEY_PREFIX, "")),
                        Map.Entry::getValue
                ));
    }
}

