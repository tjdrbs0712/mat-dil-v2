package hello.matdil.domain.store.reader;

import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.store.service.StoreSummaryCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StoreSummaryLoader {

    private final StoreSummaryCacheService storeCacheService;
    private final StoreRepository storeRepository;

    public Map<Long, StoreSummaryResponseDto> loadWithCacheFallback(List<Long> storeIds) {
        Map<Long, StoreSummaryResponseDto> cached = storeCacheService.getBatch(storeIds);

        List<Long> missingIds = storeIds.stream()
                .filter(id -> !cached.containsKey(id))
                .toList();

        if (!missingIds.isEmpty()) {
            Map<Long, StoreSummaryResponseDto> loaded = storeRepository.findStoreSummariesByIds(missingIds);
            loaded.forEach(storeCacheService::put);
            cached.putAll(loaded);
        }

        return cached;
    }

    public StoreSummaryResponseDto loadWithCacheFallback(Long storeId) {
        Optional<StoreSummaryResponseDto> cached = storeCacheService.get(storeId);

        if(cached.isPresent()){
            return cached.get();
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        StoreSummaryResponseDto loaded = StoreSummaryResponseDto.from(store);
        storeCacheService.put(storeId, loaded);

        return loaded;
    }
}
