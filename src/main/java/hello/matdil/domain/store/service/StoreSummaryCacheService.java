package hello.matdil.domain.store.service;

import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.infrastructure.redis.RedisDtoCacheHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreSummaryCacheService {

    private final RedisDtoCacheHelper redisDtoCacheHelper;
    private static final String STORE_SUMMARY_KEY_PREFIX = "store:summary:";
    private static final Duration TTL = Duration.ofMinutes(10);

    private String getKey(Long storeId) {
        return STORE_SUMMARY_KEY_PREFIX + storeId;
    }

    public Optional<StoreSummaryResponseDto> get(Long storeId) {
        return redisDtoCacheHelper.get(getKey(storeId), StoreSummaryResponseDto.class);
    }

    public void put(Long storeId, StoreSummaryResponseDto dto) {
        redisDtoCacheHelper.put(getKey(storeId), dto, TTL);
    }

    public void evict(Long storeId) {
        redisDtoCacheHelper.delete(getKey(storeId));
    }

    public Map<Long, StoreSummaryResponseDto> getBatch(List<Long> storeIds) {
        List<String> keys = storeIds.stream()
                .map(this::getKey)
                .collect(Collectors.toList());
        Map<String, StoreSummaryResponseDto> result = redisDtoCacheHelper.multiGet(keys, StoreSummaryResponseDto.class);
        return result.entrySet().stream()
                .collect(Collectors.toMap(
                        entry ->
                                Long.parseLong(entry.getKey().replace(STORE_SUMMARY_KEY_PREFIX, "")),
                        Map.Entry::getValue
                ));
    }
}

