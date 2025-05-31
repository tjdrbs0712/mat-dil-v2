package hello.matdil.domain.store.validator;

import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.entity.StoreStatus;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.store.service.StoreSummaryCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StoreValidator {

    private final StoreSummaryCacheService cacheService;
    private final StoreRepository storeRepository;

    public void validateUserExists(Long storeId) {
        validateExistsWithExcludedStatuses(storeId, List.of(StoreStatus.DELETED, StoreStatus.INACTIVE));
    }

    public void validateOwnerExists(Long storeId) {
        validateExistsWithExcludedStatuses(storeId, List.of(StoreStatus.DELETED));
    }

    public void validateAdminExists(Long storeId) {
        boolean exists = cacheService.get(storeId).isPresent() || storeRepository.existsById(storeId);
        if (!exists) {
            throw new StoreException(StoreErrorCode.STORE_NOT_FOUND);
        }
    }

    public void validateOwnerOf(Long storeId, Long ownerId) {
        Optional<StoreSummaryResponseDto> cached = cacheService.get(storeId);
        if (cached.isPresent()) {
            if (!cached.get().getOwnerId().equals(ownerId)) {
                throw new StoreException(StoreErrorCode.NO_PERMISSION);
            }
            return;
        }

        Store store = storeRepository.findByIdWithNotDeleted(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        store.validateOwnerId(ownerId);
    }

    public void validateOwnerAccessible(Long storeId, Long ownerId) {
        Optional<StoreSummaryResponseDto> cached = cacheService.get(storeId);

        if (cached.isPresent()) {
            StoreSummaryResponseDto dto = cached.get();
            if (dto.getStatus() == StoreStatus.DELETED) {
                throw new StoreException(StoreErrorCode.STORE_NOT_FOUND);
            }
            if (!dto.getOwnerId().equals(ownerId)) {
                throw new StoreException(StoreErrorCode.NO_PERMISSION);
            }
            return;
        }

        Store store = storeRepository.findByIdWithNotDeleted(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        store.validateOwnerId(ownerId);
    }


    private void validateExistsWithExcludedStatuses(Long storeId, List<StoreStatus> excludedStatuses) {
        Optional<StoreSummaryResponseDto> cached = cacheService.get(storeId);

        if (cached.isPresent()) {
            StoreStatus status = cached.get().getStatus();
            if (excludedStatuses.contains(status)) {
                throw new StoreException(StoreErrorCode.STORE_NOT_FOUND);
            }
            return;
        }

        boolean exists = storeRepository.existsByIdAndStatusNotIn(storeId, excludedStatuses);
        if (!exists) {
            throw new StoreException(StoreErrorCode.STORE_NOT_FOUND);
        }
    }
}
