package hello.matdil.domain.store.reader;

import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class StoreReader {

    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public Store readByIdWithPermission(Long userId, Long storeId, UserRole role) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));
        store.validateVisibleTo(role, userId);
        return store;
    }

    @Transactional(readOnly = true)
    public Store readWithOpen(Long storeId) {
        return storeRepository.findByIdWithOpen(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Store readWithNotDeletedWithPermission(Long userId, Long storeId, UserRole role) {
        Store store = storeRepository.findByIdWithNotDeleted(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));
        store.validateAccessibleTo(userId, role);
        return store;
    }

}
