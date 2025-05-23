package hello.matdil.domain.store.reader;

import hello.matdil.domain.store.dto.StoreInfoDto;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.store.service.StoreSummaryCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StoreSummaryLoader {

    private final StoreSummaryCacheService storeCacheService;
    private final StoreRepository storeRepository;

    public Map<Long, StoreInfoDto> loadWithCacheFallback(List<Long> storeIds) {
        Map<Long, StoreInfoDto> cached = storeCacheService.getBatch(storeIds);

        List<Long> missingIds = storeIds.stream()
                .filter(id -> !cached.containsKey(id))
                .toList();

        if (!missingIds.isEmpty()) {
            Map<Long, StoreInfoDto> loaded = storeRepository.findStoreSummariesByIds(missingIds);
            loaded.forEach(storeCacheService::put);
            cached.putAll(loaded);
        }

        return cached;
    }
}
