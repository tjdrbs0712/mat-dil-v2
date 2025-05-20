package hello.matdil.domain.store.repository;

import hello.matdil.domain.store.dto.StoreSearchRequestDto;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.user.entity.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreQueryRepository {
    List<Store> findStoresByCondition(Long userId, UserRole role, StoreSearchRequestDto request);
}
