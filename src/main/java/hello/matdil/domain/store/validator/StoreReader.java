package hello.matdil.domain.store.validator;

import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreReader {

    private final StoreRepository storeRepository;

    public Store getStoreWithPermission(Long userId, Long storeId, UserRole role) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        if (store.isVisibleTo(role, userId)) {
            throw new StoreException(StoreErrorCode.NO_PERMISSION);
        }
        return store;
    }
}
