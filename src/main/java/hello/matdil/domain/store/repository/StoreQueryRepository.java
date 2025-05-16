package hello.matdil.domain.store.repository;

import hello.matdil.domain.store.entity.Store;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface StoreQueryRepository {
    Slice<Store> findStoresByCondition(String address, String name, String sort, int size, Map<String, Object> cursorParams);
}
