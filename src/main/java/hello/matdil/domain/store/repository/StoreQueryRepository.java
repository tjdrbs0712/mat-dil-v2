package hello.matdil.domain.store.repository;

import hello.matdil.domain.store.entity.Store;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreQueryRepository {
    Slice<Store> findStoresByCondition(String address, String name, String sort, int page, int size);
}
