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

@Component
@RequiredArgsConstructor
public class StoreSummaryLoader {

    private final StoreSummaryCacheService cacheService;
    private final StoreRepository storeRepository;

    public Map<Long, StoreSummaryResponseDto> loadWithCacheFallback(List<Long> storeIds) {
        Map<Long, StoreSummaryResponseDto> cached = cacheService.getBatch(storeIds);

        List<Long> missingIds = storeIds.stream()
                .filter(id -> !cached.containsKey(id))
                .toList();

        if (!missingIds.isEmpty()) {
            Map<Long, StoreSummaryResponseDto> loaded = storeRepository.findStoreSummariesByIds(missingIds);
            loaded.forEach(cacheService::put);
            cached.putAll(loaded);
        }

        return cached;
    }

    public StoreSummaryResponseDto loadWithCacheFallback(Long storeId) {
        return cacheService.get(storeId)
                .orElseGet(() -> {
                    Store store = storeRepository.findById(storeId)
                            .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));
                    StoreSummaryResponseDto dto = StoreSummaryResponseDto.from(store);
                    cacheService.put(storeId, dto);
                    return dto;
                });
    }
}
