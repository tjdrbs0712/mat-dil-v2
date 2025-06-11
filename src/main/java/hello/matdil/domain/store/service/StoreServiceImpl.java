package hello.matdil.domain.store.service;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.store.dto.*;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.entity.StoreSortType;
import hello.matdil.domain.store.entity.StoreStatus;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.factory.StoreFactory;
import hello.matdil.domain.store.policy.StoreCreatePolicy;
import hello.matdil.domain.store.reader.StoreSummaryLoader;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.util.pagination.PageAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService{
    private final StoreRepository storeRepository;
    private final StoreFactory storeFactory;
    private final PageAssembler pageAssembler;
    private final StoreCreatePolicy storeCreatePolicy;
    private final StoreSummaryCacheService cacheService;
    private final StoreSummaryLoader storeSummaryLoader;

    @Override
    @Transactional
    public StoreResponseDto createStore(Long userId, UserRole role, StoreCreateRequestDto requestDto) {
        storeCreatePolicy.validateCreatableBy(role);

        Store store = storeFactory.createStore(userId, requestDto);
        storeRepository.save(store);
        return StoreResponseDto.from(store);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponse<StoreSummaryResponseDto, StoreCursorResponseDto> getStores(
            Long userId, UserRole role, StoreSearchRequestDto request) {

        List<Store> stores = storeRepository.findStoresByCondition(userId, role, request);
        int pageSize = request.getSize();

        List<Long> storeIds = stores.stream()
                .map(Store::getId)
                .toList();

        Map<Long, StoreSummaryResponseDto> storeSummaryMap = storeSummaryLoader.loadWithCacheFallback(storeIds);

        return pageAssembler.assemble(
                stores,
                pageSize,
                last -> extractCursor(last, request.toEnum()),
                store -> {
                    StoreSummaryResponseDto summary = storeSummaryMap.get(store.getId());
                    if (summary == null) {
                        throw new StoreException(StoreErrorCode.STORE_NOT_FOUND);
                    }
                    return summary;
                }
        );
    }

    private StoreCursorResponseDto extractCursor(Store store, StoreSortType sort) {
        return switch (sort) {
            case RATING -> StoreCursorResponseDto.from(store.getRating(), store.getId());
            case NAME -> StoreCursorResponseDto.from(store.getName(), store.getId());
            case REVIEW -> StoreCursorResponseDto.from(store.getReviewCount(), store.getId());
            case DELIVERY_TIME -> StoreCursorResponseDto.from(store.getDeliveryTimeEstimate(), store.getId());
            default -> StoreCursorResponseDto.from(store.getId(), store.getId());
        };
    }


    @Override
    @Transactional(readOnly = true)
    public StoreResponseDto getStore(Long userId, UserRole role, Long storeId) {
        Store store = getStoreOrThrow(storeId);

        store.validateVisibleTo(role, userId);

        return StoreResponseDto.from(store);
    }


    @Override
    @Transactional
    public StoreResponseDto updateStore(Long userId, UserRole role, Long storeId, StoreUpdateRequestDto dto) {
        Store store = getStoreOrThrow(storeId);
        store.validateAccessibleTo(userId, role);
        store.validateBusinessHours(dto.getOpenTime(), dto.getCloseTime());
        Address address = storeFactory.update(dto.getCity(), dto.getStreet(), dto.getDetailAddress());
        store.update(dto, address);
        cacheService.evict(storeId);
        return StoreResponseDto.from(store);
    }

    @Override
    @Transactional
    public void changeStoreStatus(Long userId, UserRole role, Long storeId, StoreStatus storeStatus) {
        Store store = getStoreOrThrow(storeId);
        store.validateAccessibleTo(userId, role);
        store.changeStoreStatus(storeStatus);
        cacheService.evict(storeId);
    }

    private Store getStoreOrThrow(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));
    }
}
