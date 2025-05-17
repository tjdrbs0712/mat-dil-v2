package hello.matdil.domain.store.repository;

import hello.matdil.domain.store.dto.StoreSearchRequestDto;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.user.entity.UserRole;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreQueryRepository {
    Slice<Store> findStoresByCondition(Long userId, UserRole role, StoreSearchRequestDto request);
}
